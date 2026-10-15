package com.example.chokokane.dto.dashboard;

import java.util.List;

import com.example.chokokane.dto.expense.ExpenseResponse;

public record DashboardResponse(
    String month,
    Long budget,
    long monthlySpent,
    Long remainingBudget,
    Long dailySpendable,
    Long todaySpent,
    Long todayAllowance,
    DashboardStatus status,
    List<ExpenseResponse> recentExpenses) {
}
