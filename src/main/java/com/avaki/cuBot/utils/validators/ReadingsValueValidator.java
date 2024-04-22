package com.avaki.cuBot.utils.validators;

import com.avaki.cuBot.constants.Constants;
import com.avaki.cuBot.exceptions.ValidationReadingsException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ReadingsValueValidator {

    public void validate(BigDecimal receivedValue, BigDecimal lastValue) {
        if (receivedValue.compareTo(lastValue) <= 0) {
            String errorText =
                    String.format("Значение должно быть больше последнего переданного показания: %s", lastValue)
                            + "\n"
                            + Constants.TRY_AGAIN;
            throw new ValidationReadingsException(errorText);
        }
    }
}
