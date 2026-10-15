package com.example.chokokane.model.analytics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryExpenseTotal {

  private Long categoryId;
  private String categoryName;
  private String categoryColor;
  private long totalAmount;
}
