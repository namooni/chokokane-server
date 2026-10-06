package com.example.chokokane.exception.expense;

public class ExpenseNotFoundException extends RuntimeException {

  public ExpenseNotFoundException(String message) {
    super(message);
  }
}
