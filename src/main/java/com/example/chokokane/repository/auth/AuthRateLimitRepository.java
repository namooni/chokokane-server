package com.example.chokokane.repository.auth;

import java.time.LocalDateTime;

import com.example.chokokane.model.auth.AuthRateLimitKey;

public interface AuthRateLimitRepository {

  int ensureKey(String scope, String subjectHash);

  AuthRateLimitKey findForUpdate(String scope, String subjectHash);

  long countEventsSince(Long keyId, LocalDateTime since);

  LocalDateTime findLastEventAt(Long keyId);

  int insertEvent(Long keyId, LocalDateTime createdAt);
}
