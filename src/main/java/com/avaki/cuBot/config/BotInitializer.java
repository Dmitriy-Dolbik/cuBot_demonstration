package com.avaki.cuBot.config;

import com.avaki.cuBot.businessLogic.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@RequiredArgsConstructor
@Slf4j
public class BotInitializer {
    private final TelegramBot telegramBot;

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            createCommandsMenu();
            registerBot();
        } catch (TelegramApiException e) {
            log.error("Error when trying to init a bot", e);
        }
    }

    private void createCommandsMenu() {
        telegramBot.setCommandsMenu();
    }

    private void registerBot() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(telegramBot);
    }
}
