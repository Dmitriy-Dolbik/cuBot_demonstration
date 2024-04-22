package com.avaki.cuBot.businessLogic;

import com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.AbstractCallbackHandler;
import com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers.AbstractCommandHandler;
import com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers.adminCommandHandlers.AbstractAdminCommandHandler;
import com.avaki.cuBot.businessLogic.messageHandlers.cuSendingModeHandlers.AbstractModeHandler;
import com.avaki.cuBot.constants.Constants;
import com.avaki.cuBot.exceptions.RegistrationException;
import com.avaki.cuBot.models.User;
import com.avaki.cuBot.models.enums.BotMode;
import com.avaki.cuBot.models.enums.CommandConstants;
import com.avaki.cuBot.utils.logs.CallbackQueryLogger;
import com.avaki.cuBot.utils.logs.ReceivedTextMessageLogger;
import com.avaki.cuBot.utils.logs.SendMessageLogger;
import com.avaki.cuBot.utils.markupCreators.AdminReplyKeyboardMarkupCreator;
import com.avaki.cuBot.validators.RegistrationValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private List<AbstractCommandHandler> commandHandlerList;
    private Map<String, AbstractCommandHandler> commandHandlerMap;

    @Autowired
    private List<AbstractCallbackHandler> callBackHandlerList;
    private Map<String, AbstractCallbackHandler> callBackHandlersMap;

    @Autowired
    private List<AbstractModeHandler> cuSendingModeHandlerList;
    private Map<BotMode, AbstractModeHandler> cuSendingModeHandlerMap;

    @Autowired
    private RegistrationValidator registrationValidator;

    @Autowired
    private AdminReplyKeyboardMarkupCreator adminReplyKeyboardMarkupCreator;

    @Autowired
    private ReceivedTextMessageLogger receivedTextMessageLogger;

    @Autowired
    private SendMessageLogger sendMessageLogger;

    @Autowired
    private CallbackQueryLogger callbackQueryLogger;

    private final Map<Long, BotMode> botModesMap = new ConcurrentHashMap<>();

    private final Map<Long, User> registeringUserMap = new ConcurrentHashMap<>();

    @Value("${bot_name}")
    private String botName;

    @Value("${admin_id}")
    private long adminId;

    public TelegramBot(@Value("${bot_token}") String botToken) {
        super(botToken);
    }

    @PostConstruct
    private void init() {
        commandHandlerMap = commandHandlerList.stream()
                .peek(handler -> handler.initBot(this))
                .collect(Collectors.toMap(AbstractCommandHandler::getCommandName, Function.identity()));

        callBackHandlersMap = callBackHandlerList.stream()
                .peek(holder -> holder.initBot(this))
                .collect(Collectors.toMap(AbstractCallbackHandler::getCallBack, Function.identity()));

        cuSendingModeHandlerMap = cuSendingModeHandlerList.stream()
                .peek(handler -> handler.initBot(this))
                .collect(Collectors.toMap(AbstractModeHandler::getMode, Function.identity()));

        addAdminReplyKeyboardMarkup();
    }

    private void addAdminReplyKeyboardMarkup() {
        SendMessage messageToSend = SendMessage.builder()
                .text("Бот запущен. AdminReplyKeyboardMarkup добавлен")
                .chatId(adminId)
                .build();
        messageToSend.setReplyMarkup(adminReplyKeyboardMarkupCreator.create(commandHandlerList.stream()
                .filter(handler -> handler instanceof AbstractAdminCommandHandler)
                .map(AbstractCommandHandler::getCommandName)));
        sendMessage(messageToSend);
    }

    public void setCommandsMenu() {
        List<BotCommand> comandsList = getBotCommandList();
        try {
            this.execute(new SetMyCommands(comandsList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error when trying to add a bot commands menu", e);
        }
    }

    private List<BotCommand> getBotCommandList() {
        return getCommandHandlerListUsersAbleToSee().stream()
                .map(commandHandler -> new BotCommand(commandHandler.getCommandName(), commandHandler.getCommandDescription()))
                .collect(Collectors.toList());
    }

    public List<AbstractCommandHandler> getCommandHandlerListUsersAbleToSee() {
        return commandHandlerList.stream()
                .filter(handler ->
                        !handler.getCommandName().equals(CommandConstants.START.getValue())
                                && !(handler instanceof AbstractAdminCommandHandler))
                .collect(Collectors.toList());
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message receivedMessage = update.getMessage();
        if (isTextMessage(receivedMessage)) {
            handleTextMessage(receivedMessage);
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
        }
    }

    private boolean isTextMessage(Message receivedMessage) {
        return receivedMessage != null && receivedMessage.hasText();
    }

    private void handleTextMessage(Message receivedMessage) {
        receivedTextMessageLogger.log(receivedMessage);
        long chatId = receivedMessage.getChatId();
        if (isNormalBotMode(chatId)) {
            handleNormalScenario(receivedMessage, chatId);
        } else {
            handleModeScenario(receivedMessage, chatId);
        }
    }

    private synchronized void handleNormalScenario(Message receivedMessage, long chatId) {
        String command = receivedMessage.getText();
        try {
            registrationValidator.validate(receivedMessage, commandHandlerList);
            AbstractCommandHandler commandHandler = commandHandlerMap.get(command);
            if (commandHandler != null) {
                commandHandler.handle(receivedMessage);
                return;
            }

            sendMessage(chatId, "Sorry, this command is not recognized");
        } catch (RegistrationException exception) {
            log.error("Error during handle user command", exception);
            sendMessage(chatId, "Вы не зарегестрированы в системе." +
                    "\nНажмите /register, чтобы зарегестрироваться.");
        } catch (Exception exception) {
            log.error("Error during handle user command", exception);
            sendMessage(chatId, "Произошла ошибка на сервере.");
        }
    }

    private synchronized void handleModeScenario(Message receivedMessage, long chatId) {
        try {
            BotMode botModeOfCurrentUser = botModesMap.get(chatId);
            AbstractModeHandler modeHandler = cuSendingModeHandlerMap.get(botModeOfCurrentUser);
            if (modeHandler != null) {
                modeHandler.handle(receivedMessage);
            }
        } catch (Exception e) {
            log.error("The error while trying handle mode scenario", e);
            closeCuSendingMode(chatId);
            sendMessage(chatId, "Произошла ошибка на сервере." +
                    "\n" + Constants.EXIT_INPUT_READINGS_MODE);
        }
    }

    public void closeCuSendingMode(long chatId) {
        botModesMap.remove(chatId);
    }

    private synchronized void handleCallbackQuery(Update update) {
        callbackQueryLogger.log(update);
        CallbackQuery callbackQuery = update.getCallbackQuery();
        try {
            AbstractCallbackHandler callbackHandler = callBackHandlersMap.get(callbackQuery.getData());
            if (callbackHandler != null) {
                callbackHandler.handle(update);
            }
        } catch (Exception e) {
            log.error("The error while trying handle callback query", e);
            sendMessage(callbackQuery.getMessage().getChatId(), "Произошла ошибка на сервере.");
        }
    }

    private boolean isNormalBotMode(long chatId) {
        return botModesMap.get(chatId) == null;
    }


    public void sendMessage(@NotNull long chatId, String textToSend) {
        SendMessage messageToSend = SendMessage.builder()
                .chatId(chatId)
                .text(textToSend)
                .build();
        sendMessage(messageToSend);
    }

    public void sendMessage(@NotNull SendMessage sendMessage) {
        try {
            sendMessageLogger.log(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error during send message. ChatId: {}, textToSend: {}", sendMessage.getChatId(), sendMessage.getText(), e);
        }
    }

    public void sendMessage(long chatId, String textToSend, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage messageToSend = SendMessage.builder()
                .chatId(chatId)
                .text(textToSend)
                .build();
        messageToSend.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage(messageToSend);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    public Map<Long, BotMode> getBotModesMap() {
        return botModesMap;
    }

    public Map<Long, User> getRegisteringUserMap() {
        return registeringUserMap;
    }

    public List<AbstractCommandHandler> getCommandHandlerList() {
        return commandHandlerList;
    }

    public Map<String, AbstractCallbackHandler> getCallBackHandlersMap() {
        return callBackHandlersMap;
    }

    public long getAdminId() {
        return adminId;
    }
}
