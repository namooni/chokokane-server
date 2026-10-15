package com.example.chokokane.dto.analytics;

import java.util.List;

public record AnalyticsResponse(
    String month,
    Long budget,
    long monthlySpent,
    Long remainingBudget,
    Long dailySpendable,
    long undatedSpent,
    List<DailyExpenseResponse> dailyExpenses,
    List<CategoryAmountResponse> categoryTotals) {

}
