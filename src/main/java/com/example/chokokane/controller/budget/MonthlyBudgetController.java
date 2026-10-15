package com.example.chokokane.controller.budget;

import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.chokokane.dto.budget.SaveMonthlyBudgetRequest;
import com.example.chokokane.service.budget.MonthlyBudgetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Budget", description = "예산 관리")
@RestController
public class MonthlyBudgetController {

  private MonthlyBudgetService monthlyBudgetService;

  public MonthlyBudgetController(MonthlyBudgetService monthlyBudgetService) {
    this.monthlyBudgetService = monthlyBudgetService;
  }

  @Operation(summary = "", description = "")
  @PutMapping("/budget/{month}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void saveBudget(
      Authentication authentication,
      @PathVariable("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
      @Valid @RequestBody SaveMonthlyBudgetRequest request) {

    Long userId = Long.valueOf(authentication.getName());

    monthlyBudgetService.saveBudget(userId, month, request);
  }
}
