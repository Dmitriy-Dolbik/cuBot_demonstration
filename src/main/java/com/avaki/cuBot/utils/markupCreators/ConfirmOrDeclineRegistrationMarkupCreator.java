package com.avaki.cuBot.utils.markupCreators;

import com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.registrationCallbackHandlers.utils.RegistrationCallbackCreator;
import com.avaki.cuBot.constants.CallbackConstants;
import com.avaki.cuBot.models.Apartment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component("confirmOrDeclineRegistrationMarkupCreator")
public class ConfirmOrDeclineRegistrationMarkupCreator {

    @Autowired
    private RegistrationCallbackCreator registrationCallbackCreator;

    public InlineKeyboardMarkup create(List<Apartment> apartmentsToRegister) {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(getApartmentButtonsRow(apartmentsToRegister))
                .keyboardRow(getDeclineButtonRow())
                .build();
    }

    private List<InlineKeyboardButton> getApartmentButtonsRow(List<Apartment> apartmentsToRegister) {
        return apartmentsToRegister.stream()
                .map(Apartment::getNumber)
                .sorted()
                .map(apartmentId -> InlineKeyboardButton.builder()
                        .text(String.valueOf(apartmentId))
                        .callbackData(registrationCallbackCreator.create(apartmentId))
                        .build())
                .collect(Collectors.toList());
    }

    private List<InlineKeyboardButton> getDeclineButtonRow() {
        return Collections.singletonList(InlineKeyboardButton.builder()
                .text("Отклонить")
                .callbackData(CallbackConstants.DECLINE_REGISTRATION)
                .build());
    }
}
