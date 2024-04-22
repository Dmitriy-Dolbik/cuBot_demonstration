package com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers.adminCommandHandlers;

import com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.MoveUserCallbackHandler;
import com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers.adminCommandHandlers.creators.MoveUserCallbackHandlerCreator;
import com.avaki.cuBot.models.User;
import com.avaki.cuBot.models.enums.AdminCommandConstants;
import com.avaki.cuBot.models.enums.Status;
import com.avaki.cuBot.services.UserService;
import com.avaki.cuBot.utils.markupCreators.MoveUserMarkupCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MoveUserCommandHandler extends AbstractAdminCommandHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private MoveUserCallbackHandlerCreator moveUserCallbackHandlerCreator;

    @Autowired
    private MoveUserMarkupCreator moveUserMarkupCreator;

    @Override
    public String getCommandName() {
        return AdminCommandConstants.MOVE_USER.getValue();
    }

    @Override
    protected void handleAdminCommand(Message receivedMessage) {
        long chatId = receivedMessage.getChatId();

        List<User> usersToBeAbleMove = userService.findAll().stream()
                .filter(user -> !Status.MOVED.equals(user.getStatus()))
                .collect(Collectors.toList());

        if (usersToBeAbleMove.isEmpty()) {
            bot.sendMessage(SendMessage.builder()
                    .text("Нет ни одного юзера доступного к выселению")
                    .chatId(chatId)
                    .build());
            return;
        }

        SendMessage messageToSend = SendMessage.builder()
                .text("Выберите юзера для выселения")
                .chatId(chatId)
                .replyMarkup(moveUserMarkupCreator.create(usersToBeAbleMove))
                .build();

        usersToBeAbleMove.forEach(user -> {
            MoveUserCallbackHandler moveUserCallbackHandler = moveUserCallbackHandlerCreator.create(user.getChatId(), bot);
            bot.getCallBackHandlersMap().put(moveUserCallbackHandler.getCallBack(), moveUserCallbackHandler);
        });

        bot.sendMessage(messageToSend);
    }
}
