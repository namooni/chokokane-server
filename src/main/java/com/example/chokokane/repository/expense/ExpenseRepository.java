package com.example.chokokane.repository.expense;

import java.util.List;

import com.example.chokokane.model.expense.Expense;

public interface ExpenseRepository {

  List<Expense> findAll(
      Long userId,
      List<String> categories,
      Integer minAmount,
      Integer maxAmount,
      String sort,
      int page,
      int size);

  int getExpenseCount(
      Long userId,
      List<String> categories,
      Integer minAmount,
      Integer maxAmount);

  int getTotalAmount(
      Long userId,
      List<String> categories,
      Integer minAmount,
      Integer maxAmount);

  Expense findById(Long userId, Long id);

  Expense save(Long userId, Expense expense);

  int update(Long userId, Expense expense);

  int updateCategory(String oldCategory, String newCategory);

  int deleteById(Long userId, Long id);
}
