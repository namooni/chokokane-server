package com.example.chokokane.exception.user;

public class LoginFailedException extends RuntimeException {

  public LoginFailedException(String message) {
    super(message);
  }
}
