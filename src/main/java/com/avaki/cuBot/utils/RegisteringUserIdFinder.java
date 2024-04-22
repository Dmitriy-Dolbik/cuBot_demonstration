package com.avaki.cuBot.utils;

import com.avaki.cuBot.constants.Constants;
import com.avaki.cuBot.exceptions.RegistrationException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegisteringUserIdFinder {
    private static final String CHAT_ID_REGEX = Constants.CHAT_ID.replace("%s", "(\\d+)");

    public long find(String receivedMessageText) {

        Pattern chatIdPattern = Pattern.compile(CHAT_ID_REGEX);
        Matcher chatIdMatcher = chatIdPattern.matcher(receivedMessageText);
        if (chatIdMatcher.find()) {
            return Long.parseLong(chatIdMatcher.group(1));
        } else {
            throw new RegistrationException(
                    String.format("ChatId of registering user was not found. receivedMessageText: %s", receivedMessageText));
        }
    }

}
