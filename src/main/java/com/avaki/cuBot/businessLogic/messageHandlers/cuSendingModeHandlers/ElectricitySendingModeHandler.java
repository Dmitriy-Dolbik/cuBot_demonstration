package com.avaki.cuBot.businessLogic.messageHandlers.cuSendingModeHandlers;

import com.avaki.cuBot.constants.Constants;
import com.avaki.cuBot.models.enums.BotMode;
import com.avaki.cuBot.models.enums.ReadingType;
import com.avaki.cuBot.utils.markupCreators.ConfirmOrChangeReadingsMarkupCreator;
import com.avaki.cuBot.utils.markupCreators.MarkupCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class ElectricitySendingModeHandler extends AbstractModeHandler {
    private static final String CU_ELECTRICITY_PATTERN = "^\\d{1,5}[.]\\d$";

    @Autowired
    private ConfirmOrChangeReadingsMarkupCreator confirmOrChangeReadingsMarkupCreator;

    @Override
    public BotMode getMode() {
        return BotMode.ELECTRICITY_SENDING;
    }

    @Override
    protected ReadingType getReadingType() {
        return ReadingType.ELECTRICITY;
    }

    @Override
    protected MarkupCreator getMarkUpCreator() {
        return confirmOrChangeReadingsMarkupCreator;
    }

    @Override
    protected void setBotMode(long chatId) {
        //Не трубет изменения состояния. Выход из режима ввода показаний происходит в ConfirmCuCallbackHandler
    }

    @Override
    protected String getVerificationPattern() {
        return CU_ELECTRICITY_PATTERN;
    }

    @Override
    protected String createTextToSend(BigDecimal receiveElectricityReadingsValue, long chatId) {
        BigDecimal previousColdWaterReadings = readingsContainer.getPreviousReadingValue(chatId, ReadingType.COLD);
        BigDecimal newColdWaterReadings = readingsContainer.getNewReading(chatId, ReadingType.COLD).getValue();
        BigDecimal coldWaterConsumption = newColdWaterReadings.subtract(previousColdWaterReadings);

        BigDecimal previousHotWaterReadings = readingsContainer.getPreviousReadingValue(chatId, ReadingType.HOT);
        BigDecimal newHotWaterReadings = readingsContainer.getNewReading(chatId, ReadingType.HOT).getValue();
        BigDecimal hotWaterConsumption = newHotWaterReadings.subtract(previousHotWaterReadings);

        BigDecimal previousElectricityReadings = readingsContainer.getPreviousReadingValue(chatId, ReadingType.ELECTRICITY);
        BigDecimal electricityConsumption = receiveElectricityReadingsValue.subtract(previousElectricityReadings);

        String messageToTenant = String.format("Проверьте введенные показания." +
                        "\nЕсли все верно, нажмите кнопку \"%s\"" +
                        "\n\nХолодная вода: %s " +
                        "\n(предыдущие показания %s, расход %s)" +
                        "\nГорячая вода: %s" +
                        "\n(предыдущие показания %s, расход %s)" +
                        "\nЭлектроэнергия: %s" +
                        "\n(предыдущие показания %s, расход %s)",
                Constants.SEND_READINGS,
                newColdWaterReadings,
                previousColdWaterReadings,
                coldWaterConsumption,

                newHotWaterReadings,
                previousHotWaterReadings,
                hotWaterConsumption,

                receiveElectricityReadingsValue,
                previousElectricityReadings,
                electricityConsumption);

        bot.sendMessage(bot.getAdminId(), "Юзер пытается передать показания" +
                "\n\n" +
                messageToTenant);

        return messageToTenant;
    }

    @Override
    protected String getVerificationPatternDescription() {
        return Constants.ELECTRICITY_FORMAT;
    }

    @Override
    protected String getNameOfReadings() {
        return Constants.ELECTRICITY;
    }
}
