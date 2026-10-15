package com.example.chokokane.common.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;

import com.example.chokokane.exception.BadRequestException;

/** A JST calendar month and its exclusive UTC timestamp boundaries. */
public record MonthRange(
    YearMonth month,
    LocalDate today,
    LocalDate monthStart,
    LocalDate nextMonthStart,
    LocalDateTime createdAtStart,
    LocalDateTime createdAtEnd) {

  private static final ZoneId JST = ZoneId.of("Asia/Tokyo");

  public static MonthRange of(YearMonth month) {
    return of(month, LocalDate.now(JST));
  }

  public static MonthRange of(YearMonth month, LocalDate today) {
    if (month == null) {
      throw new BadRequestException("表示する月を指定してください。");
    }
    if (month.isAfter(YearMonth.from(today))) {
      throw new BadRequestException("未来の月は指定できません。");
    }
    LocalDate start = month.atDay(1);
    LocalDate end = month.plusMonths(1).atDay(1);
    return new MonthRange(month, today, start, end,
        start.atStartOfDay(JST).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime(),
        end.atStartOfDay(JST).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
  }

  public boolean isCurrentMonth() {
    return month.equals(YearMonth.from(today));
  }
}
