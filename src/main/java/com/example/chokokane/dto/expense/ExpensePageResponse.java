package com.example.chokokane.dto.expense;

import com.example.chokokane.common.pagination.PageResponse;

public class ExpensePageResponse {

  private PageResponse<ExpenseResponse> pagination;
  private int totalAmount;

  public ExpensePageResponse(
      PageResponse<ExpenseResponse> pagination,
      int totalAmount) {
    this.pagination = pagination;
    this.totalAmount = totalAmount;
  }

  public PageResponse<ExpenseResponse> getPagination() {
    return pagination;
  }

  public int getTotalAmount() {
    return totalAmount;
  }
}
