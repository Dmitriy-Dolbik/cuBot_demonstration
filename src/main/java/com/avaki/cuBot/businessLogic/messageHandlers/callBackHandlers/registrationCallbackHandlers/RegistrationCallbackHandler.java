package com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.registrationCallbackHandlers;

import com.avaki.cuBot.businessLogic.TelegramBot;
import com.avaki.cuBot.businessLogic.messageHandlers.CuSendingStarter;
import com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.AbstractCallbackHandler;
import com.avaki.cuBot.exceptions.RegistrationException;
import com.avaki.cuBot.models.Apartment;
import com.avaki.cuBot.models.User;
import com.avaki.cuBot.services.ApartmentService;
import com.avaki.cuBot.services.UserService;
import com.avaki.cuBot.services.notification.NotificationService;
import com.avaki.cuBot.utils.RegisteringUserIdFinder;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class RegistrationCallbackHandler extends AbstractCallbackHandler {

    private final long apartmentId;

    private final UserService userService;

    private final RegisteringUserIdFinder registeringUserIdFinder;

    private final ApartmentService apartmentService;

    private final CuSendingStarter cuSendingStarter;

    private final String callbackData;

    private final NotificationService notificationService;

    public RegistrationCallbackHandler(long apartmentId,
                                       UserService userService,
                                       RegisteringUserIdFinder registeringUserIdFinder,
                                       ApartmentService apartmentService,
                                       CuSendingStarter cuSendingStarter,
                                       String callbackData,
                                       NotificationService notificationService,
                                       TelegramBot bot) {
        this.apartmentId = apartmentId;
        this.userService = userService;
        this.registeringUserIdFinder = registeringUserIdFinder;
        this.apartmentService = apartmentService;
        this.cuSendingStarter = cuSendingStarter;
        this.callbackData = callbackData;
        this.notificationService = notificationService;
        initBot(bot);
    }

    public String getCallBack() {
        return callbackData;
    }

    @Override
    public synchronized void handle(Update update) {
        String receivedMessageText = getMessageText(update);
        long adminChatId = getChatId(update);

        try {
            long registeringUserId = registeringUserIdFinder.find(receivedMessageText);
            User registeringUser = bot.getRegisteringUserMap().remove(registeringUserId);
            if (registeringUser == null) {
                return;
            }

            byte apartmentId = (byte) this.apartmentId;
            Apartment choosenApartment = apartmentService.findById(apartmentId);
            registeringUser.setApartment(choosenApartment);
            userService.save(registeringUser);
            notificationService.addScheduledTasks(registeringUser);
            bot.sendMessage(adminChatId, "Пользователь успешно зарегестрирован");
            bot.sendMessage(registeringUserId, "Ваша заявка принята");
            cuSendingStarter.handle(registeringUserId, bot);
        } catch (RegistrationException exception) {
            log.error("Error during handling the registration call back", exception);
            bot.sendMessage(adminChatId, exception.getMessage());
        }
    }
}
