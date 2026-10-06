package com.example.chokokane.dto.expense;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.chokokane.validation.expense.ValidExpenseDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@ValidExpenseDateTime
public class CreateExpenseRequest
    implements ExpenseDateTimeRequest {

  @Positive(message = "Amount must be greater than 0.")
  private int amount;

  @NotBlank(message = "Category is required.")
  private String category;
  private String memo;

  private LocalDate expenseDate;
  private LocalTime expenseTime;

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
}
