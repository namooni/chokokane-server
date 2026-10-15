package com.example.chokokane.service.analytics;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.chokokane.dto.analytics.AnalyticsResponse;
import com.example.chokokane.exception.BadRequestException;
import com.example.chokokane.model.analytics.CategoryExpenseTotal;
import com.example.chokokane.model.analytics.DailyCategoryExpenseTotal;
import com.example.chokokane.model.budget.MonthlyBudget;
import com.example.chokokane.repository.budget.MonthlyBudgetRepository;
import com.example.chokokane.repository.expense.ExpenseRepository;

class AnalyticsServiceTest {
  private MonthlyBudgetRepository budgets;
  private ExpenseRepository expenses;
  private AnalyticsService service;

  @BeforeEach
  void setUp() {
    budgets = mock(MonthlyBudgetRepository.class);
    expenses = mock(ExpenseRepository.class);
    service = new AnalyticsService(budgets, expenses);
    when(expenses.getMonthlySpent(anyLong(), any(), any(), any(), any())).thenReturn(0L);
    when(expenses.getUndatedSpentByMonth(anyLong(), any(), any())).thenReturn(0L);
    when(expenses.getCategoryTotalByMonth(anyLong(), any(), any(), any(), any())).thenReturn(List.of());
    when(expenses.getDailyCategoryTotalsByMonth(anyLong(), any(), any())).thenReturn(List.of());
  }

  @Test
  void leapMonthIncludesAllDaysAndUsesUtcBoundaries() {
    AnalyticsResponse result = service.getAnalytics(9L, YearMonth.of(2024, 2));
    assertEquals(29, result.dailyExpenses().size());
    assertEquals(LocalDate.of(2024, 2, 29), result.dailyExpenses().get(28).date());
    assertTrue(result.dailyExpenses().stream().allMatch(day -> day.totalAmount() == 0 && day.categories().isEmpty()));
    assertNull(result.budget());
    assertNull(result.remainingBudget());
    assertNull(result.dailySpendable());
    verify(expenses).getMonthlySpent(9L, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 3, 1),
        LocalDateTime.of(2024, 1, 31, 15, 0), LocalDateTime.of(2024, 2, 29, 15, 0));
  }

  @Test
  void datedAndUndatedTotalsRemainSeparate() {
    YearMonth month = YearMonth.of(2024, 2);
    when(expenses.getMonthlySpent(anyLong(), any(), any(), any(), any())).thenReturn(1500L);
    when(expenses.getUndatedSpentByMonth(anyLong(), any(), any())).thenReturn(500L);
    CategoryExpenseTotal category = new CategoryExpenseTotal();
    category.setCategoryId(11L);
    category.setCategoryName("交通");
    category.setCategoryColor("#65A7D8");
    category.setTotalAmount(1500L);
    when(expenses.getCategoryTotalByMonth(anyLong(), any(), any(), any(), any())).thenReturn(List.of(category));
    DailyCategoryExpenseTotal daily = new DailyCategoryExpenseTotal();
    daily.setExpenseDate(month.atDay(15));
    daily.setCategoryId(11L);
    daily.setCategoryName("交通");
    daily.setCategoryColor("#65A7D8");
    daily.setTotalAmount(1000L);
    when(expenses.getDailyCategoryTotalsByMonth(anyLong(), any(), any())).thenReturn(List.of(daily));
    when(budgets.findByUserIdAndMonth(9L, month.atDay(1)))
        .thenReturn(new MonthlyBudget(1L, 9L, month.atDay(1), 10000, null, null));

    AnalyticsResponse result = service.getAnalytics(9L, month);
    assertEquals(1500L, result.monthlySpent());
    assertEquals(500L, result.undatedSpent());
    assertEquals(8500L, result.remainingBudget().longValue());
    assertNull(result.dailySpendable());
    assertEquals(1000L, result.dailyExpenses().stream().mapToLong(day -> day.totalAmount()).sum());
    assertEquals(1000L, result.dailyExpenses().get(14).categories().getFirst().amount());
    assertEquals(1500L, result.categoryTotals().getFirst().amount());
  }

  @Test
  void zeroBudgetIsPresentAndOverspendKeepsNegativeRemaining() {
    YearMonth month = YearMonth.from(LocalDate.now(ZoneId.of("Asia/Tokyo")));
    when(budgets.findByUserIdAndMonth(9L, month.atDay(1)))
        .thenReturn(new MonthlyBudget(1L, 9L, month.atDay(1), 0, null, null));
    when(expenses.getMonthlySpent(anyLong(), any(), any(), any(), any())).thenReturn(1500L);
    AnalyticsResponse result = service.getAnalytics(9L, month);
    assertEquals(0L, result.budget().longValue());
    assertEquals(-1500L, result.remainingBudget().longValue());
    assertEquals(0L, result.dailySpendable().longValue());
  }

  @Test
  void futureMonthIsRejectedBeforeAnyQueries() {
    clearInvocations(budgets, expenses);
    YearMonth future = YearMonth.from(LocalDate.now(ZoneId.of("Asia/Tokyo"))).plusMonths(1);
    assertThrows(BadRequestException.class, () -> service.getAnalytics(9L, future));
    verifyNoInteractions(budgets, expenses);
  }
}
