package com.example.chokokane.mapper.budget;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.chokokane.model.budget.MonthlyBudget;

@Mapper
public interface MonthlyBudgetMapper {

  MonthlyBudget findByUserIdAndMonth(
      @Param("userId") Long userId,
      @Param("budgetMonth") LocalDate budgetMonth);

  int upsert(
      @Param("userId") Long userId,
      @Param("budgetMonth") LocalDate budgetMonth,
      @Param("amount") Integer amount);

  int deleteAllByUserId(
      @Param("userId") Long userId);
}
