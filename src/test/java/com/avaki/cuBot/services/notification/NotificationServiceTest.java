package com.avaki.cuBot.services.notification;

import com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.utils.MapSetContainer;
import com.avaki.cuBot.services.UserService;
import com.avaki.cuBot.services.notification.triggerTaskCreators.TriggerTaskCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledFuture;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private Executor taskExecutor;

    @Mock
    private List<TriggerTaskCreator> triggerTaskCreators;

    private final NotificationService notificationService = new NotificationService(userService, taskExecutor, triggerTaskCreators);
    private final MapSetContainer<Long, ScheduledFuture<?>> scheduledTasksContainer = notificationService.getScheduledTasksContainer();
    private final long USER_ID = 1;

    @AfterEach
    public void afterEachTest() {
        scheduledTasksContainer.clear();
    }

    @Test
    public void cancelScheduledTasksTest_thereIsNoUserIdInTasksMap_shouldNotThrowNPE() {
        //Given;

        //When
        notificationService.cancelScheduledTasks(USER_ID);

        //Then
    }

    @Test
    public void cancelScheduledTasksTest_emptyScheduledTaskSet_shouldNotThrowNPEAndRemoveUserIdFromMap() {
        //Given
        scheduledTasksContainer.put(USER_ID, Collections.emptySet());

        //When
        notificationService.cancelScheduledTasks(USER_ID);

        //Then
        Assertions.assertTrue(scheduledTasksContainer.isEmpty());
    }

    @Test
    public void cancelScheduledTasksTest_notEmptyTasksSet_shouldCancelUserTask() {
        //Given
        ScheduledFuture<?> scheduledFuture = Mockito.mock(ScheduledFuture.class);
        scheduledTasksContainer.put(USER_ID, Collections.singleton(scheduledFuture));

        //When
        notificationService.cancelScheduledTasks(USER_ID);

        //Then
        Assertions.assertTrue(scheduledTasksContainer.isEmpty());
        Mockito.verify(scheduledFuture).cancel(false);
    }
}