package com.avaki.cuBot.repositories;

import com.avaki.cuBot.models.Reading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface ReadingRepository extends JpaRepository<Reading, Long> {

    @Query(value = "SELECT r.value FROM \n" +
            "readings r \n" +
            "JOIN meters m ON r.meter_id=m.id \n" +
            "JOIN apartments ap ON m.apartment_id=ap.id\n" +
            "JOIN users u ON ap.id=u.apartment_id\n" +
            "WHERE u.chat_id=:chatId AND r.reading_type=:readingType\n" +
            "ORDER BY r.created_date DESC\n" +
            "LIMIT 1"
            , nativeQuery = true)
    Optional<BigDecimal> findLastReadingValueByChatIdAndReadingType(@Param("chatId") Long chatId, @Param("readingType")String readingType);

    @Query(value = "SELECT r.* FROM \n" +
            "readings r \n" +
            "JOIN meters m ON r.meter_id=m.id \n" +
            "JOIN apartments ap ON m.apartment_id=ap.id\n" +
            "JOIN users u ON ap.id=u.apartment_id\n" +
            "WHERE u.chat_id=:chatId AND r.reading_type=:readingType\n" +
            "ORDER BY r.created_date DESC\n" +
            "LIMIT 1"
            , nativeQuery = true)
    Optional<Reading> findLastReadingByChatIdAndReadingType(@Param("chatId") Long chatId, @Param("readingType")String readingType);
}