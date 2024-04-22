package com.avaki.cuBot.services.notification.triggerTaskCreators;

import com.avaki.cuBot.businessLogic.TelegramBot;
import com.avaki.cuBot.models.User;
import com.avaki.cuBot.services.ApartmentService;
import com.avaki.cuBot.services.notification.tasks.CleaningRemindingTask;
import com.avaki.cuBot.services.notification.triggers.CleaningRemindingTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.stereotype.Component;

@Component
public class CleaningRemindingTriggerTaskCreator implements TriggerTaskCreator{

    private final TelegramBot bot;

    private final ApartmentService apartmentService;

    @Autowired
    public CleaningRemindingTriggerTaskCreator(@Lazy TelegramBot bot, ApartmentService apartmentService) {
        this.bot = bot;
        this.apartmentService = apartmentService;
    }

    @Override
    public TriggerTask create(User user) {
        return new TriggerTask(new CleaningRemindingTask(user, bot), new CleaningRemindingTrigger(user.getApartment(), apartmentService));
    }
}
