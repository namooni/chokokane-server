package com.example.chokokane.repository.auth;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import com.example.chokokane.mapper.auth.AuthRateLimitMapper;
import com.example.chokokane.model.auth.AuthRateLimitKey;

@Repository
public class MyBatisAuthRateLimitRepository implements AuthRateLimitRepository {

  private final AuthRateLimitMapper authRateLimitMapper;

  public MyBatisAuthRateLimitRepository(
      AuthRateLimitMapper authRateLimitMapper) {
    this.authRateLimitMapper = authRateLimitMapper;
  }

  @Override
  public int ensureKey(String scope, String subjectHash) {
    return authRateLimitMapper.ensureKey(scope, subjectHash);
  }

  @Override
  public AuthRateLimitKey findForUpdate(String scope, String subjectHash) {
    return authRateLimitMapper.findForUpdate(scope, subjectHash);
  }

  @Override
  public long countEventsSince(Long keyId, LocalDateTime since) {
    return authRateLimitMapper.countEventsSince(keyId, since);
  }

  @Override
  public LocalDateTime findLastEventAt(Long keyId) {
    return authRateLimitMapper.findLastEventAt(keyId);
  }

  @Override
  public int insertEvent(Long keyId, LocalDateTime createdAt) {
    return authRateLimitMapper.insertEvent(keyId, createdAt);
  }

}
