package com.example.chokokane.dto.budget;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record SaveMonthlyBudgetRequest(
    @NotNull(message = "予算額を入力してください。") @PositiveOrZero(message = "予算額は0円以上で入力してください。") Integer amount) {
}
