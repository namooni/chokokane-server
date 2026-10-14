package com.example.chokokane.mapper.category;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.chokokane.model.category.Category;

@Mapper
public interface CategoryMapper {

  List<Category> findActiveByUserId(@Param("userId") Long userId);

  Category findByIdAndUserId(
      @Param("categoryId") Long categoryId,
      @Param("userId") Long userId);

  Category findByNameAndUserId(
      @Param("name") String name,
      @Param("userId") Long userId);

  void save(@Param("category") Category category);

  int update(
      @Param("categoryId") Long categoryId,
      @Param("userId") Long userId,
      @Param("name") String name,
      @Param("color") String color);

  int softDelete(
      @Param("categoryId") Long categoryId,
      @Param("userId") Long userId);

  int restore(
      @Param("categoryId") Long categoryId,
      @Param("userId") Long userId,
      @Param("color") String color);
}
