package com.example.chokokane.validation.expense;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExpenseDateTimeValidator.class)
public @interface ValidExpenseDateTime {

  String message() default "Expense time cannot be set without expense date.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
