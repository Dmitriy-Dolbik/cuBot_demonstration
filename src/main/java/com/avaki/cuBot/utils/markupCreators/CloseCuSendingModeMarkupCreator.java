package com.avaki.cuBot.utils.markupCreators;

import com.avaki.cuBot.constants.CallbackConstants;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;
import java.util.List;

@Component
public class CloseCuSendingModeMarkupCreator implements MarkupCreator {
    @Override
    public InlineKeyboardMarkup create() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(getCloseCuSendingModeRow())
                .build();
    }

    public List<InlineKeyboardButton> getCloseCuSendingModeRow() {
        return Collections.singletonList(InlineKeyboardButton.builder()
                .text("Выйти из режима ввода показаний")
                .callbackData(CallbackConstants.CLOSE_CU_SENDING_MODE)
                .build());
    }
}
