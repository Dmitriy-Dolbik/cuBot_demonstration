package com.avaki.cuBot.services.notification.triggerTaskCreators;

import com.avaki.cuBot.models.User;
import org.springframework.scheduling.config.TriggerTask;

public interface TriggerTaskCreator {

    TriggerTask create(User user);
}
