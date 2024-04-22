package com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers;

import com.avaki.cuBot.businessLogic.messageHandlers.CuSendingStarter;
import com.avaki.cuBot.constants.CallbackConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChangeCuCallbackHandler extends CuSendingCallbackHandler {
    @Autowired
    private CuSendingStarter cuSendingStarter;

    @Override
    public String getCallBack() {
        return CallbackConstants.CHANGE_CU;
    }

    @Override
    protected void handleCallback(long chatId) {
        cuSendingStarter.handle(chatId, bot);
    }
}
