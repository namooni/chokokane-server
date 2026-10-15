package com.example.chokokane.common.time;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import org.junit.jupiter.api.Test;
import com.example.chokokane.exception.BadRequestException;

class MonthRangeTest {
  @Test
  void convertsJstLeapMonthToExclusiveUtcBoundaries() {
    MonthRange range = MonthRange.of(YearMonth.of(2024, 2), LocalDate.of(2026, 10, 15));
    assertEquals(LocalDate.of(2024, 2, 1), range.monthStart());
    assertEquals(LocalDate.of(2024, 3, 1), range.nextMonthStart());
    assertEquals(LocalDateTime.of(2024, 1, 31, 15, 0), range.createdAtStart());
    assertEquals(LocalDateTime.of(2024, 2, 29, 15, 0), range.createdAtEnd());
    assertFalse(range.isCurrentMonth());
  }

  @Test
  void handlesYearRolloverAndCurrentMonth() {
    MonthRange range = MonthRange.of(YearMonth.of(2025, 12), LocalDate.of(2025, 12, 31));
    assertEquals(LocalDate.of(2026, 1, 1), range.nextMonthStart());
    assertEquals(LocalDateTime.of(2025, 12, 31, 15, 0), range.createdAtEnd());
    assertTrue(range.isCurrentMonth());
  }

  @Test
  void rejectsFutureAndMissingMonth() {
    LocalDate today = LocalDate.of(2026, 10, 15);
    assertThrows(BadRequestException.class, () -> MonthRange.of(YearMonth.of(2026, 11), today));
    assertThrows(BadRequestException.class, () -> MonthRange.of(null, today));
  }
}
