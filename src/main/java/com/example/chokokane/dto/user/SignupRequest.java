package com.example.chokokane.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SignupRequest {

  @NotBlank(message = "name is required.")
  private String name;
  @NotBlank(message = "email is required.")
  @Email(message = "email format is invalid.")
  private String email;
  @NotBlank(message = "password is required.")
  private String password;

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }
}
