package com.example.chokokane.repository.budget;

import java.time.LocalDate;

import com.example.chokokane.model.budget.MonthlyBudget;

public interface MonthlyBudgetRepository {

  MonthlyBudget findByUserIdAndMonth(
      Long userId,
      LocalDate budgetMonth);

  int upsert(
      Long userId,
      LocalDate budgetMonth,
      Integer amount);

  int deleteAllByUserId(Long userId);
}
