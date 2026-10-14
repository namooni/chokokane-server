package com.example.chokokane.dto.expense;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.chokokane.dto.category.CategorySummaryResponse;

public record ExpenseResponse(
    Long id,
    int amount,
    CategorySummaryResponse category,
    String memo,
    LocalDate expenseDate,
    LocalTime expenseTime,
    LocalDateTime createdAt) {

}
