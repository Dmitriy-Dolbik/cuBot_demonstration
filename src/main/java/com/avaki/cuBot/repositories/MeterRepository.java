package com.avaki.cuBot.repositories;

import com.avaki.cuBot.models.Meter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MeterRepository extends JpaRepository<Meter, Integer> {

    @Query(value = "SELECT m.*\n" +
            "FROM meters m\n" +
            "WHERE m.reading_type=:readingType AND m.apartment_id = (\n" +
                                        "SELECT us.apartment_id FROM users us \n" +
                                        "WHERE us.chat_id = :chatId)"
    , nativeQuery = true)
    Optional<Meter> findByUserIdAndReadingType(@Param("chatId") Long chatId, @Param("readingType") String readingType);
}
