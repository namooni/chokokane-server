package com.example.chokokane.model.expense;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseDetail {

  private Long id;
  private int amount;
  private Long categoryId;
  private String categoryName;
  private String categoryColor;
  private String memo;
  private LocalDate expenseDate;
  private LocalTime expenseTime;
  private LocalDateTime createdAt;
}
