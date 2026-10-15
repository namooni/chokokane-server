package com.example.chokokane.service.budget;

import java.time.YearMonth;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.example.chokokane.dto.budget.SaveMonthlyBudgetRequest;
import com.example.chokokane.exception.BadRequestException;
import com.example.chokokane.model.budget.MonthlyBudget;
import com.example.chokokane.repository.budget.MonthlyBudgetRepository;

@Service
public class MonthlyBudgetService {
  private final MonthlyBudgetRepository monthlyBudgetRepository;

  public MonthlyBudgetService(MonthlyBudgetRepository monthlyBudgetRepository) {
    this.monthlyBudgetRepository = monthlyBudgetRepository;
  }

  public void saveBudget(
      Long userId,
      YearMonth month,
      SaveMonthlyBudgetRequest request) {

    YearMonth currentMonth = YearMonth.now(ZoneId.of("Asia/Tokyo"));

    if (month.isAfter(currentMonth)) {
      throw new BadRequestException("未来の月は指定できません。");
    }

    Integer amount = request.amount();

    if (amount == null || amount < 0) {
      throw new BadRequestException("予算額は0円以上で入力してください。");
    }

    monthlyBudgetRepository.upsert(
        userId,
        month.atDay(1),
        amount);
  }

  public MonthlyBudget getMonthlyBudget(Long userId, YearMonth month) {
    return monthlyBudgetRepository.findByUserIdAndMonth(userId, month.atDay(1));
  }
}
