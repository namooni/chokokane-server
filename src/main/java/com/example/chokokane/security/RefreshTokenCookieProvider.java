package com.example.chokokane.security;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenCookieProvider {

  private final boolean secure;
  private final String sameSite;

  public RefreshTokenCookieProvider(
      @Value("${app.cookie.secure}") boolean secure,
      @Value("${app.cookie.same-site}") String sameSite) {
    this.secure = secure;
    this.sameSite = sameSite;
  }

  public ResponseCookie create(String refreshToken) {
    return ResponseCookie.from(
        "refreshToken",
        refreshToken)
        .httpOnly(true)
        .secure(this.secure)
        .sameSite(this.sameSite)
        .path("/")
        .maxAge(Duration.ofDays(30))
        .build();

  }

  public ResponseCookie delete() {
    return ResponseCookie.from(
        "refreshToken",
        "")
        .httpOnly(true)
        .secure(this.secure)
        .sameSite(this.sameSite)
        .path("/")
        .maxAge(0)
        .build();
  }
}
