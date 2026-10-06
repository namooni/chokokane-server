package com.example.chokokane.validation.expense;

import com.example.chokokane.dto.expense.ExpenseDateTimeRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExpenseDateTimeValidator
    implements ConstraintValidator<ValidExpenseDateTime, ExpenseDateTimeRequest> {
  @Override
  public boolean isValid(
      ExpenseDateTimeRequest request,
      ConstraintValidatorContext context) {

    if (request.getExpenseDate() == null &&
        request.getExpenseTime() != null) {
      context.disableDefaultConstraintViolation();

      context.buildConstraintViolationWithTemplate(
          "Expense time cannot be set without expense date.")
          .addPropertyNode("expenseTime")
          .addConstraintViolation();

      return false;
    }
    return true;
  }
}
