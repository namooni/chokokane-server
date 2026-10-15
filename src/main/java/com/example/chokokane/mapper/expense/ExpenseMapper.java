package com.example.chokokane.mapper.expense;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.chokokane.model.analytics.CategoryExpenseTotal;
import com.example.chokokane.model.analytics.DailyCategoryExpenseTotal;
import com.example.chokokane.model.expense.Expense;
import com.example.chokokane.model.expense.ExpenseDetail;
import com.example.chokokane.model.history.HistoryCursor;

@Mapper
public interface ExpenseMapper {

  int save(
      @Param("userId") Long userId,
      @Param("expense") Expense expense);

  List<ExpenseDetail> findAll(
      @Param("userId") Long userId,
      @Param("categoryIds") List<Long> categoryIds,
      @Param("minAmount") Integer minAmount,
      @Param("maxAmount") Integer maxAmount,
      @Param("sort") String sort,
      @Param("offset") int offset,
      @Param("size") int size);

  int getTotalElements(
      @Param("userId") Long userId,
      @Param("categoryIds") List<Long> categoryIds,
      @Param("minAmount") Integer minAmount,
      @Param("maxAmount") Integer maxAmount);

  int getTotalAmount(
      @Param("userId") Long userId,
      @Param("categoryIds") List<Long> categoryIds,
      @Param("minAmount") Integer minAmount,
      @Param("maxAmount") Integer maxAmount);

  Long getMonthlySpent(
      @Param("userId") Long userId,
      @Param("monthStart") LocalDate monthStart,
      @Param("nextMonthStart") LocalDate nextMonthStart,
      @Param("createdAtStart") LocalDateTime createdAtStart,
      @Param("createdAtEnd") LocalDateTime createdAtEnd);

  Long getTodaySpent(
      @Param("userId") Long userId,
      @Param("today") LocalDate today);

  List<CategoryExpenseTotal> getCategoryTotalByMonth(
      @Param("userId") Long userId,
      @Param("monthStart") LocalDate monthStart,
      @Param("nextMonthStart") LocalDate nextMonthStart,
      @Param("createdAtStart") LocalDateTime createdAtStart,
      @Param("createdAtEnd") LocalDateTime createdAtEnd);

  List<DailyCategoryExpenseTotal> getDailyCategoryTotalsByMonth(
      @Param("userId") Long userId,
      @Param("monthStart") LocalDate monthStart,
      @Param("nextMonthStart") LocalDate nextMonthStart);

  Long getUndatedSpentByMonth(
      @Param("userId") Long userId,
      @Param("createdAtStart") LocalDateTime createdAtStart,
      @Param("createdAtEnd") LocalDateTime createdAtEnd);

  List<ExpenseDetail> findRecentByMonth(
      @Param("userId") Long userId,
      @Param("monthStart") LocalDate monthStart,
      @Param("nextMonthStart") LocalDate nextMonthStart,
      @Param("createdAtStart") LocalDateTime createdAtStart,
      @Param("createdAtEnd") LocalDateTime createdAtEnd);

  Long getHistoryFilteredTotal(
      @Param("userId") Long userId,
      @Param("monthStart") LocalDate monthStart,
      @Param("nextMonthStart") LocalDate nextMonthStart,
      @Param("createdAtStart") LocalDateTime createdAtStart,
      @Param("createdAtEnd") LocalDateTime createdAtEnd,
      @Param("categoryId") Long categoryId,
      @Param("undated") boolean undated);

  List<ExpenseDetail> findHistory(
      @Param("userId") Long userId,
      @Param("monthStart") LocalDate monthStart,
      @Param("nextMonthStart") LocalDate nextMonthStart,
      @Param("createdAtStart") LocalDateTime createdAtStart,
      @Param("createdAtEnd") LocalDateTime createdAtEnd,
      @Param("categoryId") Long categoryId,
      @Param("undated") boolean undated,
      @Param("cursor") HistoryCursor cursor,
      @Param("limit") int limit);

  Expense findById(
      @Param("userId") Long userId,
      @Param("id") Long id);

  int update(
      @Param("userId") Long userId,
      @Param("expense") Expense expense);

  int delete(
      @Param("userId") Long userId,
      @Param("id") Long id);
}
