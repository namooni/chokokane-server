package com.example.chokokane.repository.expense;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.chokokane.mapper.expense.ExpenseMapper;
import com.example.chokokane.model.expense.Expense;

@Repository
public class MyBatisExpenseRepository implements ExpenseRepository {

  private ExpenseMapper expenseMapper;

  public MyBatisExpenseRepository(ExpenseMapper expenseMapper) {
    this.expenseMapper = expenseMapper;
  }

  @Override
  public List<Expense> findAll(
      Long userId,
      List<String> categories,
      Integer minAmount,
      Integer maxAmount,
      String sort,
      int offset,
      int size) {
    return expenseMapper.findAll(
        userId,
        categories,
        minAmount,
        maxAmount,
        sort,
        offset,
        size);
  }

  @Override
  public int getExpenseCount(
      Long userId,
      List<String> categories,
      Integer minAmount,
      Integer maxAmount) {
    return expenseMapper.getExpenseCount(
        userId,
        categories,
        minAmount,
        maxAmount);
  }

  @Override
  public int getTotalAmount(
      Long userId,
      List<String> categories,
      Integer minAmount,
      Integer maxAmount) {
    return expenseMapper.getTotalAmount(
        userId,
        categories,
        minAmount,
        maxAmount);
  }

  @Override
  public Expense findById(Long userId, Long id) {
    return expenseMapper.findById(userId, id);
  }

  @Override
  public Expense save(Long userId, Expense expense) {
    expenseMapper.save(userId, expense);
    return expense;
  }

  @Override
  public int update(Long userId, Expense expense) {
    return expenseMapper.update(userId, expense);
  }

  @Override
  public int updateCategory(String oldCategory, String newCategory) {
    return expenseMapper.updateCategory(oldCategory, newCategory);
  }

  @Override
  public int deleteById(Long userId, Long id) {
    return expenseMapper.delete(userId, id);
  }
}
