package com.example.chokokane.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.chokokane.exception.expense.ExpenseNotFoundException;
import com.example.chokokane.model.expense.Expense;
import com.example.chokokane.repository.expense.ExpenseRepository;
import com.example.chokokane.service.expense.ExpenseService;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

  @Mock
  private ExpenseRepository expenseRepository;

  private ExpenseService expenseService;

  @BeforeEach
  void setUp() {
    expenseService = new ExpenseService(expenseRepository);
  }

  @Test
  @Disabled
  void getExpense_returnExpense() {
    Expense expense = new Expense(
        1L,
        1500,
        "Food",
        "Sushi",
        LocalDate.of(2026, 10, 1),
        LocalTime.of(17, 57),
        LocalDateTime.of(2026, 10, 1, 13, 4, 12));

    when(expenseRepository.findById(1L, 1L))
        .thenReturn(expense);

    Expense result = expenseService.getExpenseById(1L, 1L);

    assertEquals(expense, result);
  }

  @Test
  void getExpense_throwsException() {

    when(expenseRepository.findById(1L, 2L))
        .thenReturn(null);

    assertThrows(
        ExpenseNotFoundException.class,
        () -> expenseService.getExpenseById(1L, 2L));
  }
}
