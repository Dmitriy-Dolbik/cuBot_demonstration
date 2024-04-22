package com.avaki.cuBot.repositories;

import com.avaki.cuBot.models.User;
import com.avaki.cuBot.models.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByStatus(Status status);
}
