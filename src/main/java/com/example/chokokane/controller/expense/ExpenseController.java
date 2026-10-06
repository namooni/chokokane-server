package com.example.chokokane.controller.expense;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.chokokane.dto.expense.CreateExpenseRequest;
import com.example.chokokane.dto.expense.ExpensePageResponse;
import com.example.chokokane.dto.expense.UpdateCategoryRequest;
import com.example.chokokane.dto.expense.UpdateExpenseRequest;
import com.example.chokokane.model.expense.Expense;
import com.example.chokokane.service.expense.ExpenseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Tag(name = "Expense", description = "지출 관리 API")
@RestController
public class ExpenseController {

  private ExpenseService expenseService;

  public ExpenseController(ExpenseService expenseService) {
    this.expenseService = expenseService;
  }

  @Operation(summary = "지출 생성", description = "지출을 생성합니다.")
  @PostMapping("/expenses")
  public Expense addExpense(
      Authentication authentication,
      @RequestBody @Valid CreateExpenseRequest request) {
    Long userId = Long.valueOf(authentication.getName());
    return expenseService.addExpense(userId, request);
  }

  @Operation(summary = "지출 전체 조회", description = "모든 지출을 조회합니다.")
  @GetMapping("/expenses")
  public ExpensePageResponse getExpenses(
      Authentication authentication,
      @RequestParam(name = "categories", required = false) List<String> categories,
      @RequestParam(name = "min_amount", required = false) Integer minAmount,
      @RequestParam(name = "max_amount", required = false) Integer maxAmount,
      @RequestParam(name = "sort", required = false) String sort,
      @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero int page,
      @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
    Long userId = Long.valueOf(authentication.getName());

    return expenseService.getExpenses(
        userId,
        categories,
        minAmount,
        maxAmount,
        sort,
        page,
        size);
  }

  @Operation(summary = "지출 단건 조회", description = "ID로 지출 하나를 조회합니다.")
  @GetMapping("/expense/{id}")
  public Expense getExpenseById(
      Authentication authentication,
      @PathVariable("id") Long id) {
    Long userId = Long.valueOf(authentication.getName());
    return expenseService.getExpenseById(userId, id);
  }

  @Operation(summary = "지출 단건 수정", description = "지출을 수정합니다.")
  @PutMapping("/expenses/{id}")
  public Expense updateExpense(
      Authentication authentication,
      @PathVariable("id") Long id,
      @RequestBody @Valid UpdateExpenseRequest request) {
    Long userId = Long.valueOf(authentication.getName());
    return expenseService.updateExpense(userId, id, request);
  }

  @Operation(summary = "미사용", description = "미사용")
  @PutMapping("/expenses_category")
  public void updateCategory(
      @RequestBody UpdateCategoryRequest request) {
    expenseService.updateCategory(request);
  }

  @Operation(summary = "지출 삭제", description = "ID로 지출을 삭제합니다.")
  @DeleteMapping("/expenses/{id}")
  public void deleteExpense(
      Authentication authentication,
      @PathVariable("id") Long id) {
    Long userId = Long.valueOf(authentication.getName());
    expenseService.deleteExpense(userId, id);
  }
}
