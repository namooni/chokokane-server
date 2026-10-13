package com.example.chokokane.mapper.category;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.chokokane.model.category.Category;

@SpringBootTest
@ActiveProfiles("local")
public class CategoryMapperTest {

  @Autowired
  private CategoryMapper categoryMapper;

  @Test
  void findActiveByUserId() {
    List<Category> categories = categoryMapper.findActiveByUserId(7L);

    for (Category category : categories) {
      System.out.println("this!!!!!!!!!!!!!!!!!!!!" + category.getName());
      System.out.println("222222222222!!!!!!!!!!!!" + category.getColor());
    }
    assertEquals("食費", categories.get(0).getName());
  }

}