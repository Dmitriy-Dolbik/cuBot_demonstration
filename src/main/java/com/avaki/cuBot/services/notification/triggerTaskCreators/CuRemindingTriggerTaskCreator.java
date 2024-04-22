package com.avaki.cuBot.services.notification.triggerTaskCreators;

import com.avaki.cuBot.businessLogic.TelegramBot;
import com.avaki.cuBot.models.User;
import com.avaki.cuBot.services.notification.tasks.CuRemindingTask;
import com.avaki.cuBot.services.notification.triggers.CuRemindingTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.stereotype.Component;

@Component
public class CuRemindingTriggerTaskCreator implements TriggerTaskCreator{
    private final TelegramBot bot;

    @Autowired
    public CuRemindingTriggerTaskCreator(@Lazy TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public TriggerTask create(User user) {
        return new TriggerTask(new CuRemindingTask(user, bot), new CuRemindingTrigger(user.getRegisteredDate()));
    }
}
