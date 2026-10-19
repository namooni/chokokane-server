package com.example.chokokane.model.auth;

import java.time.LocalDateTime;

public record PasswordResetToken(
    Long id,
    Long userId,
    String tokenHash,
    LocalDateTime createdAt,
    LocalDateTime expiresAt,
    LocalDateTime usedAt) {

}
