package com.avaki.cuBot.businessLogic.messageHandlers;

import com.avaki.cuBot.businessLogic.TelegramBot;
import com.avaki.cuBot.constants.Constants;
import com.avaki.cuBot.models.enums.BotMode;
import com.avaki.cuBot.models.enums.ReadingType;
import com.avaki.cuBot.services.ReadingsService;
import com.avaki.cuBot.utils.WaterReadingSendingTextCreator;
import com.avaki.cuBot.utils.markupCreators.CloseCuSendingModeMarkupCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class CuSendingStarter {

    @Autowired
    private WaterReadingSendingTextCreator waterReadingSendingTextCreator;

    @Autowired
    private CloseCuSendingModeMarkupCreator closeCuSendingModeMarkupCreator;

    @Autowired
    private ReadingsService readingsService;


    public void handle(long chatId, TelegramBot bot) {
        String textToSend = waterReadingSendingTextCreator.create(Constants.COLD_WATER, Constants.WATER_READINGS_FORMAT, readingsService.findLastReadingByChatIdAndReadingType(chatId, ReadingType.COLD));
        InlineKeyboardMarkup closeKuSendingModeMarkup = closeCuSendingModeMarkupCreator.create();
        bot.sendMessage(chatId, textToSend, closeKuSendingModeMarkup);
        bot.getBotModesMap().put(chatId, BotMode.COLD_WATER_SENDING);
    }
}
