package com.avaki.cuBot.businessLogic.messageHandlers.cuSendingModeHandlers;

import com.avaki.cuBot.constants.Constants;
import com.avaki.cuBot.models.enums.BotMode;
import com.avaki.cuBot.models.enums.ReadingType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ColdWaterSendingModeHandler extends WaterSendingModeHandler {

    @Override
    public BotMode getMode() {
        return BotMode.COLD_WATER_SENDING;
    }

    @Override
    protected ReadingType getReadingType() {
        return ReadingType.COLD;
    }

    @Override
    protected ReadingType getReadingTypeOfNextInputReadings() {
        return ReadingType.HOT;
    }

    @Override
    protected String getNameOfNextReadingToInput() {
        return Constants.HOT_WATER;
    }

    @Override
    protected String getReadingsFormatOfNextReading() {
        return Constants.WATER_READINGS_FORMAT;
    }

    @Override
    protected String getNameOfReadings() {
        return Constants.COLD_WATER;
    }

    @Override
    protected void setBotMode(long chatId) {
        bot.getBotModesMap().put(chatId, BotMode.HOT_WATER_SENDING);
    }
}
