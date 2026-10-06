package com.example.chokokane.dto.expense;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ExpenseDateTimeRequest {

  LocalDate getExpenseDate();

  LocalTime getExpenseTime();
}