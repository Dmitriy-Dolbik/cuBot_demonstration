package com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers;

import com.avaki.cuBot.businessLogic.messageHandlers.CuSendingStarter;
import com.avaki.cuBot.models.enums.CommandConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class CuCommandHandler extends AbstractCommandHandler {

    @Autowired
    private CuSendingStarter cuSendingStarter;

    @Override
    public String getCommandName() {
        return CommandConstants.CU.getValue();
    }

    @Override
    public String getCommandDescription() {
        return "Отправить показания счетчиков";
    }

    @Override
    public void handle(Message receivedMessage) {
        long chatId = receivedMessage.getChatId();
        cuSendingStarter.handle(chatId, bot);
    }
}