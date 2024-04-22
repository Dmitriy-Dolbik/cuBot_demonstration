package com.avaki.cuBot.businessLogic.messageHandlers.cuSendingModeHandlers;

import com.avaki.cuBot.constants.Constants;
import com.avaki.cuBot.models.enums.BotMode;
import com.avaki.cuBot.models.enums.ReadingType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HotWaterSendingModeHandler extends WaterSendingModeHandler {

    @Override
    public BotMode getMode() {
        return BotMode.HOT_WATER_SENDING;
    }

    @Override
    protected ReadingType getReadingType() {
        return ReadingType.HOT;
    }

    @Override
    protected String getNameOfReadings() {
        return Constants.HOT_WATER;
    }

    @Override
    protected ReadingType getReadingTypeOfNextInputReadings() {
        return ReadingType.ELECTRICITY;
    }

    @Override
    protected String getNameOfNextReadingToInput() {
        return Constants.ELECTRICITY;
    }

    @Override
    protected String getReadingsFormatOfNextReading() {
        return Constants.ELECTRICITY_FORMAT;
    }

    @Override
    protected void setBotMode(long chatId) {
        bot.getBotModesMap().put(chatId, BotMode.ELECTRICITY_SENDING);
    }
}
