package com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers;

import com.avaki.cuBot.models.enums.CommandConstants;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.stream.Collectors;

@Component
public class StartCommandHandler extends AbstractCommandHandler {


    @Override
    public String getCommandName() {
        return CommandConstants.START.getValue();
    }

    @Override
    public String getCommandDescription() {
        return "Welcome message";
    }

    @Override
    public void handle(Message receivedMessage) {
        String smileEmoji = ":blush:";
        String textToSend = getTextToSend(receivedMessage, smileEmoji);


        bot.sendMessage(receivedMessage.getChatId(), textToSend);
    }

    private String getTextToSend(Message receivedMessage, String smileEmoji) {
        String welcomeText = EmojiParser.parseToUnicode(
                String.format("Добро пожаловать, %s%s." +
                        "\nЗдесь вы можете: ", receivedMessage.getFrom().getFirstName(), smileEmoji));

        String commandsList = getCommandsListText();

        return welcomeText + "\n" + commandsList;
    }

    protected String getCommandsListText() {
        String listOfCommands = bot.getCommandHandlerListUsersAbleToSee().stream()
                .map(handler -> String.join(" ", handler.getCommandDescription(), handler.getCommandName()))
                .collect(Collectors.joining("\n- "));
        return "- " + listOfCommands;
    }
}
