package com.avaki.cuBot.services;

import com.avaki.cuBot.constants.ErrorTextConstants;
import com.avaki.cuBot.exceptions.NotFoundException;
import com.avaki.cuBot.models.Meter;
import com.avaki.cuBot.models.enums.ReadingType;
import com.avaki.cuBot.repositories.MeterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeterService {
    private final MeterRepository meterRepository;

    public Meter findByUserIdAndReadingType(long chatId, ReadingType readingType) {
        return meterRepository.findByUserIdAndReadingType(chatId, readingType.toString())
                .orElseThrow(() -> new NotFoundException(
                        String.format(ErrorTextConstants.NOT_FOUND_EXCEPTION, "ColdWaterMeter", chatId)
                ));
    }
}
