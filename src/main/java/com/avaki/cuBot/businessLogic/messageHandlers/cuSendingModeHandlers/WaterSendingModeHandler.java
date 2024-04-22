package com.avaki.cuBot.businessLogic.messageHandlers.cuSendingModeHandlers;

import com.avaki.cuBot.constants.Constants;
import com.avaki.cuBot.models.Reading;
import com.avaki.cuBot.models.enums.ReadingType;
import com.avaki.cuBot.utils.WaterReadingSendingTextCreator;
import com.avaki.cuBot.utils.markupCreators.MarkupCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Slf4j
public abstract class WaterSendingModeHandler extends AbstractModeHandler {
    private static final String CU_WATER_PATTERN = "^\\d{1,5}[.]\\d{3}$";

    @Autowired
    private WaterReadingSendingTextCreator waterReadingSendingTextCreator;

    @Override
    protected String getVerificationPattern() {
        return CU_WATER_PATTERN;
    }

    @Override
    protected String getVerificationPatternDescription() {
        return Constants.WATER_READINGS_FORMAT;
    }

    @Override
    protected String createTextToSend(BigDecimal receivedReadingsValue, long chatId) {
        Reading previousReadingOfNextInputReadings = readingsService.findLastReadingByChatIdAndReadingType(chatId, getReadingTypeOfNextInputReadings());
        return getReadingAcceptedText(getNameOfReadings(), receivedReadingsValue)
                + "\n"
                + waterReadingSendingTextCreator.create(getNameOfNextReadingToInput(), getReadingsFormatOfNextReading(), previousReadingOfNextInputReadings);
    }

    private String getReadingAcceptedText(String typeOfReadings, BigDecimal readingsValue) {
        return String.format(Constants.READINGS_ARE_ACCEPTED, typeOfReadings, readingsValue);
    }

    @Override
    protected MarkupCreator getMarkUpCreator() {
        return closeCuSendingModeMarkupCreator;
    }

    protected abstract ReadingType getReadingTypeOfNextInputReadings();

    protected abstract String getNameOfNextReadingToInput();

    protected abstract String getReadingsFormatOfNextReading();
}
