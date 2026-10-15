package com.example.chokokane.dto.analytics;

import java.time.LocalDate;
import java.util.List;

public record DailyExpenseResponse(
    LocalDate date,
    long totalAmount,
    List<CategoryAmountResponse> categories) {
}
