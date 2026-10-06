package com.example.chokokane.dto.auth;

public class RefreshResult {

  private String accessToken;
  private String refreshToken;

  public RefreshResult(
      String accessToken,
      String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }
}
