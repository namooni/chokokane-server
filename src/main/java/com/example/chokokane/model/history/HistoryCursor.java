package com.example.chokokane.model.history;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record HistoryCursor(
    LocalDate expenseDate,
    LocalTime expenseTime,
    LocalDateTime createdAt,
    Long id) {

}
