package com.avaki.cuBot.utils.markupCreators;

import com.avaki.cuBot.constants.CallbackConstants;
import com.avaki.cuBot.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;
import java.util.List;

@Component
public class ConfirmOrChangeReadingsMarkupCreator implements MarkupCreator {

    @Autowired
    private CloseCuSendingModeMarkupCreator closeCuSendingModeMarkupCreator;

    @Override
    public InlineKeyboardMarkup create() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(getConfirmReadingsRow())
                .keyboardRow(getChangeReadingsRow())
                .keyboardRow(closeCuSendingModeMarkupCreator.getCloseCuSendingModeRow())
                .build();
    }

    private List<InlineKeyboardButton> getConfirmReadingsRow() {
        return Collections.singletonList(InlineKeyboardButton.builder()
                .text(Constants.SEND_READINGS)
                .callbackData(CallbackConstants.CONFIRM_CU)
                .build());
    }

    private List<InlineKeyboardButton> getChangeReadingsRow() {
        return Collections.singletonList(InlineKeyboardButton.builder()
                .text(Constants.CHANGE_READINGS)
                .callbackData(CallbackConstants.CHANGE_CU)
                .build());
    }
}
