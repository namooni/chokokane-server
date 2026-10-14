package com.example.chokokane.mapper.expense;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.chokokane.model.expense.Expense;
import com.example.chokokane.model.expense.ExpenseDetail;

@Mapper
public interface ExpenseMapper {

  int save(
      @Param("userId") Long userId,
      @Param("expense") Expense expense);

  List<ExpenseDetail> findAll(
      @Param("userId") Long userId,
      @Param("categoryIds") List<Long> categoryIds,
      @Param("minAmount") Integer minAmount,
      @Param("maxAmount") Integer maxAmount,
      @Param("sort") String sort,
      @Param("offset") int offset,
      @Param("size") int size);

  int getTotalElements(
      @Param("userId") Long userId,
      @Param("categoryIds") List<Long> categoryIds,
      @Param("minAmount") Integer minAmount,
      @Param("maxAmount") Integer maxAmount);

  int getTotalAmount(
      @Param("userId") Long userId,
      @Param("categoryIds") List<Long> categoryIds,
      @Param("minAmount") Integer minAmount,
      @Param("maxAmount") Integer maxAmount);

  Expense findById(
      @Param("userId") Long userId,
      @Param("id") Long id);

  int update(
      @Param("userId") Long userId,
      @Param("expense") Expense expense);

  int delete(
      @Param("userId") Long userId,
      @Param("id") Long id);
}
