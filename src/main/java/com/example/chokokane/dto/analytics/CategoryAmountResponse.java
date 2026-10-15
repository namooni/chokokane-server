package com.example.chokokane.dto.analytics;

import com.example.chokokane.dto.category.CategorySummaryResponse;

public record CategoryAmountResponse(
    CategorySummaryResponse category,
    long amount) {

}
