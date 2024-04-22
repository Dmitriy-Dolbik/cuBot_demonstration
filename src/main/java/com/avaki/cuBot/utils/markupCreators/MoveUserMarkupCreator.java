package com.avaki.cuBot.utils.markupCreators;

import com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.utils.MoveUserCallbackCreator;
import com.avaki.cuBot.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class MoveUserMarkupCreator {

    @Autowired
    private MoveUserCallbackCreator moveUserCallbackCreator;

    public InlineKeyboardMarkup create(List<User> usersToBeAbleMove) {
        List<List<InlineKeyboardButton>> userRowsToMove = new ArrayList<>();
        usersToBeAbleMove.forEach(user -> userRowsToMove.add(getUserKeyboardRow(user)));

        return InlineKeyboardMarkup.builder()
                .keyboard(userRowsToMove)
                .build();
    }

    private List<InlineKeyboardButton> getUserKeyboardRow(User user) {
        return Collections.singletonList(InlineKeyboardButton.builder()
                .text(user.getUserName() + " " + "Студия: " + user.getApartment().getNumber())
                .callbackData(moveUserCallbackCreator.create(user.getChatId()))
                .build());
    }
}
