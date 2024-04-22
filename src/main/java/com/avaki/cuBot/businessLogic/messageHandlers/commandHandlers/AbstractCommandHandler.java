package com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers;

import com.avaki.cuBot.businessLogic.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
public abstract class AbstractCommandHandler {
    protected TelegramBot bot;

    public abstract String getCommandName();

    public abstract String getCommandDescription();

    public abstract void handle(Message receivedMessage);

    public void initBot(TelegramBot bot) {
        if (this.bot != null) {
            return;
        }
        this.bot = bot;
    }
}
