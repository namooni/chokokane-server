package com.example.chokokane.dto.category;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCategoryRequest {

  @Size(max = 50)
  @Pattern(regexp = "(?s).*[\\S&&[^\\u3000]].*")
  private String name;
  private String color;

}
