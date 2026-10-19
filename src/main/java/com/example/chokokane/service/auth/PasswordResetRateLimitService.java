package com.example.chokokane.service.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HexFormat;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.example.chokokane.model.auth.AuthRateLimitKey;
import com.example.chokokane.repository.auth.AuthRateLimitRepository;

@Service
public class PasswordResetRateLimitService {

  private final AuthRateLimitRepository repository;

  public PasswordResetRateLimitService(
      AuthRateLimitRepository repository) {
    this.repository = repository;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
  public boolean tryAcquireIp(String ipAddress) {
    if (ipAddress == null || ipAddress.isBlank()) {
      throw new IllegalArgumentException("IP address is required.");
    }

    AuthRateLimitKey key = lockKey(
        "PASSWORD_RESET_IP",
        hash(ipAddress));

    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

    long count = repository.countEventsSince(
        key.id(),
        now.minusMinutes(15));

    if (count >= 10)

    {
      return false;
    }

    recordEvent(key.id(), now);

    return true;
  }

  private AuthRateLimitKey lockKey(String scope, String subjectHash) {
    repository.ensureKey(scope, subjectHash);

    AuthRateLimitKey key = repository.findForUpdate(scope, subjectHash);

    if (key == null) {
      throw new IllegalStateException("Rate limit key not found.");
    }

    return key;
  }

  private void recordEvent(Long keyId, LocalDateTime now) {
    if (repository.insertEvent(keyId, now) != 1) {
      throw new IllegalStateException("Rate limit event insert failed.");
    }
  }

  private String hash(String value) {
    try {
      byte[] digest = MessageDigest
          .getInstance("SHA-256")
          .digest(value.getBytes(StandardCharsets.UTF_8));

      return HexFormat.of().formatHex(digest);
    } catch (NoSuchAlgorithmException exception) {
      throw new IllegalStateException(
          "SHA-256 is unavailable.",
          exception);
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
  public boolean tryReserveEmail(String email) {
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("Email is required.");
    }

    String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

    AuthRateLimitKey globalKey = lockKey(
        "PASSWORD_RESET_GLOBAL",
        hash("PASSWORD_RESET_GLOBAL"));

    AuthRateLimitKey emailKey = lockKey(
        "PASSWORD_RESET_EMAIL",
        hash(normalizedEmail));

    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

    ZoneId japan = ZoneId.of("Asia/Tokyo");

    LocalDateTime dayStartUtc = now
        .atOffset(ZoneOffset.UTC)
        .atZoneSameInstant(japan)
        .toLocalDate()
        .atStartOfDay(japan)
        .withZoneSameInstant(ZoneOffset.UTC)
        .toLocalDateTime();

    long globalCount = repository.countEventsSince(
        globalKey.id(),
        dayStartUtc);

    if (globalCount >= 100) {
      return false;
    }

    long emailCount = repository.countEventsSince(emailKey.id(), dayStartUtc);

    if (emailCount >= 5) {
      return false;
    }

    LocalDateTime lastEventAt = repository.findLastEventAt(emailKey.id());

    if (lastEventAt != null
        && lastEventAt.isAfter(now.minusSeconds(60))) {
      return false;
    }

    recordEvent(globalKey.id(), now);
    recordEvent(emailKey.id(), now);

    return true;
  }
}