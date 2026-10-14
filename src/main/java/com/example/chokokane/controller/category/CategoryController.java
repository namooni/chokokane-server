package com.example.chokokane.controller.category;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.chokokane.dto.category.CategoryResponse;
import com.example.chokokane.dto.category.CreateCategoryRequest;
import com.example.chokokane.dto.category.UpdateCategoryRequest;
import com.example.chokokane.service.category.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Category", description = "카테고리 관리")
@RestController
public class CategoryController {
  private CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @Operation(summary = "카테고리 전체 조회", description = "자신의 카테고리를 전체 조회합니다.")
  @GetMapping("/categories")
  public List<CategoryResponse> getCategories(Authentication authentication) {
    Long userId = Long.valueOf(authentication.getName());

    return categoryService.getCategories(userId);
  }

  @Operation(summary = "카테고리 생성", description = "카테고리를 생성합니다.")
  @PostMapping("/categories")
  public CategoryResponse addCategory(
      Authentication authentication,
      @RequestBody @Valid CreateCategoryRequest request) {

    Long userId = Long.valueOf(authentication.getName());
    return categoryService.createCategory(userId, request);
  }

  @Operation(summary = "카테고리 수정", description = "카테고리를 수정합니다.")
  @PatchMapping("/categories/{categoryId}")
  public CategoryResponse updateCategory(
      Authentication authentication,
      @PathVariable("categoryId") Long categoryId,
      @RequestBody @Valid UpdateCategoryRequest request) {

    Long userId = Long.valueOf(authentication.getName());

    return categoryService.updateCategory(categoryId, userId, request);
  }

  @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/categories/{categoryId}")
  public void deleteCategory(
      Authentication authentication,
      @PathVariable("categoryId") Long categoryId) {
    Long userId = Long.valueOf(authentication.getName());

    categoryService.deleteCategory(userId, categoryId);
  }
}
