package com.example.chokokane.service.category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.chokokane.dto.category.CategoryResponse;
import com.example.chokokane.dto.category.CreateCategoryRequest;
import com.example.chokokane.dto.category.UpdateCategoryRequest;
import com.example.chokokane.exception.BadRequestException;
import com.example.chokokane.exception.ConflictException;
import com.example.chokokane.exception.NotFoundException;
import com.example.chokokane.model.category.Category;
import com.example.chokokane.repository.category.CategoryRepository;

@Service
public class CategoryService {
  private static final Set<String> ALLOWED_COLORS = Set.of(
      "#E89A64",
      "#5BAF98",
      "#65A7D8",
      "#A88CD8",
      "#DCA8B5",
      "#F2C46F",
      "#6DAFC0",
      "#7B96D2",
      "#9BAF74",
      "#D78D79",
      "#9D9FA8",
      "#D4A76C");

  private static final List<String> DEFAULT_CATEGORIES = List.of(
      "食費",
      "交通",
      "日用品",
      "趣味");

  private static void requestValidator(String trimmedName, String color) {
    if (trimmedName == null || trimmedName.isEmpty() || trimmedName.length() > 50) {
      throw new BadRequestException("Invalid category name.");
    }

    if (!ALLOWED_COLORS.contains(color)) {
      throw new BadRequestException("Invalid category color.");
    }

  }

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public List<CategoryResponse> getCategories(Long userId) {
    return categoryRepository.findActiveByUserId(userId)
        .stream()
        .map(category -> new CategoryResponse(
            category.getId(),
            category.getName(),
            category.getColor(),
            category.getIsSystem()))
        .toList();

  }

  @Transactional
  public CategoryResponse createCategory(
      Long userId,
      CreateCategoryRequest request) {

    String name = request.getName();
    String color = request.getColor();

    String trimmedName = name == null ? null : name.replaceAll("^[\\s\\u3000]+|[\\s\\u3000]+$", "");

    requestValidator(trimmedName, color);

    Category existingCategory = categoryRepository.findByNameAndUserId(trimmedName, userId);

    if (existingCategory != null) {

      if (existingCategory.getDeletedAt() != null) {
        int affectedRows = categoryRepository.restore(existingCategory.getId(), userId, color);

        if (affectedRows != 1) {
          throw new IllegalStateException("Failed to restore category.");
        }

        return new CategoryResponse(
            existingCategory.getId(),
            trimmedName,
            color,
            existingCategory.getIsSystem());
      }

      throw new ConflictException("already has category.");
    }

    Category createCategory = new Category(
        null,
        userId,
        trimmedName,
        color,
        false,
        null,
        null);

    categoryRepository.save(createCategory);

    return new CategoryResponse(
        createCategory.getId(),
        createCategory.getName(),
        createCategory.getColor(),
        createCategory.getIsSystem());
  }

  public void createDefaultCategories(Long userId) {
    Category createSystemCategory = new Category(
        null,
        userId,
        "未分類",
        "#9D9FA8",
        true,
        null,
        null);

    categoryRepository.save(createSystemCategory);

    List<String> colors = new ArrayList<>(ALLOWED_COLORS);
    colors.remove("#9D9FA8");
    Collections.shuffle(colors);

    for (int i = 0; i < DEFAULT_CATEGORIES.size(); i++) {
      Category category = new Category(
          null,
          userId,
          DEFAULT_CATEGORIES.get(i),
          colors.get(i),
          false,
          null,
          null);

      categoryRepository.save(category);
    }
  }

  @Transactional
  public CategoryResponse updateCategory(
      Long categoryId,
      Long userId,
      UpdateCategoryRequest request) {

    if (request.getName() == null && request.getColor() == null) {
      throw new BadRequestException("No fields to update.");
    }

    Category existingCategory = categoryRepository.findByIdAndUserId(categoryId, userId);

    if (existingCategory == null || existingCategory.getDeletedAt() != null) {
      throw new NotFoundException("Not found category.");
    }

    if (existingCategory.getIsSystem()) {
      throw new ConflictException("Is System Category.");
    }

    String name = request.getName() == null
        ? existingCategory.getName()
        : request.getName();

    String color = request.getColor() == null
        ? existingCategory.getColor()
        : request.getColor();

    String trimmedName = name.replaceAll("^[\\s\\u3000]+|[\\s\\u3000]+$", "");

    requestValidator(trimmedName, color);

    Category duplicateCategory = categoryRepository.findByNameAndUserId(trimmedName, userId);

    if (duplicateCategory != null
        && !duplicateCategory.getId().equals(categoryId)) {
      throw new ConflictException("already has category.");
    }

    int affectedRows = categoryRepository.update(
        categoryId,
        userId,
        trimmedName,
        color);

    if (affectedRows != 1) {
      throw new IllegalStateException("Failed to update category.");
    }

    return new CategoryResponse(
        categoryId,
        trimmedName,
        color,
        existingCategory.getIsSystem());
  }

  @Transactional
  public void deleteCategory(
      Long userId,
      Long categoryId) {

    Category existingCategory = categoryRepository.findByIdAndUserId(categoryId, userId);

    if (existingCategory == null || existingCategory.getDeletedAt() != null) {
      throw new NotFoundException("Not found category.");
    }

    if (existingCategory.getIsSystem()) {
      throw new ConflictException("Is System category.");
    }

    int affectedRows = categoryRepository.softDelete(categoryId, userId);

    if (affectedRows != 1) {
      throw new IllegalStateException("Failed to delete category.");
    }
  }

}
