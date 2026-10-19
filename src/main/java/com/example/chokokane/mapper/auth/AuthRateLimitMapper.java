package com.example.chokokane.mapper.auth;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.chokokane.model.auth.AuthRateLimitKey;

@Mapper
public interface AuthRateLimitMapper {

  int ensureKey(
      @Param("scope") String scope,
      @Param("subjectHash") String subjectHash);

  AuthRateLimitKey findForUpdate(
      @Param("scope") String scope,
      @Param("subjectHash") String subjectHash);

  long countEventsSince(
      @Param("keyId") Long keyId,
      @Param("since") LocalDateTime since);

  LocalDateTime findLastEventAt(
      @Param("keyId") Long keyId);

  int insertEvent(
      @Param("keyId") Long keyId,
      @Param("createdAt") LocalDateTime createdAt);
}
