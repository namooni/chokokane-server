package com.example.chokokane.repository.category;

import java.util.List;

import com.example.chokokane.model.category.Category;

public interface CategoryRepository {

  List<Category> findActiveByUserId(Long userId);

  Category findByIdAndUserId(Long categoryId, Long userId);

  Category findByNameAndUserId(String name, Long userId);

  void save(Category category);

  int update(
      Long categoryId,
      Long userId,
      String name,
      String color);

  int softDelete(Long categoryId, Long userId);

  int restore(Long categoryId, Long userId, String color);
}
