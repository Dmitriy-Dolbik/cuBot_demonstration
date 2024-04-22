package com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers;

import com.avaki.cuBot.businessLogic.TelegramBot;
import com.avaki.cuBot.constants.ErrorTextConstants;
import com.avaki.cuBot.exceptions.MovingException;
import com.avaki.cuBot.models.User;
import com.avaki.cuBot.models.enums.Status;
import com.avaki.cuBot.services.UserService;
import com.avaki.cuBot.services.notification.NotificationService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MoveUserCallbackHandler extends AbstractCallbackHandler {

    private final UserService userService;

    private final long movingUserId;

    private final String callbackData;

    private final NotificationService notificationService;

    public MoveUserCallbackHandler(long movingUserId, UserService userService, String callbackData, NotificationService notificationService, TelegramBot bot) {
        this.movingUserId = movingUserId;
        this.userService = userService;
        this.callbackData = callbackData;
        this.notificationService = notificationService;
        initBot(bot);
    }

    @Override
    public String getCallBack() {
        return callbackData;
    }

    @Override
    public void handle(Update update) {
        long chatId = getChatId(update);

        User movingUser = userService.findById(movingUserId)
                .orElseThrow(() -> new MovingException(
                        String.format(ErrorTextConstants.NOT_FOUND_EXCEPTION, "User", movingUserId)));
        movingUser.setStatus(Status.MOVED);
        movingUser.setNeedToShowCuPayment(false);
        userService.save(movingUser);
        notificationService.cancelScheduledTasks(movingUser.getChatId());

        SendMessage sendMessage = SendMessage.builder()
                .text("Юзер успешно выселен")
                .chatId(chatId)
                .build();
        bot.sendMessage(sendMessage);

        bot.getCallBackHandlersMap().remove(getCallBack());
    }
}
