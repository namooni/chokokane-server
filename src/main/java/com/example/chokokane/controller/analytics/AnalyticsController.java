package com.example.chokokane.controller.analytics;

import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.chokokane.dto.analytics.AnalyticsResponse;
import com.example.chokokane.service.analytics.AnalyticsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Analytics", description = "분석 관리")
@RestController
public class AnalyticsController {

  private final AnalyticsService analyticsService;

  public AnalyticsController(AnalyticsService analyticsService) {
    this.analyticsService = analyticsService;
  }

  @Operation(summary = "분석 조회", description = "분석을 조회합니다.")
  @GetMapping("/analytics")
  public AnalyticsResponse getAnalytics(
      Authentication authentication,
      @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {

    Long userId = Long.valueOf(authentication.getName());

    return analyticsService.getAnalytics(userId, month);
  }
}
