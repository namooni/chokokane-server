package com.example.chokokane.service.auth;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.chokokane.dto.auth.PasswordResetConfirmRequest;
import com.example.chokokane.exception.BadRequestException;
import com.example.chokokane.model.auth.PasswordResetToken;
import com.example.chokokane.repository.auth.PasswordResetTokenRepository;
import com.example.chokokane.repository.auth.RefreshTokenRepository;
import com.example.chokokane.repository.user.UserRepository;
import com.example.chokokane.security.PasswordResetTokenProvider;

@Service
public class PasswordResetConfirmService {

  private final PasswordResetTokenRepository tokenRepository;
  private final PasswordResetTokenProvider tokenProvider;
  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;

  public PasswordResetConfirmService(
      PasswordResetTokenRepository tokenRepository,
      PasswordResetTokenProvider tokenProvider,
      UserRepository userREpository,
      RefreshTokenRepository refreshTokenRepository,
      PasswordEncoder passwordEncoder) {
    this.tokenRepository = tokenRepository;
    this.tokenProvider = tokenProvider;
    this.userRepository = userREpository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public void confirm(PasswordResetConfirmRequest request) {
    String rawToken = request.token();
    String newPassword = request.newPassword();

    if (rawToken == null
        || !rawToken.matches("[A-Za-z0-9_-]{43}")) {
      throw invalidLink();
    }

    if (newPassword == null || newPassword.isBlank()) {
      throw new BadRequestException(
          "新しいパスワードを入力してください。");
    }

    if (newPassword.getBytes(StandardCharsets.UTF_8).length > 72) {
      throw new BadRequestException(
          "新しいパスワードはUTF-8で72バイト以内にしてください。");
    }

    String tokenHash = tokenProvider.hash(rawToken);
    PasswordResetToken token = tokenRepository.findByTokenHash(tokenHash);

    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

    if (token == null
        || token.usedAt() != null
        || !token.expiresAt().isAfter(now)) {
      throw invalidLink();
    }

    String encodedPassword = passwordEncoder.encode(newPassword);

    int consumed = tokenRepository.markUsed(
        tokenHash,
        LocalDateTime.now(ZoneOffset.UTC));

    if (consumed != 1) {
      throw invalidLink();
    }

    int updated = userRepository.updatePassword(
        token.userId(),
        encodedPassword);

    if (updated != 1) {
      throw invalidLink();
    }

    refreshTokenRepository.deleteByUserId(token.userId());
  }

  private BadRequestException invalidLink() {
    return new BadRequestException(
        "再設定リンクが無効か、有効期限が切れています。もう一度お申し込みください。");
  }
}
