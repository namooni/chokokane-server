package com.example.chokokane.dto.expense;

import java.util.List;

import com.example.chokokane.model.expense.Expense;

public class ExpensePageResponse {

  private List<Expense> content;
  private int page;
  private int size;
  private int totalElements;
  private int totalPages;
  private int totalAmount;

  public ExpensePageResponse(
      List<Expense> content,
      int page,
      int size,
      int totalElements,
      int totalPages,
      int totalAmount) {
    this.content = content;
    this.page = page;
    this.size = size;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
    this.totalAmount = totalAmount;
  }

  public List<Expense> getContent() {
    return content;
  }

  public int getPage() {
    return page;
  }

  public int getSize() {
    return size;
  }

  public int getTotalElements() {
    return totalElements;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public int getTotalAmount() {
    return totalAmount;
  }
}
