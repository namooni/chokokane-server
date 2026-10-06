package com.example.chokokane.model.auth;

import java.time.LocalDateTime;

public class RefreshToken {
  private Long id;
  private Long userId;
  private String token;
  private LocalDateTime expiresAt;

  public RefreshToken(
      Long id,
      Long userId,
      String token,
      LocalDateTime expiresAt) {
    this.id = id;
    this.userId = userId;
    this.token = token;
    this.expiresAt = expiresAt;
  }

  public Long getId() {
    return id;
  }

  public Long getUserId() {
    return userId;
  }

  public String getToken() {
    return token;
  }

  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }
}
