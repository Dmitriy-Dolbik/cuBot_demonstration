package com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers;

import com.avaki.cuBot.exceptions.NotFoundException;
import com.avaki.cuBot.models.User;
import com.avaki.cuBot.models.WiFiCredentials;
import com.avaki.cuBot.models.enums.CommandConstants;
import com.avaki.cuBot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class WiFiCommandHandler extends AbstractCommandHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public String getCommandName() {
        return CommandConstants.WIFI.getValue();
    }

    @Override
    public String getCommandDescription() {
        return "Узнать логин и пароль от wifi";
    }

    @Override
    public void handle(Message receivedMessage) {
        long chatId = receivedMessage.getChatId();
        User currentUser = userRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User with id %s cannot be found in DB", chatId)));
        WiFiCredentials wiFiCredentialsOfCurrentUser = currentUser.getApartment().getWiFiCredentials();
        String textToSend = createTextToSend(wiFiCredentialsOfCurrentUser);
        bot.sendMessage(receivedMessage.getChatId(), textToSend);
    }

    private String createTextToSend(WiFiCredentials wiFiCredentialsOfCurrentUser) {
        return  "WiFi"
                + "\n"
                + "login: " + wiFiCredentialsOfCurrentUser.getLogin()
                + "\n"
                + "password: " + wiFiCredentialsOfCurrentUser.getPassword();
    }
}
