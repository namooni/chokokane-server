package com.example.chokokane.model.budget;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class MonthlyBudget {

  private Long id;
  private Long userId;
  private LocalDate budgetMonth;
  private Integer amount;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public MonthlyBudget(
      Long id,
      Long userId,
      LocalDate budgetMonth,
      Integer amount,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    this.id = id;
    this.userId = userId;
    this.budgetMonth = budgetMonth;
    this.amount = amount;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

}
