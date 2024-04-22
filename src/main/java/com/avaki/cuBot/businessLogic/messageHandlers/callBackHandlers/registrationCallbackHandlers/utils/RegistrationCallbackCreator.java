package com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.registrationCallbackHandlers.utils;

import com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.registrationCallbackHandlers.RegistrationCallbackHandler;
import org.springframework.stereotype.Component;

@Component
public class RegistrationCallbackCreator {

    public String create(long apartmentId) {
        return apartmentId + "_" + RegistrationCallbackHandler.class.getSimpleName();
    }
}