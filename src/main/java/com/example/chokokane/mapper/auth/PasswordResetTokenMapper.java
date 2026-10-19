package com.example.chokokane.mapper.auth;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.chokokane.model.auth.PasswordResetToken;

@Mapper
public interface PasswordResetTokenMapper {

  PasswordResetToken findByTokenHash(
      @Param("tokenHash") String tokenHash);

  int upsert(
      @Param("token") PasswordResetToken token);

  int markUsed(
      @Param("tokenHash") String tokenHash,
      @Param("now") LocalDateTime now);

  int deleteByUserId(
      @Param("userId") Long userId);
}
