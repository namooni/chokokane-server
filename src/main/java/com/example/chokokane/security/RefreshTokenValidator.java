package com.example.chokokane.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class RefreshTokenValidator implements OAuth2TokenValidator<Jwt> {

  @Override
  public OAuth2TokenValidatorResult validate(Jwt jwt) {

    String type = jwt.getClaimAsString("type");

    if ("refresh".equals(type)) {
      return OAuth2TokenValidatorResult.success();
    }

    OAuth2Error error = new OAuth2Error(
        "invalid_token",
        "Token type must be refresh.",
        null);

    return OAuth2TokenValidatorResult.failure(error);
  }
}
