package com.example.chokokane.service.auth;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.chokokane.model.auth.PasswordResetToken;
import com.example.chokokane.repository.auth.PasswordResetTokenRepository;
import com.example.chokokane.security.PasswordResetTokenProvider;

@Service
public class PasswordResetTokenService {

  private final PasswordResetTokenRepository repository;
  private final PasswordResetTokenProvider tokenProvider;

  public PasswordResetTokenService(
      PasswordResetTokenRepository repository,
      PasswordResetTokenProvider tokenProvider) {
    this.repository = repository;
    this.tokenProvider = tokenProvider;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public String issue(Long userId) {
    if (userId == null || userId <= 0) {
      throw new IllegalArgumentException("Valid user ID is required.");
    }

    String rawToken = tokenProvider.generate();
    String tokenHash = tokenProvider.hash(rawToken);

    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

    PasswordResetToken token = new PasswordResetToken(
        null,
        userId,
        tokenHash,
        now,
        now.plusMinutes(30),
        null);

    int affected = repository.upsert(token);

    if (affected <= 0) {
      throw new IllegalStateException("Password reset token save failed.");
    }

    return rawToken;
  }
}
