package com.example.chokokane.mapper.auth;

import org.apache.ibatis.annotations.Mapper;

import com.example.chokokane.model.auth.RefreshToken;

@Mapper
public interface RefreshTokenMapper {

  RefreshToken findByUserId(Long userId);

  void save(RefreshToken refreshToken);

  void deleteByUserId(Long userId);
}
