package com.avaki.cuBot.utils.logs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class ReceivedTextMessageLogger {

    public void log(Message receivedMessage) {
        org.telegram.telegrambots.meta.api.objects.User writingUser = receivedMessage.getFrom();
        log.info("Получено сообщение:" +
                        "\nUsername: {}" +
                        "\nChatId: {}" +
                        "\nТекст: {}",
                writingUser.getUserName(), writingUser.getId(), receivedMessage.getText());
    }
}
