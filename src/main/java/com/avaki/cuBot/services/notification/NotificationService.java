package com.avaki.cuBot.services.notification;

import com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.utils.MapSetContainer;
import com.avaki.cuBot.models.User;
import com.avaki.cuBot.models.enums.Status;
import com.avaki.cuBot.services.UserService;
import com.avaki.cuBot.services.notification.triggerTaskCreators.TriggerTaskCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService implements ApplicationRunner {

    private final UserService userService;

    private final Executor taskExecutor;

    private final List<TriggerTaskCreator> triggerTaskCreatorList;

    @Value("${admin_id}")
    private long adminId;

    private final MapSetContainer<Long, ScheduledFuture<?>> scheduledTasksContainer = new MapSetContainer<>();

    private TaskScheduler taskScheduler;

    @Override
    public void run(ApplicationArguments args) {
        if (taskScheduler == null) {
            taskScheduler = new ConcurrentTaskScheduler((ScheduledExecutorService) taskExecutor);
        }
        userService.findAllByStatus(Status.ACTIVE)
                .forEach(this::addScheduledTasks);
    }

    public void addScheduledTasks(User user) {
        triggerTaskCreatorList.forEach(taskCreator -> {
            addScheduledTask(user.getChatId(), taskCreator.create(user));
        });
    }

    private void addScheduledTask(long userId, TriggerTask triggerTask) {
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(triggerTask.getRunnable(), triggerTask.getTrigger());
        scheduledTasksContainer.put(userId, scheduledFuture);
    }

    public void cancelScheduledTasks(long userId) {
        Set<ScheduledFuture<?>> scheduledFutureSet = scheduledTasksContainer.remove(userId);
        if (scheduledFutureSet == null) {
            log.warn("Try to cancel scheduled task for user {}, but there is not scheduled tasks for this user", userId);
            return;
        }
        scheduledFutureSet.forEach(scheduledTask -> scheduledTask.cancel(false));
    }

    MapSetContainer<Long, ScheduledFuture<?>> getScheduledTasksContainer() {
        return scheduledTasksContainer;
    }
}

