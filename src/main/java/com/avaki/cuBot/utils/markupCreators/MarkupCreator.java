package com.avaki.cuBot.utils.markupCreators;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface MarkupCreator {
    InlineKeyboardMarkup create();
}