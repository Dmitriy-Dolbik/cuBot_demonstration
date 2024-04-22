package com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.creators;

import com.avaki.cuBot.businessLogic.TelegramBot;
import com.avaki.cuBot.businessLogic.messageHandlers.CuSendingStarter;
import com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.registrationCallbackHandlers.RegistrationCallbackHandler;
import com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.registrationCallbackHandlers.utils.RegistrationCallbackCreator;
import com.avaki.cuBot.services.ApartmentService;
import com.avaki.cuBot.services.UserService;
import com.avaki.cuBot.services.notification.NotificationService;
import com.avaki.cuBot.utils.RegisteringUserIdFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationCallbackHandlerCreator {

    @Autowired
    private UserService userService;

    @Autowired
    private RegisteringUserIdFinder registeringUserIdFinder;

    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private CuSendingStarter cuSendingStarter;

    @Autowired
    private RegistrationCallbackCreator registrationCallbackCreator;

    @Autowired
    private NotificationService notificationService;

    public RegistrationCallbackHandler create(long apartmentId, TelegramBot bot) {
        return new RegistrationCallbackHandler(apartmentId,
                userService,
                registeringUserIdFinder,
                apartmentService,
                cuSendingStarter,
                registrationCallbackCreator.create(apartmentId),
                notificationService,
                bot);
    }
}
