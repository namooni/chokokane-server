package com.example.chokokane.mapper.expense;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.example.chokokane.model.expense.Expense;

@Mapper
public interface ExpenseMapper {

  int save(
      @Param("userId") Long userId,
      @Param("expense") Expense expense);

  List<Expense> findAll(
      @Param("userId") Long userId,
      @Param("categories") List<String> categories,
      @Param("minAmount") Integer minAmount,
      @Param("maxAmount") Integer maxAmount,
      @Param("sort") String sort,
      @Param("offset") int offset,
      @Param("size") int size);

  int getExpenseCount(
      @Param("userId") Long userId,
      @Param("categories") List<String> categories,
      @Param("minAmount") Integer minAmount,
      @Param("maxAmount") Integer maxAmount);

  int getTotalAmount(
      @Param("userId") Long userId,
      @Param("categories") List<String> categories,
      @Param("minAmount") Integer minAmount,
      @Param("maxAmount") Integer maxAmount);

  Expense findById(
      @Param("userId") Long userId,
      @Param("id") Long id);

  int update(
      @Param("userId") Long userId,
      @Param("expense") Expense expense);

  @Update("""
      UPDATE expenses
      SET category = #{newCategory}
      WHERE category = #{oldCategory}
          """)
  int updateCategory(
      @Param("oldCategory") String oldCategory,
      @Param("newCategory") String newCategory);

  int delete(
      @Param("userId") Long userId,
      @Param("id") Long id);
}
