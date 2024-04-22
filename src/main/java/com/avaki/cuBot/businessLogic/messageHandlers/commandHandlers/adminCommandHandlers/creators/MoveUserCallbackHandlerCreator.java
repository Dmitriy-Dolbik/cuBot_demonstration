package com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers.adminCommandHandlers.creators;

import com.avaki.cuBot.businessLogic.TelegramBot;
import com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.MoveUserCallbackHandler;
import com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.utils.MoveUserCallbackCreator;
import com.avaki.cuBot.services.UserService;
import com.avaki.cuBot.services.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MoveUserCallbackHandlerCreator {

    @Autowired
    private UserService userService;

    @Autowired
    private MoveUserCallbackCreator moveUserCallbackDataCreator;

    @Autowired
    private NotificationService notificationService;

    public MoveUserCallbackHandler create(long movingUserId, TelegramBot bot) {
        return new MoveUserCallbackHandler(movingUserId, userService, moveUserCallbackDataCreator.create(movingUserId), notificationService, bot);
    }
}
