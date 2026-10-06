package com.example.chokokane.repository.auth;

import com.example.chokokane.model.auth.RefreshToken;

public interface RefreshTokenRepository {

  RefreshToken findByUserId(Long userId);

  void save(RefreshToken refreshToken);

  void deleteByUserId(Long userId);
}
