package com.example.chokokane.config;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class JwtConfig {

  private final String secret;

  public JwtConfig(@Value("${jwt.secret}") String secret) {
    this.secret = secret;
  }

  private SecretKey getSecretKey() {
    byte[] keyBytes = Base64.getDecoder().decode(secret);

    return new SecretKeySpec(keyBytes, "HmacSHA256");
  }

  @Bean
  JwtEncoder jwtEncoder() {
    SecretKey key = getSecretKey();

    return NimbusJwtEncoder.withSecretKey(key).build();
  }

  @Bean
  @Primary
  JwtDecoder jwtDecoder() {
    SecretKey key = getSecretKey();

    NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key).build();

    OAuth2TokenValidator<Jwt> defaultValidator = JwtValidators.createDefault();
    OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
        defaultValidator,
        new AccessTokenValidator());

    decoder.setJwtValidator(validator);

    return decoder;
  }

  @Bean
  JwtDecoder refreshJwtDecoder() {
    SecretKey key = getSecretKey();

    NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key).build();

    OAuth2TokenValidator<Jwt> defaultValidator = JwtValidators.createDefault();
    OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
        defaultValidator,
        new RefreshTokenValidator());

    decoder.setJwtValidator(validator);

    return decoder;
  }
}
