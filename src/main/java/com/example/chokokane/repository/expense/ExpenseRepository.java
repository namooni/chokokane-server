package com.example.chokokane.repository.expense;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.chokokane.model.analytics.CategoryExpenseTotal;
import com.example.chokokane.model.analytics.DailyCategoryExpenseTotal;
import com.example.chokokane.model.expense.Expense;
import com.example.chokokane.model.expense.ExpenseDetail;
import com.example.chokokane.model.history.HistoryCursor;

public interface ExpenseRepository {

  List<ExpenseDetail> findAll(
      Long userId,
      List<Long> categoryIds,
      Integer minAmount,
      Integer maxAmount,
      String sort,
      int offset,
      int size);

  int getTotalElements(
      Long userId,
      List<Long> categoryIds,
      Integer minAmount,
      Integer maxAmount);

  int getTotalAmount(
      Long userId,
      List<Long> categoryIds,
      Integer minAmount,
      Integer maxAmount);

  Long getMonthlySpent(
      Long userId,
      LocalDate monthStart,
      LocalDate nextMonthStart,
      LocalDateTime createdAtStart,
      LocalDateTime createdAtEnd);

  Long getTodaySpent(
      Long userId,
      LocalDate today);

  List<CategoryExpenseTotal> getCategoryTotalByMonth(
      Long userId,
      LocalDate monthStart,
      LocalDate nextMonthStart,
      LocalDateTime createdAtStart,
      LocalDateTime createdAtEnd);

  List<DailyCategoryExpenseTotal> getDailyCategoryTotalsByMonth(
      Long userId,
      LocalDate monthStart,
      LocalDate nextMonthStart);

  Long getUndatedSpentByMonth(
      Long userId,
      LocalDateTime createdAtStart,
      LocalDateTime createdAtEnd);

  List<ExpenseDetail> findRecentByMonth(
      Long userId,
      LocalDate monthStart,
      LocalDate nextMonthStart,
      LocalDateTime createdAtStart,
      LocalDateTime createdAtEnd);

  Long getHistoryFilteredTotal(
      Long userId,
      LocalDate monthStart,
      LocalDate nextMonthStart,
      LocalDateTime createdAtStart,
      LocalDateTime createdAtEnd,
      Long categoryId,
      boolean undated);

  List<ExpenseDetail> findHistory(
      Long userId,
      LocalDate monthStart,
      LocalDate nextMonthStart,
      LocalDateTime createdAtStart,
      LocalDateTime createdAtEnd,
      Long categoryId,
      boolean undated,
      HistoryCursor cursor,
      int limit);

  Expense findById(Long userId, Long id);

  Expense save(Long userId, Expense expense);

  int update(Long userId, Expense expense);

  int deleteAllByUserId(Long userId);

  int deleteById(Long userId, Long id);
}
