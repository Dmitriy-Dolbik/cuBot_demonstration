package com.avaki.cuBot.utils;

import java.math.BigDecimal;

public interface FindLastReadingsValueStrategy {

    BigDecimal findLastReadingValueByChatId(long chatId);
}
