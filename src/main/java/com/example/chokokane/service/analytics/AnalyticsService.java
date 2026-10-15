package com.example.chokokane.service.analytics;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.chokokane.common.time.MonthRange;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.chokokane.dto.analytics.AnalyticsResponse;
import com.example.chokokane.dto.analytics.CategoryAmountResponse;
import com.example.chokokane.dto.analytics.DailyExpenseResponse;
import com.example.chokokane.dto.category.CategorySummaryResponse;
import com.example.chokokane.model.analytics.CategoryExpenseTotal;
import com.example.chokokane.model.analytics.DailyCategoryExpenseTotal;
import com.example.chokokane.model.budget.MonthlyBudget;
import com.example.chokokane.repository.budget.MonthlyBudgetRepository;
import com.example.chokokane.repository.expense.ExpenseRepository;

@Service
public class AnalyticsService {

  private final MonthlyBudgetRepository monthlyBudgetRepository;
  private final ExpenseRepository expenseRepository;

  public AnalyticsService(
      MonthlyBudgetRepository monthlyBudgetRepository,
      ExpenseRepository expenseRepository) {
    this.monthlyBudgetRepository = monthlyBudgetRepository;
    this.expenseRepository = expenseRepository;
  }

  @Transactional(readOnly = true)
  public AnalyticsResponse getAnalytics(Long userId, YearMonth month) {
    MonthRange range = MonthRange.of(month);
    LocalDate today = range.today();
    LocalDate monthStart = range.monthStart();
    LocalDate nextMonthStart = range.nextMonthStart();
    LocalDateTime createdAtStart = range.createdAtStart();
    LocalDateTime createdAtEnd = range.createdAtEnd();

    MonthlyBudget monthlyBudget = monthlyBudgetRepository.findByUserIdAndMonth(userId, monthStart);

    long monthlySpent = expenseRepository.getMonthlySpent(
        userId,
        monthStart,
        nextMonthStart,
        createdAtStart,
        createdAtEnd);

    long undatedSpent = expenseRepository.getUndatedSpentByMonth(
        userId,
        createdAtStart,
        createdAtEnd);

    List<CategoryExpenseTotal> categoryTotals = expenseRepository.getCategoryTotalByMonth(
        userId,
        monthStart,
        nextMonthStart,
        createdAtStart,
        createdAtEnd);

    List<DailyCategoryExpenseTotal> dailyTotals = expenseRepository.getDailyCategoryTotalsByMonth(
        userId,
        monthStart,
        nextMonthStart);

    Long budget = monthlyBudget == null ? null : monthlyBudget.getAmount().longValue();
    Long remainingBudget = null;
    Long dailySpendable = null;

    if (budget != null) {
      long remaining = budget - monthlySpent;
      remainingBudget = remaining;
      if (range.isCurrentMonth()) {
        int remainingDays = month.lengthOfMonth() - today.getDayOfMonth() + 1;
        dailySpendable = Math.max(Math.floorDiv(remaining, remainingDays), 0L);
      }
    }

    List<CategoryAmountResponse> categoryResponses = categoryTotals
        .stream()
        .map(item -> new CategoryAmountResponse(
            new CategorySummaryResponse(
                item.getCategoryId(),
                item.getCategoryName(),
                item.getCategoryColor()),
            item.getTotalAmount()))
        .toList();

    Map<LocalDate, List<DailyCategoryExpenseTotal>> totalsByDate = dailyTotals
        .stream()
        .collect(Collectors.groupingBy(item -> item.getExpenseDate()));
    List<DailyExpenseResponse> dailyResponses = new ArrayList<>();
    for (int day = 1; day <= month.lengthOfMonth(); day++) {
      LocalDate date = month.atDay(day);
      // Future days are explicitly represented as zero, even if invalid historical
      // data exists.
      List<DailyCategoryExpenseTotal> rows = date.isAfter(today)
          ? List.of()
          : totalsByDate.getOrDefault(date, List.of());
      long total = rows.stream().mapToLong(item -> item.getTotalAmount()).sum();
      List<CategoryAmountResponse> categories = rows
          .stream()
          .map(item -> new CategoryAmountResponse(
              new CategorySummaryResponse(item.getCategoryId(), item.getCategoryName(), item.getCategoryColor()),
              item.getTotalAmount()))
          .toList();
      dailyResponses.add(new DailyExpenseResponse(date, total, categories));
    }

    return new AnalyticsResponse(
        month.toString(), budget, monthlySpent, remainingBudget, dailySpendable,
        undatedSpent, List.copyOf(dailyResponses), categoryResponses);
  }
}
