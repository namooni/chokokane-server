package com.example.chokokane.repository.auth;

import org.springframework.stereotype.Repository;

import com.example.chokokane.mapper.auth.RefreshTokenMapper;
import com.example.chokokane.model.auth.RefreshToken;

@Repository
public class MyBatisRefreshTokenRepository
    implements RefreshTokenRepository {

  private RefreshTokenMapper refreshTokenMapper;

  public MyBatisRefreshTokenRepository(
      RefreshTokenMapper refreshTokenMapper) {
    this.refreshTokenMapper = refreshTokenMapper;
  }

  @Override
  public RefreshToken findByUserId(Long userId) {
    return refreshTokenMapper.findByUserId(userId);
  }

  @Override
  public void save(RefreshToken refreshToken) {
    refreshTokenMapper.save(refreshToken);
  }

  @Override
  public void deleteByUserId(Long userId) {
    refreshTokenMapper.deleteByUserId(userId);
  }
}
