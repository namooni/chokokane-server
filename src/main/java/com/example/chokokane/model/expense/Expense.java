package com.example.chokokane.model.expense;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Expense {
  private Long id;
  private int amount;
  private String category;
  private String memo;
  private LocalDate expenseDate;
  private LocalTime expenseTime;
  private LocalDateTime createdAt;

  public Expense(
      Long id,
      int amount,
      String category,
      String memo,
      LocalDate expenseDate,
      LocalTime expenseTime,
      LocalDateTime createdAt) {
    this.id = id;
    this.amount = amount;
    this.category = category;
    this.memo = memo;
    this.expenseDate = expenseDate;
    this.expenseTime = expenseTime;
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getAmount() {
    return amount;
  }

  public String getCategory() {
    return category;
  }

  public String getMemo() {
    return memo;
  }

  public LocalDate getExpenseDate() {
    return expenseDate;
  }

  public LocalTime getExpenseTime() {
    return expenseTime;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void update(
      int amount,
      String category,
      String memo,
      LocalDate expenseDate,
      LocalTime expenseTime) {
    this.amount = amount;
    this.category = category;
    this.memo = memo;
    this.expenseDate = expenseDate;
    this.expenseTime = expenseTime;
  }

}
