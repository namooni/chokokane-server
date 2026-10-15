package com.example.chokokane.service.dashboard;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.chokokane.dto.category.CategorySummaryResponse;
import com.example.chokokane.dto.dashboard.DashboardResponse;
import com.example.chokokane.dto.dashboard.DashboardStatus;
import com.example.chokokane.dto.expense.ExpenseResponse;
import com.example.chokokane.exception.BadRequestException;
import com.example.chokokane.model.budget.MonthlyBudget;
import com.example.chokokane.model.expense.ExpenseDetail;
import com.example.chokokane.repository.budget.MonthlyBudgetRepository;
import com.example.chokokane.repository.expense.ExpenseRepository;

@Service
public class DashboardService {
  private final MonthlyBudgetRepository monthlyBudgetRepository;
  private final ExpenseRepository expenseRepository;

  private DashboardStatus determineStatus(
      Long budget,
      long monthlySpent,
      boolean isCurrentMonth,
      int dayOfMonth,
      int daysInMonth) {

    if (budget == null) {
      return DashboardStatus.NO_BUDGET;
    }

    if (monthlySpent > budget) {
      return DashboardStatus.OVER;
    }

    if (!isCurrentMonth) {
      return DashboardStatus.SAFE;
    }

    if (dayOfMonth <= 2) {
      return DashboardStatus.INSUFFICIENT_DATA;
    }

    double projectedSpent = (double) monthlySpent / dayOfMonth * daysInMonth;

    if (projectedSpent > budget * 1.05) {
      return DashboardStatus.WARNING;
    }

    return DashboardStatus.SAFE;
  }

  public DashboardService(
      MonthlyBudgetRepository monthlyBudgetRepository,
      ExpenseRepository expenseRepository) {
    this.monthlyBudgetRepository = monthlyBudgetRepository;
    this.expenseRepository = expenseRepository;
  }

  public DashboardResponse getDashboard(Long userId, YearMonth month) {
    ZoneId jst = ZoneId.of("Asia/Tokyo");

    LocalDate today = LocalDate.now(jst);

    if (month.isAfter(YearMonth.from(today))) {
      throw new BadRequestException("未来の月は照会できません。");
    }

    LocalDate monthStart = month.atDay(1);
    LocalDate nextMonthStart = month.plusMonths(1).atDay(1);
    LocalDateTime createdAtStart = monthStart
        .atStartOfDay(jst)
        .withZoneSameInstant(ZoneOffset.UTC)
        .toLocalDateTime();
    LocalDateTime createdAtEnd = nextMonthStart
        .atStartOfDay(jst)
        .withZoneSameInstant(ZoneOffset.UTC)
        .toLocalDateTime();

    boolean isCurrentMonth = month.equals(YearMonth.from(today));

    MonthlyBudget monthlyBudget = monthlyBudgetRepository.findByUserIdAndMonth(userId, monthStart);

    long monthlySpent = expenseRepository.getMonthlySpent(
        userId,
        monthStart,
        nextMonthStart,
        createdAtStart,
        createdAtEnd);

    Long todaySpent = isCurrentMonth ? expenseRepository.getTodaySpent(userId, today) : null;

    List<ExpenseDetail> recentExpenses = expenseRepository.findRecentByMonth(
        userId,
        monthStart,
        nextMonthStart,
        createdAtStart,
        createdAtEnd);

    List<ExpenseResponse> recentResponses = recentExpenses.stream()
        .map(expense -> new ExpenseResponse(
            expense.getId(),
            expense.getAmount(),
            new CategorySummaryResponse(
                expense.getCategoryId(),
                expense.getCategoryName(),
                expense.getCategoryColor()),
            expense.getMemo(),
            expense.getExpenseDate(),
            expense.getExpenseTime(),
            expense.getCreatedAt()))
        .toList();

    Long budget = monthlyBudget == null
        ? null
        : monthlyBudget.getAmount().longValue();
    Long remainingBudget = null;
    Long dailySpendable = null;
    Long todayAllowance = null;

    if (budget != null) {
      remainingBudget = budget - monthlySpent;

      if (isCurrentMonth && todaySpent != null) {
        int remainingDays = month.lengthOfMonth() - today.getDayOfMonth() + 1;

        dailySpendable = Math.max(
            Math.floorDiv(remainingBudget, remainingDays),
            0L);

        todayAllowance = Math.max(
            Math.floorDiv(remainingBudget + todaySpent, remainingDays),
            0L);
      }
    }

    DashboardStatus status = determineStatus(
        budget,
        monthlySpent,
        isCurrentMonth,
        today.getDayOfMonth(),
        month.lengthOfMonth());

    return new DashboardResponse(
        month.toString(),
        budget,
        monthlySpent,
        remainingBudget,
        dailySpendable,
        todaySpent,
        todayAllowance,
        status,
        recentResponses);
  }
}
