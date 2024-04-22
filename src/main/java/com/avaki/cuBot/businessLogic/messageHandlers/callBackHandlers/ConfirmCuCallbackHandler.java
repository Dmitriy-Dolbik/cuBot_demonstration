package com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers;

import com.avaki.cuBot.businessLogic.CuPaymentCalculator;
import com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.utils.ReadingsContainer;
import com.avaki.cuBot.constants.CallbackConstants;
import com.avaki.cuBot.constants.ErrorTextConstants;
import com.avaki.cuBot.exceptions.NotFoundException;
import com.avaki.cuBot.models.Reading;
import com.avaki.cuBot.models.User;
import com.avaki.cuBot.services.ReadingsService;
import com.avaki.cuBot.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
@Slf4j
public class ConfirmCuCallbackHandler extends CuSendingCallbackHandler {

    @Value("${number_to_pay}")
    private String numberToPay;

    @Value("${name_to_pay}")
    private String nameToPay;

    @Autowired
    private ReadingsContainer readingsContainer;

    @Autowired
    private ReadingsService readingsService;

    @Autowired
    private CuPaymentCalculator cuPaymentCalculator;

    @Autowired
    private UserService userService;

    @Override
    public String getCallBack() {
        return CallbackConstants.CONFIRM_CU;
    }

    @Override
    protected void handleCallback(long chatId) {
        Set<Pair<Reading, BigDecimal>> newAndPreviousColdWaterReadingsSet = readingsContainer.remove(chatId);
        saveReadingsInDb(newAndPreviousColdWaterReadingsSet);
        bot.sendMessage(chatId, "Показания успешно сохранены");
        User currentUser = userService.findById(chatId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorTextConstants.NOT_FOUND_EXCEPTION, "User", chatId)));
        if (currentUser.isNeedToShowCuPayment()) {
            BigDecimal tenantPayment = cuPaymentCalculator.calculate(newAndPreviousColdWaterReadingsSet);

            bot.sendMessage(chatId, String.format("Стоимость КУ: %s" +
                    "\nОплата производится на Тинькофф по номеру телефона %s" +
                    "\n%s." +
                    "\nСпасибо за своевременную оплату." +
                    "\nХорошего дня!", tenantPayment, numberToPay, nameToPay));

            bot.sendMessage(bot.getAdminId(),
                    String.format("Пользователю: %s" +
                                    "\nСтудия: %s" +
                                    "\nОтправлен расчет КУ: %s",
                            currentUser.getUserName(),
                            currentUser.getApartment().getNumber(),
                            tenantPayment));
        } else {
            currentUser.setNeedToShowCuPayment(true);
            userService.save(currentUser);
        }
        bot.closeCuSendingMode(chatId);
    }

    private void saveReadingsInDb(Set<Pair<Reading, BigDecimal>> newAndPreviousColdWaterReadingsSet) {
        newAndPreviousColdWaterReadingsSet.stream()
                .map(Pair::getFirst)
                .forEach(newReading -> readingsService.save(newReading));
    }
}
