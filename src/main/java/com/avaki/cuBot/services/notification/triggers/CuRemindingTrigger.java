package com.avaki.cuBot.services.notification.triggers;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class CuRemindingTrigger implements Trigger {
    private final LocalDateTime registeredUserDate;

    public CuRemindingTrigger(LocalDateTime registeredUserDate) {
        this.registeredUserDate = registeredUserDate;
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        LocalDateTime now = getLocalDateTimeNow();
        LocalDateTime targetDateTime = LocalDateTime.of(now.getYear(), now.getMonthValue(),
                        registeredUserDate.getDayOfMonth(), 10, 0)
                .minusDays(1);
        while (!now.isBefore(targetDateTime)) {
            targetDateTime = targetDateTime.plusMonths(1);
        }
        return Date.from(targetDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    LocalDateTime getLocalDateTimeNow() {
        return LocalDateTime.now();
    }
}
