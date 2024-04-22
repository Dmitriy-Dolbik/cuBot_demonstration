package com.avaki.cuBot.services;

import com.avaki.cuBot.models.Reading;
import com.avaki.cuBot.models.enums.ReadingType;
import com.avaki.cuBot.repositories.ReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ReadingsService {
    private final ReadingRepository readingRepository;
    private final MeterService meterService;

    public BigDecimal findLastReadingValueByChatId(long chatId, ReadingType readingType) {
        return readingRepository.findLastReadingValueByChatIdAndReadingType(chatId, readingType.toString()).orElse(BigDecimal.ZERO);
    }

    public Reading findLastReadingByChatIdAndReadingType(long chatId, ReadingType readingType) {
        return readingRepository.findLastReadingByChatIdAndReadingType(chatId, readingType.toString()).orElse(new Reading(BigDecimal.ZERO, meterService.findByUserIdAndReadingType(chatId, readingType), readingType));
    }

    public void save(Reading reading) {
        readingRepository.save(reading);
    }
}
