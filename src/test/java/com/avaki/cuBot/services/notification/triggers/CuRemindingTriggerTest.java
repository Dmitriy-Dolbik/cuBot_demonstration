package com.avaki.cuBot.services.notification.triggers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TriggerContext;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class CuRemindingTriggerTest {

    @Mock
    private TriggerContext triggerContext;

    private CuRemindingTrigger cuRemindingTrigger;

    @Test
    public void cuRemindingTrigger_nextExecutionTimeTest_forNewUserWithCreatingTaskOnMidnight_shouldRemindInNextMonth() {
        //Given
        LocalDateTime registeredUserDate = LocalDateTime.of(2024, Month.JANUARY, 15, 8, 0);
        cuRemindingTrigger = spy(new CuRemindingTrigger(registeredUserDate));


        LocalDateTime mockNow = LocalDateTime.of(2024, Month.JANUARY, 16, 0, 0);
        Mockito.doReturn(mockNow).when(cuRemindingTrigger).getLocalDateTimeNow();

        Date expectedDate = Date.from(
                LocalDateTime.of(2024, Month.FEBRUARY, 14, 10, 0)
                        .atZone(ZoneId.systemDefault()).toInstant());
        //When
        Date actualDate = cuRemindingTrigger.nextExecutionTime(triggerContext);

        //Then
        Assertions.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void cuRemindingTrigger_nextExecutionTimeTest_forNewUserWithCreatingTaskOnBefore10am_shouldRemindInNextMonth() {
        //Given
        LocalDateTime registeredUserDate = LocalDateTime.of(2024, Month.JANUARY, 15, 8, 0);
        cuRemindingTrigger = spy(new CuRemindingTrigger(registeredUserDate));


        LocalDateTime mockNow = LocalDateTime.of(2024, Month.JANUARY, 15, 9, 0);
        Mockito.doReturn(mockNow).when(cuRemindingTrigger).getLocalDateTimeNow();

        Date expectedDate = Date.from(
                LocalDateTime.of(2024, Month.FEBRUARY, 14, 10, 0)
                        .atZone(ZoneId.systemDefault()).toInstant());
        //When
        Date actualDate = cuRemindingTrigger.nextExecutionTime(triggerContext);

        //Then
        Assertions.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void cuRemindingTrigger_nextExecutionTimeTest_forNewUserWithCreatingTaskOnAfter10am_shouldRemindInNextMonth() {
        //Given
        LocalDateTime registeredUserDate = LocalDateTime.of(2024, Month.JANUARY, 15, 8, 0);
        cuRemindingTrigger = spy(new CuRemindingTrigger(registeredUserDate));


        LocalDateTime mockNow = LocalDateTime.of(2024, Month.JANUARY, 15, 11, 0);
        Mockito.doReturn(mockNow).when(cuRemindingTrigger).getLocalDateTimeNow();

        Date expectedDate = Date.from(
                LocalDateTime.of(2024, Month.FEBRUARY, 14, 10, 0)
                        .atZone(ZoneId.systemDefault()).toInstant());
        //When
        Date actualDate = cuRemindingTrigger.nextExecutionTime(triggerContext);

        //Then
        Assertions.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void cuRemindingTrigger_nextExecutionTimeTest_forOldUserWithCreatingTaskBeforeRemindingTime10am_shouldRemindInThisMonth() {
        //Given
        LocalDateTime registeredUserDate = LocalDateTime.of(2024, Month.JANUARY, 15, 8, 0);
        cuRemindingTrigger = spy(new CuRemindingTrigger(registeredUserDate));


        LocalDateTime mockNow = LocalDateTime.of(2024, Month.MARCH, 14, 9, 0);
        Mockito.doReturn(mockNow).when(cuRemindingTrigger).getLocalDateTimeNow();

        Date expectedDate = Date.from(
                LocalDateTime.of(2024, Month.MARCH, 14, 10, 0)
                        .atZone(ZoneId.systemDefault()).toInstant());
        //When
        Date actualDate = cuRemindingTrigger.nextExecutionTime(triggerContext);

        //Then
        Assertions.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void cuRemindingTrigger_nextExecutionTimeTest_forOldUserWithCreatingTaskAfterRemindingTime10am_shouldRemindInNextMonth() {
        //Given
        LocalDateTime registeredUserDate = LocalDateTime.of(2024, Month.JANUARY, 15, 8, 0);
        cuRemindingTrigger = spy(new CuRemindingTrigger(registeredUserDate));


        LocalDateTime mockNow = LocalDateTime.of(2024, Month.MARCH, 14, 11, 0);
        Mockito.doReturn(mockNow).when(cuRemindingTrigger).getLocalDateTimeNow();

        Date expectedDate = Date.from(
                LocalDateTime.of(2024, Month.APRIL, 14, 10, 0)
                        .atZone(ZoneId.systemDefault()).toInstant());
        //When
        Date actualDate = cuRemindingTrigger.nextExecutionTime(triggerContext);

        //Then
        Assertions.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void cuRemindingTrigger_nextExecutionTimeTest_registeredDateIsFirstDayOfMonth_shouldRemindInNextMonth() {
        //Given
        LocalDateTime registeredUserDate = LocalDateTime.of(2024, Month.FEBRUARY, 1, 20, 37);
        cuRemindingTrigger = spy(new CuRemindingTrigger(registeredUserDate));


        LocalDateTime mockNow = LocalDateTime.of(2024, Month.FEBRUARY, 29, 10, 0);
        Mockito.doReturn(mockNow).when(cuRemindingTrigger).getLocalDateTimeNow();

        Date expectedDate = Date.from(
                LocalDateTime.of(2024, Month.MARCH, 29, 10, 0)
                        .atZone(ZoneId.systemDefault()).toInstant());
        //When
        Date actualDate = cuRemindingTrigger.nextExecutionTime(triggerContext);

        //Then
        Assertions.assertEquals(expectedDate, actualDate);
    }
}