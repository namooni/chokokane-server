package com.example.chokokane.model.expense;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;

@Getter
public class Expense {
  private Long id;
  private int amount;
  private Long categoryId;
  private String memo;
  private LocalDate expenseDate;
  private LocalTime expenseTime;
  private LocalDateTime createdAt;

  public Expense(
      Long id,
      int amount,
      Long categoryId,
      String memo,
      LocalDate expenseDate,
      LocalTime expenseTime,
      LocalDateTime createdAt) {
    this.id = id;
    this.amount = amount;
    this.categoryId = categoryId;
    this.memo = memo;
    this.expenseDate = expenseDate;
    this.expenseTime = expenseTime;
    this.createdAt = createdAt;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void update(
      int amount,
      Long categoryId,
      String memo,
      LocalDate expenseDate,
      LocalTime expenseTime) {
    this.amount = amount;
    this.categoryId = categoryId;
    this.memo = memo;
    this.expenseDate = expenseDate;
    this.expenseTime = expenseTime;
  }

}
