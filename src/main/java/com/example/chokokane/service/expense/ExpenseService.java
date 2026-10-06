package com.example.chokokane.service.expense;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.chokokane.dto.expense.CreateExpenseRequest;
import com.example.chokokane.dto.expense.ExpensePageResponse;
import com.example.chokokane.dto.expense.UpdateCategoryRequest;
import com.example.chokokane.dto.expense.UpdateExpenseRequest;
import com.example.chokokane.exception.expense.ExpenseNotFoundException;
import com.example.chokokane.model.expense.Expense;
import com.example.chokokane.repository.expense.ExpenseRepository;

@Service
public class ExpenseService {
  private ExpenseRepository expenseRepository;

  private static final Logger log = LoggerFactory.getLogger(ExpenseService.class);

  public ExpenseService(ExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  // C
  public Expense addExpense(Long userId, CreateExpenseRequest request) {
    Expense expense = new Expense(
        null,
        request.getAmount(),
        request.getCategory(),
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
      List<String> categories,
      Integer minAmount,
      Integer maxAmount,
      String sort,
      int page,
      int size) {
    int offset = page * size;

    int expenseCount = expenseRepository.getExpenseCount(
        userId,
        categories,
        minAmount,
        maxAmount);

    int totalAmount = expenseRepository.getTotalAmount(
        userId,
        categories,
        minAmount,
        maxAmount);

    int totalPage = (int) Math.ceil((double) expenseCount / size);

    List<Expense> expenses = expenseRepository.findAll(
        userId,
        categories,
        minAmount,
        maxAmount,
        sort,
        offset,
        size);

    ExpensePageResponse expensePageResponse = new ExpensePageResponse(
        expenses,
        page,
        size,
        expenseCount,
        totalPage,
        totalAmount);

    return expensePageResponse;
  }

  public Expense getExpenseById(Long userId, Long id) {
    Expense expense = expenseRepository.findById(userId, id);

    if (expense == null) {
      throw new ExpenseNotFoundException("Expense not found.");
    }

    return expense;
  }

  // U
  public Expense updateExpense(Long userId, Long id, UpdateExpenseRequest request) {
    Expense expense = getExpenseById(userId, id);

    if (expense == null) {
      throw new ExpenseNotFoundException("Expense not found.");
    }

    expense.update(
        request.getAmount(),
        request.getCategory(),
        request.getMemo(),
        request.getExpenseDate(),
        request.getExpenseTime());

    expenseRepository.update(userId, expense);
    return expense;
  }

  public void updateCategory(UpdateCategoryRequest request) {
    expenseRepository.updateCategory(
        request.getOldCategory(),
        request.getNewCategory());
  }

  // D
  public void deleteExpense(Long userId, Long id) {
    getExpenseById(userId, id);
    expenseRepository.deleteById(userId, id);
  }
}
