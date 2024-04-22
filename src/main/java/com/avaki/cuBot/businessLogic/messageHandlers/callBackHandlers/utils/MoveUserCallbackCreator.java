package com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.utils;

import com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers.adminCommandHandlers.MoveUserCommandHandler;
import org.springframework.stereotype.Component;

@Component
public class MoveUserCallbackCreator {

    public String create(long userId) {
        return userId + "_" + MoveUserCommandHandler.class.getSimpleName();
    }
}