package com.example.chokokane.repository.auth;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import com.example.chokokane.mapper.auth.PasswordResetTokenMapper;
import com.example.chokokane.model.auth.PasswordResetToken;

@Repository
public class MyBatisPasswordResetTokenRepository implements PasswordResetTokenRepository {

  private final PasswordResetTokenMapper passwordResetTokenMapper;

  public MyBatisPasswordResetTokenRepository(
      PasswordResetTokenMapper passwordResetTokenMapper) {
    this.passwordResetTokenMapper = passwordResetTokenMapper;
  }

  @Override
  public PasswordResetToken findByTokenHash(String tokenHash) {
    return passwordResetTokenMapper.findByTokenHash(tokenHash);
  }

  @Override
  public int upsert(PasswordResetToken token) {
    return passwordResetTokenMapper.upsert(token);
  }

  @Override
  public int markUsed(
      String tokenHash,
      LocalDateTime now) {
    return passwordResetTokenMapper.markUsed(tokenHash, now);
  }

  @Override
  public int deleteByUserId(Long userId) {
    return passwordResetTokenMapper.deleteByUserId(userId);
  }
}
