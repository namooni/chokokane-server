package com.example.chokokane.dto.expense;

import java.util.List;

import org.springframework.web.bind.annotation.BindParam;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseSearchRequest {

  private List<Long> categoryIds;

  @BindParam("min_amount")
  private Integer minAmount;

  @BindParam("max_amount")
  private Integer maxAmount;
  private String sort;

  @PositiveOrZero
  private int page = 0;

  @Min(1)
  @Max(100)
  private int size = 10;
}
