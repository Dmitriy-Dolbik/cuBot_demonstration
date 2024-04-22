package com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers;

import com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.creators.RegistrationCallbackHandlerCreator;
import com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.registrationCallbackHandlers.RegistrationCallbackHandler;
import com.avaki.cuBot.constants.Constants;
import com.avaki.cuBot.models.Apartment;
import com.avaki.cuBot.models.User;
import com.avaki.cuBot.models.enums.CommandConstants;
import com.avaki.cuBot.models.enums.Status;
import com.avaki.cuBot.services.ApartmentService;
import com.avaki.cuBot.services.UserService;
import com.avaki.cuBot.utils.markupCreators.ConfirmOrDeclineRegistrationMarkupCreator;
import lombok.EqualsAndHashCode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EqualsAndHashCode(callSuper = true)
public class RegistrationCommandHandler extends AbstractCommandHandler {

    @Value("${admin_id}")
    public long admin_id;

    @Autowired
    private UserService userService;

    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ConfirmOrDeclineRegistrationMarkupCreator confirmOrDeclineRegistrationMarkupCreator;

    @Autowired
    private RegistrationCallbackHandlerCreator registrationCallbackHandlerCreator;

    @Override
    public String getCommandName() {
        return CommandConstants.REGISTER.getValue();
    }

    @Override
    public String getCommandDescription() {
        return "Зарегестрироваться в боте";
    }

    @Override
    public void handle(Message receivedMessage) {
        long chatId = receivedMessage.getChatId();
        User registeringUser = userService.findById(chatId).orElse(null);
        if (registeringUser == null) {
            org.telegram.telegrambots.meta.api.objects.User sourceUser = receivedMessage.getFrom();
            User newUser = modelMapper.map(sourceUser, User.class);
            handle(chatId, newUser);
        } else if (Status.MOVED.equals(registeringUser.getStatus())) {
            registeringUser.setStatus(Status.ACTIVE);
            registeringUser.setRegisteredDate(LocalDateTime.now());
            handle(chatId, registeringUser);
        } else {
            bot.sendMessage(chatId, "Вы уже зарегестрированы");
        }
    }

    private void handle(long registeringUserChatId, User registeringUser) {
        bot.getRegisteringUserMap().put(registeringUserChatId, registeringUser);

        List<Apartment> apartmentsAvailableToUserRegistration = getApartmentsAvailableToUserRegistration();
        SendMessage messageToAdmin = createMessageToAdmin(registeringUser, apartmentsAvailableToUserRegistration);
        bot.sendMessage(messageToAdmin);
        bot.sendMessage(registeringUserChatId, "Ваша заявка на регистрацию успешно отправлена");

        apartmentsAvailableToUserRegistration.forEach(apartment -> {
            RegistrationCallbackHandler registrationCallbackHandler = registrationCallbackHandlerCreator.create(apartment.getNumber(), bot);
            bot.getCallBackHandlersMap().put(registrationCallbackHandler.getCallBack(), registrationCallbackHandler);
        });
    }

    private List<Apartment> getApartmentsAvailableToUserRegistration() {
        return apartmentService.findAllWithUsers()
                .stream()
                .filter(apartment -> apartment.getUsers().stream()
                        .allMatch(user -> user.getStatus() == Status.MOVED))
                .collect(Collectors.toList());
    }

    private SendMessage createMessageToAdmin(User registeringUser, List<Apartment> apartmentsAvailableToUserRegistration) {
        InlineKeyboardMarkup inlineKeyboardMarkup = confirmOrDeclineRegistrationMarkupCreator.create(apartmentsAvailableToUserRegistration);
        String textToSend = getRegistrationTextForAdmin(registeringUser);
        SendMessage messageToSend = SendMessage.builder()
                .chatId(admin_id)
                .text(textToSend)
                .build();
        messageToSend.setReplyMarkup(inlineKeyboardMarkup);
        return messageToSend;
    }

    private String getRegistrationTextForAdmin(User registeringUser) {
        String userInformation = String.format("Пользователь пытается зарегестрироваться:" +
                        "\n" +
                        Constants.CHAT_ID +
                        "\n" +
                        "FirstName: %s" +
                        "\n" +
                        "LastName: %s" +
                        "\n" +
                        "UserName: %s"
                , registeringUser.getChatId(), registeringUser.getFirstName(), registeringUser.getLastName(), registeringUser.getUserName());
        return userInformation +
                "\n" +
                "Выберите номер студии или отклоните регистрацию";
    }
}
