package com.example.chokokane.dto.history;

import java.util.List;

import com.example.chokokane.dto.category.CategorySummaryResponse;
import com.example.chokokane.dto.expense.ExpenseResponse;

public record HistoryResponse(
    String month,
    long monthlyTotal,
    long filteredTotal,
    List<CategorySummaryResponse> availableCategories,
    List<ExpenseResponse> content,
    String nextCursor,
    boolean hasNext) {

}