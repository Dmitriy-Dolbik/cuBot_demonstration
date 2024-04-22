package com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers.adminCommandHandlers;

import com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers.AbstractCommandHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
public abstract class AbstractAdminCommandHandler extends AbstractCommandHandler {
    
    @Value("${admin_id}")
    private long adminId;

    @Override
    public String getCommandDescription() {
        return "";//не требуется, т.к. не отображается в меню команд для юзера
    }

    @Override
    public void handle(Message receivedMessage) {
        if (receivedMessage.getChatId() != adminId) {
            return;
        }
        handleAdminCommand(receivedMessage);
    }

    protected abstract void handleAdminCommand(Message receivedMessage);
}
