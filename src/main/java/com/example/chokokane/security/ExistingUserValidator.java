package com.example.chokokane.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import com.example.chokokane.repository.user.UserRepository;

public class ExistingUserValidator implements OAuth2TokenValidator<Jwt> {

  private final UserRepository userRepository;

  public ExistingUserValidator(
      UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public OAuth2TokenValidatorResult validate(Jwt jwt) {
    String subject = jwt.getSubject();

    if (subject == null || subject.isBlank()) {
      return invalid();
    }

    long userId;

    try {
      userId = Long.parseLong(subject);
    } catch (NumberFormatException exception) {
      return invalid();
    }

    if (userId <= 0 || userRepository.findById(userId) == null) {
      return invalid();
    }

    return OAuth2TokenValidatorResult.success();
  }

  private OAuth2TokenValidatorResult invalid() {
    return OAuth2TokenValidatorResult.failure(
        new OAuth2Error(
            "invalid_token",
            "アカウントが見つかりません。",
            null));
  }
}
