package com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers;

import com.avaki.cuBot.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public abstract class CuSendingCallbackHandler extends AbstractCallbackHandler{
    @Override
    public void handle(Update update) {
        long chatId = getChatId(update);
        if (!isInCuSendingMode(chatId)) {
            return;
        }
        try {
            handleCallback(chatId);
        } catch (Exception e) {
            log.error("The error while handle method of CuSendingCallbackHandler", e);
            bot.closeCuSendingMode(chatId);
            bot.sendMessage(chatId, "Произошла ошибка на сервере." +
                    "\n" + Constants.EXIT_INPUT_READINGS_MODE);
        }
    }

    protected abstract void handleCallback(long chatId);
}
