package com.example.chokokane.dto.expense;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.chokokane.validation.expense.ValidExpenseDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@ValidExpenseDateTime
public class CreateExpenseRequest
    implements ExpenseDateTimeRequest {

  @NotNull(message = "Amount is required.")
  @Positive(message = "Amount must be greater than 0.")
  private int amount;

  @NotNull(message = "Category is required.")
  @Positive(message = "Category ID must be greater than 0.")
  private Long categoryId;
  private String memo;

  private LocalDate expenseDate;
  private LocalTime expenseTime;

  public int getAmount() {
    return amount;
  }

  public Long getCategoryId() {
    return categoryId;
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
}
