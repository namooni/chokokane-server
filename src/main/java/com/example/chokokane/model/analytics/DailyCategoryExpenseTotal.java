package com.example.chokokane.model.analytics;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyCategoryExpenseTotal {

  private LocalDate expenseDate;
  private Long categoryId;
  private String categoryName;
  private String categoryColor;
  private long totalAmount;
}
