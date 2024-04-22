package com.avaki.cuBot.services.notification.triggers;

import com.avaki.cuBot.models.Apartment;
import com.avaki.cuBot.services.ApartmentService;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class CleaningRemindingTrigger implements Trigger {

    private final ApartmentService apartmentService;
    private static final int PERIOD_OF_CLEANING = 28;
    private final Apartment apartment;

    public CleaningRemindingTrigger(Apartment apartment, ApartmentService apartmentService) {
        this.apartment = apartment;
        this.apartmentService = apartmentService;
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        LocalDateTime now = getLocalDateTimeNow();
        LocalDateTime cleaningDate = apartment.getCleaningDate();

        LocalDateTime targetDateTime = LocalDateTime.of(cleaningDate.getYear(), cleaningDate.getMonthValue(),
                cleaningDate.getDayOfMonth(), 10, 0);

        long differanceBetweenTwoDates = ChronoUnit.DAYS.between(targetDateTime, now);
        long numberOfNeededPeriodsToPlus = (int) Math.ceil(differanceBetweenTwoDates / (double) PERIOD_OF_CLEANING);
        targetDateTime = targetDateTime.plusDays(numberOfNeededPeriodsToPlus * PERIOD_OF_CLEANING);
        while (!now.isBefore(targetDateTime)) {
            targetDateTime = targetDateTime.plusDays(PERIOD_OF_CLEANING);
        }
        apartment.setCleaningDate(targetDateTime);
        apartmentService.save(apartment);
        return Date.from(targetDateTime.atZone(ZoneId.systemDefault()).toInstant());
//            return Date.from(now.plusSeconds(5).atZone(ZoneId.systemDefault()).toInstant());
    }

    LocalDateTime getLocalDateTimeNow() {
        return LocalDateTime.now();
    }
}
