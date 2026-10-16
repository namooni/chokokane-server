package com.example.chokokane.repository.category;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.chokokane.mapper.category.CategoryMapper;
import com.example.chokokane.model.category.Category;

@Repository
public class MyBatisCategoryRepository implements CategoryRepository {

  CategoryMapper categoryMapper;

  public MyBatisCategoryRepository(CategoryMapper categoryMapper) {
    this.categoryMapper = categoryMapper;
  }

  @Override
  public List<Category> findActiveByUserId(Long userId) {
    return categoryMapper.findActiveByUserId(userId);
  }

  @Override
  public Category findByIdAndUserId(Long categoryId, Long userId) {
    return categoryMapper.findByIdAndUserId(categoryId, userId);
  }

  @Override
  public Category findByNameAndUserId(String name, Long userId) {
    return categoryMapper.findByNameAndUserId(name, userId);
  }

  @Override
  public void save(Category category) {
    categoryMapper.save(category);
  }

  @Override
  public int update(Long categoryId, Long userId, String name, String color) {
    return categoryMapper.update(categoryId, userId, name, color);
  }

  @Override
  public int softDelete(Long categoryId, Long userId) {
    return categoryMapper.softDelete(categoryId, userId);
  }

  @Override
  public int restore(Long categoryId, Long userId, String color) {
    return categoryMapper.restore(categoryId, userId, color);
  }

  @Override
  public int deleteAllByUserId(Long userId) {
    return categoryMapper.deleteAllByUserId(userId);
  }
}
