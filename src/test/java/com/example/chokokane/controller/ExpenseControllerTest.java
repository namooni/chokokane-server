package com.example.chokokane.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.chokokane.controller.expense.ExpenseController;
import com.example.chokokane.model.expense.Expense;
import com.example.chokokane.service.expense.ExpenseService;

@WebMvcTest(ExpenseController.class)
public class ExpenseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ExpenseService expenseService;

  @Test
  @Disabled
  void getExpense_return200() throws Exception {

    Expense expense = new Expense(
        1L,
        1500,
        "Food",
        "Sushi",
        LocalDate.of(2026, 10, 2),
        LocalTime.of(9, 18),
        LocalDateTime.of(2026, 10, 2, 13, 4, 12));

    when(expenseService.getExpenseById(1L, 1L))
        .thenReturn(expense);

    mockMvc.perform(get("/expense/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.amount").value(1500))
        .andExpect(jsonPath("$.category").value("Food"));
  }

}
