package com.avaki.cuBot.validators;

import com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers.AbstractCommandHandler;
import com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers.adminCommandHandlers.AbstractAdminCommandHandler;
import com.avaki.cuBot.exceptions.RegistrationException;
import com.avaki.cuBot.models.User;
import com.avaki.cuBot.models.enums.CommandConstants;
import com.avaki.cuBot.models.enums.Status;
import com.avaki.cuBot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RegistrationValidator {

    @Autowired
    private UserRepository userRepository;

    @Value("${admin_id}")
    private long adminId;

    public void validate(Message message, List<AbstractCommandHandler> commandHandlerList) {
        long chatId = message.getChatId();
        String messageText = message.getText();

        if (isStartOrRegisterCommand(messageText)) {
            return;
        }

        if (chatId == adminId && isAdminCommand(messageText, commandHandlerList)) {
            return;
        }

        User writingUser = userRepository.findById(chatId).orElseThrow(() -> getRegistrationException(chatId, messageText));

        Status userStatus = writingUser.getStatus();
        if (userStatus == null || Status.MOVED.equals(writingUser.getStatus())) {
            throw getRegistrationException(chatId, messageText);
        }
    }

    private boolean isAdminCommand(String messageText, List<AbstractCommandHandler> commandHandlerList) {
        return commandHandlerList.stream()
                .filter(handler -> handler instanceof AbstractAdminCommandHandler)
                .map(AbstractCommandHandler::getCommandName)
                .collect(Collectors.toSet())
                .contains(messageText);
    }

    private RegistrationException getRegistrationException(long chatId, String messageText) {
        return new RegistrationException(String.format("The user %s isn't registered. Message of user: %s", chatId, messageText));
    }

    private boolean isStartOrRegisterCommand(String messageText) {
        return CommandConstants.START.getValue().equals(messageText) || CommandConstants.REGISTER.getValue().equals(messageText);
    }

    void setAdminId(long adminId) {
        this.adminId = adminId;
    }
}
