package com.avaki.cuBot.utils.markupCreators;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AdminReplyKeyboardMarkupCreator {

    public ReplyKeyboardMarkup create(Stream<String> adminCommandNames) {
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(getKeyboardRow(adminCommandNames))
                .resizeKeyboard(true)
                .selective(true)
                .build();
    }

    private KeyboardRow getKeyboardRow(Stream<String> adminCommandNames) {
        return new KeyboardRow(adminCommandNames
                .map(KeyboardButton::new)
                .collect(Collectors.toList()));
    }
}
