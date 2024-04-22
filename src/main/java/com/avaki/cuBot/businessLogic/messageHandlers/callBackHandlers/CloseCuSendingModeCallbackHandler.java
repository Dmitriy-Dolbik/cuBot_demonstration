package com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers;

import com.avaki.cuBot.constants.CallbackConstants;
import com.avaki.cuBot.constants.Constants;
import org.springframework.stereotype.Component;

@Component
public class CloseCuSendingModeCallbackHandler extends CuSendingCallbackHandler {

    @Override
    public String getCallBack() {
        return CallbackConstants.CLOSE_CU_SENDING_MODE;
    }

    @Override
    protected void handleCallback(long chatId) {
        bot.closeCuSendingMode(chatId);
        bot.sendMessage(chatId, Constants.EXIT_INPUT_READINGS_MODE);
    }
}
