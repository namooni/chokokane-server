package com.example.chokokane.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequest {

  @NotBlank
  @Size(max = 50)
  private String name;

  @NotBlank
  private String color;
}
