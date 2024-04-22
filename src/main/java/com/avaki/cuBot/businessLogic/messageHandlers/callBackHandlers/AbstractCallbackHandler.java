package com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers;

import com.avaki.cuBot.businessLogic.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractCallbackHandler {

    protected TelegramBot bot;

    public abstract String getCallBack();

    public abstract void handle(Update update);

    protected long getChatId(Update update) {
        return update.getCallbackQuery().getMessage().getChatId();
    }

    protected String getMessageText(Update update) {
        return update.getCallbackQuery().getMessage().getText();
    }

    public void initBot(TelegramBot bot) {
        if (this.bot != null) {
            return;
        }
        this.bot = bot;
    }

    protected boolean isInCuSendingMode(long chatId) {
        return bot.getBotModesMap().containsKey(chatId);
    }
}
