package com.example.chokokane.service.expense;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.chokokane.common.pagination.PageRequest;
import com.example.chokokane.common.pagination.PageResponse;
import com.example.chokokane.dto.category.CategorySummaryResponse;
import com.example.chokokane.dto.expense.CreateExpenseRequest;
import com.example.chokokane.dto.expense.ExpensePageResponse;
import com.example.chokokane.dto.expense.ExpenseResponse;
import com.example.chokokane.dto.expense.ExpenseSearchRequest;
import com.example.chokokane.dto.expense.UpdateExpenseRequest;
import com.example.chokokane.exception.BadRequestException;
import com.example.chokokane.exception.NotFoundException;
import com.example.chokokane.model.category.Category;
import com.example.chokokane.model.expense.Expense;
import com.example.chokokane.model.expense.ExpenseDetail;
import com.example.chokokane.repository.category.CategoryRepository;
import com.example.chokokane.repository.expense.ExpenseRepository;

@Service
public class ExpenseService {
  private final ExpenseRepository expenseRepository;
  private final CategoryRepository categoryRepository;

  private static final Logger log = LoggerFactory.getLogger(ExpenseService.class);

  public ExpenseService(
      ExpenseRepository expenseRepository,
      CategoryRepository categoryRepository) {
    this.expenseRepository = expenseRepository;
    this.categoryRepository = categoryRepository;
  }

  // C
  public Expense addExpense(Long userId, CreateExpenseRequest request) {
    validateCategory(request.getCategoryId(), userId);
    Expense expense = new Expense(
        null,
        request.getAmount(),
        request.getCategoryId(),
        request.getMemo(),
        request.getExpenseDate(),
        request.getExpenseTime(),
        LocalDateTime.now());
    expenseRepository.save(userId, expense);

    log.info("Expense created. id={}, amount={}",
        expense.getId(),
        expense.getAmount());

    return expense;
  }

  // R
  public ExpensePageResponse getExpenses(
      Long userId,
      ExpenseSearchRequest request) {

    PageRequest pageRequest = new PageRequest(
        request.getPage(),
        request.getSize());

    int offset = pageRequest.getOffset();

    int totalElements = expenseRepository.getTotalElements(
        userId,
        request.getCategoryIds(),
        request.getMinAmount(),
        request.getMaxAmount());

    int totalAmount = expenseRepository.getTotalAmount(
        userId,
        request.getCategoryIds(),
        request.getMinAmount(),
        request.getMaxAmount());

    List<ExpenseDetail> expenses = expenseRepository.findAll(
        userId,
        request.getCategoryIds(),
        request.getMinAmount(),
        request.getMaxAmount(),
        request.getSort(),
        offset,
        request.getSize());

    List<ExpenseResponse> responses = expenses.stream()
        .map(expense -> new ExpenseResponse(
            expense.getId(),
            expense.getAmount(),
            new CategorySummaryResponse(
                expense.getCategoryId(),
                expense.getCategoryName(),
                expense.getCategoryColor()),
            expense.getMemo(),
            expense.getExpenseDate(),
            expense.getExpenseTime(),
            expense.getCreatedAt()))
        .toList();

    return new ExpensePageResponse(
        new PageResponse<>(responses, pageRequest, totalElements),
        totalAmount);
  }

  public Expense getExpenseById(Long userId, Long id) {
    Expense expense = expenseRepository.findById(userId, id);

    if (expense == null) {
      throw new NotFoundException("Expense not found.");
    }

    return expense;
  }

  // U
  public Expense updateExpense(Long userId, Long id, UpdateExpenseRequest request) {
    Expense expense = getExpenseById(userId, id);
    if (request.getCategoryId() != null
        && !request.getCategoryId().equals(expense.getCategoryId())) {
      validateCategory(request.getCategoryId(), userId);
    }

    expense.update(
        request.getAmount(),
        request.getCategoryId(),
        request.getMemo(),
        request.getExpenseDate(),
        request.getExpenseTime());

    expenseRepository.update(userId, expense);
    return expense;
  }

  // D
  public void deleteExpense(Long userId, Long id) {
    getExpenseById(userId, id);
    expenseRepository.deleteById(userId, id);
  }

  private void validateCategory(Long categoryId, Long userId) {
    Category category = categoryRepository.findByIdAndUserId(
        categoryId,
        userId);

    if (category == null) {
      throw new NotFoundException("Category not found.");
    }

    if (category.getDeletedAt() != null) {
      throw new BadRequestException("Deleted category cannot be selected.");
    }
  }
}
