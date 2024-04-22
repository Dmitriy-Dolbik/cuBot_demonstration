package com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.concurrent.ScheduledFuture;

class MapSetContainerTest {
    private final long USER_ID = 1;
    private final MapSetContainer<Long, ScheduledFuture<?>> mapSetContainer = new MapSetContainer<>();
    @Mock
    private ScheduledFuture<?> scheduledFuture;

    @BeforeEach
    public void beforeEachTest() {
        mapSetContainer.clear();
    }

    @Test
    public void addTest_addTaskForNewUser_shouldCreateNewSetMap() {
        //Given

        //When
        mapSetContainer.put(USER_ID, scheduledFuture);

        //Then
        Assertions.assertEquals(1, mapSetContainer.size());
        Assertions.assertEquals(1, mapSetContainer.get(USER_ID).size());
    }

    @Test
    public void addTest_shouldCreateNewSetAndAddTasksToIt() {
        //Given
        ScheduledFuture<?> scheduledFuture2 = Mockito.mock(ScheduledFuture.class);

        //When
        mapSetContainer.put(USER_ID, scheduledFuture);
        mapSetContainer.put(USER_ID, scheduledFuture2);

        //Then
        Assertions.assertEquals(1, mapSetContainer.size());
        Assertions.assertEquals(2, mapSetContainer.get(USER_ID).size());
    }

    @Test
    public void addTest_shouldCreateNewSetForEachUserAndAddTasksToIt() {
        //Given
        ScheduledFuture<?> scheduledFuture2 = Mockito.mock(ScheduledFuture.class);

        //When
        mapSetContainer.put(USER_ID, scheduledFuture);
        mapSetContainer.put(USER_ID, scheduledFuture2);
        long userId2 = 2;
        mapSetContainer.put(userId2, scheduledFuture2);

        //Then
        Assertions.assertEquals(2, mapSetContainer.size());
        Assertions.assertEquals(2, mapSetContainer.get(USER_ID).size());
        Assertions.assertEquals(1, mapSetContainer.get(userId2).size());
    }
}