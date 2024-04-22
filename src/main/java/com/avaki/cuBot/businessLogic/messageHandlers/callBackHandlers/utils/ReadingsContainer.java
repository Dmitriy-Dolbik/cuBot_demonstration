package com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.utils;

import com.avaki.cuBot.exceptions.NotFoundException;
import com.avaki.cuBot.models.Reading;
import com.avaki.cuBot.models.enums.ReadingType;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Мапа хранящая новые и предыдущие показания счетчиков для всех юзеров
 * Map<userId, Set<Pair<новое показание арендатора, предыдущее показание арендатора>>>
 * <p>
 * Новое показание хранится в виде полноценного объекта для последующего сохранения в БД
 * Предыдущее показание хранится только в виде значения, т.к. нужно только для вычислений.
 */
@Component
public class ReadingsContainer extends MapSetContainer<Long, Pair<Reading, BigDecimal>>{

    public Reading getNewReading(long chatId, ReadingType readingType) {
        return map.get(chatId).stream()
                .filter(pair -> readingType.equals(pair.getFirst().getReadingType()))
                .map(Pair::getFirst)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("New %s readings can't be saved for user %s, because can't be found in readingMap",
                        readingType, chatId)));
    }

    public BigDecimal getPreviousReadingValue(@NotNull long chatId, @NotNull ReadingType readingType) {
        return map.get(chatId).stream()
                .filter(pair -> readingType.equals(pair.getFirst().getReadingType()))
                .map(Pair::getSecond)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Previous %s readings can't be saved for user %s, because can't be found in readingMap",
                        readingType, chatId)));
    }
}
