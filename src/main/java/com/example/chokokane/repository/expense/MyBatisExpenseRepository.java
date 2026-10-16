package com.example.chokokane.repository.expense;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.chokokane.mapper.expense.ExpenseMapper;
import com.example.chokokane.model.analytics.CategoryExpenseTotal;
import com.example.chokokane.model.analytics.DailyCategoryExpenseTotal;
import com.example.chokokane.model.expense.Expense;
import com.example.chokokane.model.expense.ExpenseDetail;
import com.example.chokokane.model.history.HistoryCursor;

@Repository
public class MyBatisExpenseRepository implements ExpenseRepository {

  private ExpenseMapper expenseMapper;

  public MyBatisExpenseRepository(ExpenseMapper expenseMapper) {
    this.expenseMapper = expenseMapper;
  }

  @Override
  public List<ExpenseDetail> findAll(
      Long userId,
      List<Long> categoryIds,
      Integer minAmount,
      Integer maxAmount,
      String sort,
      int offset,
      int size) {
    return expenseMapper.findAll(
        userId,
        categoryIds,
        minAmount,
        maxAmount,
        sort,
        offset,
        size);
  }

  @Override
  public int getTotalElements(
      Long userId,
      List<Long> categoryIds,
      Integer minAmount,
      Integer maxAmount) {
    return expenseMapper.getTotalElements(
        userId,
        categoryIds,
        minAmount,
        maxAmount);
  }

  @Override
  public int getTotalAmount(
      Long userId,
      List<Long> categoryIds,
      Integer minAmount,
      Integer maxAmount) {
    return expenseMapper.getTotalAmount(
        userId,
        categoryIds,
        minAmount,
        maxAmount);
  }

  @Override
  public Long getMonthlySpent(
      Long userId,
      LocalDate monthStart,
      LocalDate nextMonthStart,
      LocalDateTime createdAtStart,
      LocalDateTime createdAtEnd) {
    return expenseMapper.getMonthlySpent(
        userId,
        monthStart,
        nextMonthStart,
        createdAtStart,
        createdAtEnd);
  }

  @Override
  public List<CategoryExpenseTotal> getCategoryTotalByMonth(
      Long userId,
      LocalDate monthStart,
      LocalDate nextMonthStart,
      LocalDateTime createdAtStart,
      LocalDateTime createdAtEnd) {
    return expenseMapper.getCategoryTotalByMonth(
        userId,
        monthStart,
        nextMonthStart,
        createdAtStart,
        createdAtEnd);
  }

  @Override
  public List<DailyCategoryExpenseTotal> getDailyCategoryTotalsByMonth(
      Long userId,
      LocalDate monthStart,
      LocalDate nextMonthStart) {
    return expenseMapper.getDailyCategoryTotalsByMonth(
        userId,
        monthStart,
        nextMonthStart);
  }

  @Override
  public Long getUndatedSpentByMonth(
      Long userId,
      LocalDateTime createdAtStart,
      LocalDateTime createdAtEnd) {
    return expenseMapper.getUndatedSpentByMonth(
        userId,
        createdAtStart,
        createdAtEnd);
  }

  @Override
  public List<ExpenseDetail> findRecentByMonth(
      Long userId,
      LocalDate monthStart,
      LocalDate nextMonthStart,
      LocalDateTime createdAtStart,
      LocalDateTime createdAtEnd) {
    return expenseMapper.findRecentByMonth(
        userId,
        monthStart,
        nextMonthStart,
        createdAtStart,
        createdAtEnd);
  }

  @Override
  public Long getTodaySpent(
      Long userId,
      LocalDate today) {
    return expenseMapper.getTodaySpent(userId, today);
  }

  @Override
  public Long getHistoryFilteredTotal(
      Long userId,
      LocalDate monthStart,
      LocalDate nextMonthStart,
      LocalDateTime createdAtStart,
      LocalDateTime createdAtEnd,
      Long categoryId,
      boolean undated) {
    return expenseMapper.getHistoryFilteredTotal(
        userId,
        monthStart,
        nextMonthStart,
        createdAtStart,
        createdAtEnd,
        categoryId,
        undated);
  }

  @Override
  public List<ExpenseDetail> findHistory(
      Long userId,
      LocalDate monthStart,
      LocalDate nextMonthStart,
      LocalDateTime createdAtStart,
      LocalDateTime createdAtEnd,
      Long categoryId,
      boolean undated,
      HistoryCursor cursor,
      int limit) {
    return expenseMapper.findHistory(
        userId,
        monthStart,
        nextMonthStart,
        createdAtStart,
        createdAtEnd,
        categoryId,
        undated,
        cursor,
        limit);
  }

  @Override
  public Expense findById(Long userId, Long id) {
    return expenseMapper.findById(userId, id);
  }

  @Override
  public Expense save(Long userId, Expense expense) {
    expenseMapper.save(userId, expense);
    return expense;
  }

  @Override
  public int update(Long userId, Expense expense) {
    return expenseMapper.update(userId, expense);
  }

  @Override
  public int deleteAllByUserId(Long userId) {
    return expenseMapper.deleteAllByUserId(userId);
  }

  @Override
  public int deleteById(Long userId, Long id) {
    return expenseMapper.delete(userId, id);
  }
}
