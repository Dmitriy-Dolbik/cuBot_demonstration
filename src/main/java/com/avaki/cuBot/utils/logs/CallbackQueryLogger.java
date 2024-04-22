package com.avaki.cuBot.utils.logs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collection;

@Component
@Slf4j
public class CallbackQueryLogger {
    public void log(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        org.telegram.telegrambots.meta.api.objects.User writingUser = callbackQuery.getFrom();
        log.info("Получен callback:" +
                        "\nUsername: {}" +
                        "\nChatId: {}" +
                        "\nНажатие кнопки: {}" +
                        "\nCallbackDate: {}",
                writingUser.getUserName(), writingUser.getId(), getCallbackButtonText(callbackQuery), callbackQuery.getData());
    }

    private String getCallbackButtonText(CallbackQuery callbackQuery) {
        return callbackQuery.getMessage().getReplyMarkup().getKeyboard().stream()
                .flatMap(Collection::stream)
                .filter(button -> button.getCallbackData().equals(callbackQuery.getData()))
                .map(InlineKeyboardButton::getText)
                .findFirst().orElse("У кнопки текста нет");
    }
}
