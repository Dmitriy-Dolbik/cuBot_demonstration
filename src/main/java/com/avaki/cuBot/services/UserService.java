package com.avaki.cuBot.services;

import com.avaki.cuBot.models.User;
import com.avaki.cuBot.models.enums.Status;
import com.avaki.cuBot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findById(long chatId) {
        return userRepository.findById(chatId);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAllByStatus(Status status) {
        return userRepository.findAllByStatus(status);
    }
}