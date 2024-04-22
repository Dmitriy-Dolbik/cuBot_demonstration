package com.avaki.cuBot.utils.logs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SendMessageLogger {
    public void log(SendMessage sendMessage) {
        String markupString = "";

        ReplyKeyboard replyKeyboard = sendMessage.getReplyMarkup();
        if (replyKeyboard instanceof InlineKeyboardMarkup) {
            InlineKeyboardMarkup inlineKeyboardMarkup = (InlineKeyboardMarkup) replyKeyboard;
            markupString = "\nMarkUp:" + inlineKeyboardMarkup.getKeyboard().stream()
                    .flatMap(Collection::stream)
                    .map(button -> "\nbuttonText: " + button.getText() + " ... " + "CallbackData: " + button.getCallbackData())
                    .collect(Collectors.joining("\n"));
        }

        String logString = "Отправлено сообщение:\nchatId: {}\ntext: {}{}";
        log.info(logString, sendMessage.getChatId(), sendMessage.getText(), markupString);
    }
}
