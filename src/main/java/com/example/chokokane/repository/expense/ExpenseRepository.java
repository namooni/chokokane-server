package com.example.chokokane.repository.expense;

import java.util.List;

import com.example.chokokane.model.expense.Expense;
import com.example.chokokane.model.expense.ExpenseDetail;

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

  Expense findById(Long userId, Long id);

  Expense save(Long userId, Expense expense);

  int update(Long userId, Expense expense);

  int deleteById(Long userId, Long id);
}
