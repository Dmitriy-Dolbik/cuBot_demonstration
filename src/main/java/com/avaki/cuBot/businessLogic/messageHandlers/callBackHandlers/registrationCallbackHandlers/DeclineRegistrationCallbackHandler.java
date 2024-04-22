package com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.registrationCallbackHandlers;

import com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.AbstractCallbackHandler;
import com.avaki.cuBot.constants.CallbackConstants;
import com.avaki.cuBot.utils.RegisteringUserIdFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DeclineRegistrationCallbackHandler extends AbstractCallbackHandler {

    @Autowired
    private RegisteringUserIdFinder registeringUserIdFinder;

    @Override
    public String getCallBack() {
        return CallbackConstants.DECLINE_REGISTRATION;
    }

    @Override
    public void handle(Update update) {
        long adminId = getChatId(update);
        long registeringUserId = registeringUserIdFinder.find(getMessageText(update));
        if (bot.getRegisteringUserMap().remove(registeringUserId) == null) {
            return;
        }
        bot.sendMessage(registeringUserId, "Ваша заявка отклонена");
        bot.sendMessage(adminId, "Заявка на регистрацию успешно отклонена");
    }
}
