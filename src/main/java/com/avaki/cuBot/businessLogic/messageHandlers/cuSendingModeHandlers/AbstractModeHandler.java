package com.avaki.cuBot.businessLogic.messageHandlers.cuSendingModeHandlers;

import com.avaki.cuBot.businessLogic.TelegramBot;
import com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.utils.ReadingsContainer;
import com.avaki.cuBot.constants.Constants;
import com.avaki.cuBot.exceptions.ValidationReadingsException;
import com.avaki.cuBot.models.Meter;
import com.avaki.cuBot.models.Reading;
import com.avaki.cuBot.models.enums.BotMode;
import com.avaki.cuBot.models.enums.ReadingType;
import com.avaki.cuBot.services.MeterService;
import com.avaki.cuBot.services.ReadingsService;
import com.avaki.cuBot.utils.markupCreators.CloseCuSendingModeMarkupCreator;
import com.avaki.cuBot.utils.markupCreators.MarkupCreator;
import com.avaki.cuBot.utils.validators.ReadingsValueValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.math.BigDecimal;

@Slf4j
public abstract class AbstractModeHandler {

    @Autowired
    protected ReadingsContainer readingsContainer;

    @Autowired
    protected ReadingsValueValidator readingsValueValidator;

    @Autowired
    protected CloseCuSendingModeMarkupCreator closeCuSendingModeMarkupCreator;

    @Autowired
    private MeterService meterService;

    @Autowired
    protected ReadingsService readingsService;

    protected TelegramBot bot;

    public void initBot(TelegramBot bot) {
        if (this.bot != null) {
            return;
        }
        this.bot = bot;
    }

    public abstract BotMode getMode();

    public void handle(Message receivedMessage) {
        String receivedReadings = receivedMessage.getText();
        long chatId = receivedMessage.getChatId();

        try {
            ReadingType readingType = getReadingType();

            validateReadingInputFormat(receivedReadings, getVerificationPattern());
            BigDecimal receivedReadingsValue = new BigDecimal(receivedReadings);
            Reading previousReading = readingsService.findLastReadingByChatIdAndReadingType(chatId, readingType);
            readingsValueValidator.validate(receivedReadingsValue, previousReading.getValue());
            Meter meter = meterService.findByUserIdAndReadingType(chatId, readingType);
            Reading newReading = new Reading(receivedReadingsValue, meter, readingType);
            readingsContainer.put(chatId, Pair.of(newReading, previousReading.getValue()));
            String textToSend = createTextToSend(receivedReadingsValue, chatId);
            InlineKeyboardMarkup inlineKeyboardMarkup = getMarkUpCreator().create();
            bot.sendMessage(chatId, textToSend, inlineKeyboardMarkup);
            setBotMode(chatId);
        } catch (ValidationReadingsException exception) {
            log.error(getLogText(chatId), exception);
            sendMessageWithCloseCuSendingModeMarkup(chatId, exception.getMessage());
        }
    }

    protected abstract ReadingType getReadingType();

    protected void validateReadingInputFormat(String validatingReadings, String verificationPattern) {
        if (!validatingReadings.matches(verificationPattern)) {
            String errorText = createWrongInputFormatErrorText(getVerificationPatternDescription());
            throw new ValidationReadingsException(errorText);
        }
    }

    protected String createWrongInputFormatErrorText(String readingsFormat) {
        return String.format(Constants.WRONG_INPUT_READINGS_FORMAT +
                        "\n" +
                        Constants.INPUT_READINGS_IN_FORMAT +
                        "\n" +
                        Constants.TRY_AGAIN
                , readingsFormat);
    }

    protected abstract String getVerificationPattern();

    protected abstract String createTextToSend(BigDecimal receivedReadingsValue, long chatId);

    protected abstract MarkupCreator getMarkUpCreator();

    private String getLogText(long chatId) {
        return String.format("Ошибка обработки показаний %s. ChatId: %s", getNameOfReadings(), chatId);
    }

    protected abstract void setBotMode(long chatId);

    protected void sendMessageWithCloseCuSendingModeMarkup(long chatId, String textToSend) {
        InlineKeyboardMarkup closeKuSendingModeMarkup = closeCuSendingModeMarkupCreator.create();
        bot.sendMessage(chatId, textToSend, closeKuSendingModeMarkup);
    }

    protected abstract String getVerificationPatternDescription();

    protected abstract String getNameOfReadings();
}
