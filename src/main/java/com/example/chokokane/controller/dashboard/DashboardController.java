package com.example.chokokane.controller.dashboard;

import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.chokokane.dto.dashboard.DashboardResponse;
import com.example.chokokane.service.dashboard.DashboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Dashboard", description = "대시보드 관리")
@RestController
public class DashboardController {

  private final DashboardService dashboardService;

  public DashboardController(DashboardService dashboardService) {
    this.dashboardService = dashboardService;
  }

  @Operation(summary = "대시보드 조회", description = "대시보드를 조회합니다.")
  @GetMapping("/dashboard")
  public DashboardResponse getDashboard(
      Authentication authentication,
      @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
    Long userId = Long.valueOf(authentication.getName());

    return dashboardService.getDashboard(userId, month);
  }
}
