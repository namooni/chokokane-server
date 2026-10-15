package com.example.chokokane.repository.budget;

import java.time.LocalDate;

import org.springframework.stereotype.Repository;

import com.example.chokokane.mapper.budget.MonthlyBudgetMapper;
import com.example.chokokane.model.budget.MonthlyBudget;

@Repository
public class MyBatisMonthlyBudgetRepository implements MonthlyBudgetRepository {

  private final MonthlyBudgetMapper monthlyBudgetMapper;

  public MyBatisMonthlyBudgetRepository(MonthlyBudgetMapper monthlyBudgetMapper) {
    this.monthlyBudgetMapper = monthlyBudgetMapper;
  }

  @Override
  public MonthlyBudget findByUserIdAndMonth(
      Long userId,
      LocalDate budgetMonth) {
    return monthlyBudgetMapper.findByUserIdAndMonth(userId, budgetMonth);
  }

  @Override
  public int upsert(
      Long userId,
      LocalDate budgetMonth,
      Integer amount) {
    return monthlyBudgetMapper.upsert(userId, budgetMonth, amount);
  }
}
