package com.avaki.cuBot.services.notification.triggers;

import com.avaki.cuBot.models.Apartment;
import com.avaki.cuBot.services.ApartmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
class CleaningRemindingTriggerTest {

    @Mock
    private TriggerContext triggerContext;

    @Mock
    private ApartmentService apartmentService;

    @Mock
    private Apartment apartment;

    private CleaningRemindingTrigger cleaningRemindingTrigger;

    @BeforeEach
    public void beforeEachTest() {
        cleaningRemindingTrigger = spy(new CleaningRemindingTrigger(apartment, apartmentService));
    }

    @Test
    public void cleaningRemindingTrigger_nextExecutionTimeTest_nowIsBetweenOneAndTwoMonthsFromCleaningDay_shouldRemindAfterTwoMonthsFromCleaningDate() {
        //Given
        LocalDateTime cleaningDate = LocalDateTime.of(2023, Month.DECEMBER, 2, 8, 0);
        Mockito.doReturn(cleaningDate).when(apartment).getCleaningDate();

        LocalDateTime mockNow = LocalDateTime.of(2024, Month.JANUARY, 9, 11, 0);
        Mockito.doReturn(mockNow).when(cleaningRemindingTrigger).getLocalDateTimeNow();

        Date expectedDate = Date.from(
                LocalDateTime.of(2024, Month.JANUARY, 27, 10, 0)
                        .atZone(ZoneId.systemDefault()).toInstant());
        //When
        Date actualDate = cleaningRemindingTrigger.nextExecutionTime(triggerContext);

        //Then
        Assertions.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void cleaningRemindingTrigger_nextExecutionTimeTest_nowIsNextDayFromCleaningDay_shouldRemindAfterOneMonthsFromCleaningDate() {
        //Given
        LocalDateTime cleaningDate = LocalDateTime.of(2023, Month.DECEMBER, 2, 8, 0);
        Mockito.doReturn(cleaningDate).when(apartment).getCleaningDate();

        LocalDateTime mockNow = LocalDateTime.of(2023, Month.DECEMBER, 3, 11, 0);
        Mockito.doReturn(mockNow).when(cleaningRemindingTrigger).getLocalDateTimeNow();

        Date expectedDate = Date.from(
                LocalDateTime.of(2023, Month.DECEMBER, 30, 10, 0)
                        .atZone(ZoneId.systemDefault()).toInstant());
        //When
        Date actualDate = cleaningRemindingTrigger.nextExecutionTime(triggerContext);

        //Then
        Assertions.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void cleaningRemindingTrigger_nextExecutionTimeTest_nowIsABitLessThanDayBeforeCleaningDay_shouldRemindAtCleaningDate() {
        //Given
        LocalDateTime cleaningDate = LocalDateTime.of(2023, Month.DECEMBER, 2, 8, 0);
        Mockito.doReturn(cleaningDate).when(apartment).getCleaningDate();

        LocalDateTime mockNow = LocalDateTime.of(2023, Month.DECEMBER, 1, 11, 0);
        Mockito.doReturn(mockNow).when(cleaningRemindingTrigger).getLocalDateTimeNow();

        Date expectedDate = Date.from(
                LocalDateTime.of(2023, Month.DECEMBER, 2, 10, 0)
                        .atZone(ZoneId.systemDefault()).toInstant());
        //When
        Date actualDate = cleaningRemindingTrigger.nextExecutionTime(triggerContext);

        //Then
        Assertions.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void cleaningRemindingTrigger_nextExecutionTimeTest_nowIsABitMoreOneDayBeforeCleaningDay_shouldRemindAtCleaningDate() {
        //Given
        LocalDateTime cleaningDate = LocalDateTime.of(2023, Month.DECEMBER, 2, 8, 0);
        Mockito.doReturn(cleaningDate).when(apartment).getCleaningDate();

        LocalDateTime mockNow = LocalDateTime.of(2023, Month.DECEMBER, 1, 7, 0);
        Mockito.doReturn(mockNow).when(cleaningRemindingTrigger).getLocalDateTimeNow();

        Date expectedDate = Date.from(
                LocalDateTime.of(2023, Month.DECEMBER, 2, 10, 0)
                        .atZone(ZoneId.systemDefault()).toInstant());
        //When
        Date actualDate = cleaningRemindingTrigger.nextExecutionTime(triggerContext);

        //Then
        Assertions.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void cleaningRemindingTrigger_nextExecutionTimeTest_nowIsABitLessThanOneMonthBeforeCleaningDay_shouldRemindAtOneMonthBeforeCleaningDate() {
        //Given
        LocalDateTime cleaningDate = LocalDateTime.of(2023, Month.DECEMBER, 2, 8, 0);
        Mockito.doReturn(cleaningDate).when(apartment).getCleaningDate();

        LocalDateTime mockNow = LocalDateTime.of(2023, Month.NOVEMBER, 1, 11, 0);
        Mockito.doReturn(mockNow).when(cleaningRemindingTrigger).getLocalDateTimeNow();

        Date expectedDate = Date.from(
                LocalDateTime.of(2023, Month.NOVEMBER, 4, 10, 0)
                        .atZone(ZoneId.systemDefault()).toInstant());
        //When
        Date actualDate = cleaningRemindingTrigger.nextExecutionTime(triggerContext);

        //Then
        Assertions.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void cleaningRemindingTrigger_nextExecutionTimeTest_nowIsABitMoreThanThreeMonthBeforeCleaningDay_shouldRemindAtThreeMonthBeforeCleaningDate() {
        //Given
        LocalDateTime cleaningDate = LocalDateTime.of(2023, Month.DECEMBER, 2, 8, 0);
        Mockito.doReturn(cleaningDate).when(apartment).getCleaningDate();

        LocalDateTime mockNow = LocalDateTime.of(2023, Month.AUGUST, 1, 11, 0);
        Mockito.doReturn(mockNow).when(cleaningRemindingTrigger).getLocalDateTimeNow();

        Date expectedDate = Date.from(
                LocalDateTime.of(2023, Month.AUGUST, 12, 10, 0)
                        .atZone(ZoneId.systemDefault()).toInstant());
        //When
        Date actualDate = cleaningRemindingTrigger.nextExecutionTime(triggerContext);

        //Then
        Assertions.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void cleaningRemindingTrigger_nextExecutionTimeTest_nowIsLessThanOneDayAfterTargetDate_shouldReturnNextMonthDate() {
        //Given
        LocalDateTime cleaningDate = LocalDateTime.of(2024, Month.JANUARY, 20, 10, 0);
        Mockito.doReturn(cleaningDate).when(apartment).getCleaningDate();

        LocalDateTime mockNow = LocalDateTime.of(2024, Month.JANUARY, 20, 10, 0, 1);
        Mockito.doReturn(mockNow).when(cleaningRemindingTrigger).getLocalDateTimeNow();

        Date expectedDate = Date.from(
                LocalDateTime.of(2024, Month.FEBRUARY, 17, 10, 0)
                        .atZone(ZoneId.systemDefault()).toInstant());
        //When
        Date actualDate = cleaningRemindingTrigger.nextExecutionTime(triggerContext);

        //Then
        Assertions.assertEquals(expectedDate, actualDate);
    }
}