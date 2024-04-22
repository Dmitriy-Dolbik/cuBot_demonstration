package com.avaki.cuBot.utils;

import com.avaki.cuBot.constants.Constants;
import com.avaki.cuBot.models.Reading;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WaterReadingSendingTextCreator {

    public String create(String typeOfReadings, String readingFormat, Reading previousReadingOfNextInputReadings) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(Constants.INPUT_READINGS, typeOfReadings, previousReadingOfNextInputReadings.getMeter().getNumber()));
        stringBuilder.append("\n");
        if (previousReadingOfNextInputReadings.getValue().compareTo(BigDecimal.ZERO) == 0) {
            stringBuilder.append(String.format(Constants.INPUT_READINGS_IN_FORMAT, readingFormat));
        } else {
            stringBuilder.append(String.format(Constants.LAST_READINGS, previousReadingOfNextInputReadings.getValue()));
        }
        return stringBuilder.toString();
    }
}
