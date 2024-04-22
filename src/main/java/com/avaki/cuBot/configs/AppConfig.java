package com.avaki.cuBot.configs;

import com.avaki.cuBot.models.User;
import com.avaki.cuBot.models.enums.Status;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(org.telegram.telegrambots.meta.api.objects.User.class, User.class)
                .setPostConverter(context -> {
                    User user = context.getDestination();
                    org.telegram.telegrambots.meta.api.objects.User sourceUser = context.getSource();
                    user.setChatId(sourceUser.getId());
                    user.setFirstName(sourceUser.getFirstName());
                    user.setLastName(sourceUser.getLastName());
                    user.setUserName(sourceUser.getUserName());
                    user.setRegisteredDate(LocalDateTime.now());
                    user.setStatus(Status.ACTIVE);
                    return user;
                });
        return modelMapper;
    }

    @Bean
    public Executor taskExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }
}
