package com.example.chokokane.repository.auth;

import java.time.LocalDateTime;

import com.example.chokokane.model.auth.PasswordResetToken;

public interface PasswordResetTokenRepository {

  PasswordResetToken findByTokenHash(String tokenHash);

  int upsert(PasswordResetToken token);

  int markUsed(
      String tokenHash,
      LocalDateTime now);

  int deleteByUserId(Long userId);
}
