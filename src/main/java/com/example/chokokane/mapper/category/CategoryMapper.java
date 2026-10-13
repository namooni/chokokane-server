package com.example.chokokane.mapper.category;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.chokokane.model.category.Category;

@Mapper
public interface CategoryMapper {

  List<Category> findActiveByUserId(@Param("userId") Long userId);
}
