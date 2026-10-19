package com.example.chokokane.controller.auth;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.chokokane.dto.auth.PasswordResetConfirmRequest;
import com.example.chokokane.dto.auth.PasswordResetRequest;
import com.example.chokokane.security.RefreshTokenCookieProvider;
import com.example.chokokane.service.auth.PasswordResetConfirmService;
import com.example.chokokane.service.auth.PasswordResetRateLimitService;
import com.example.chokokane.service.auth.PasswordResetRequestService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Tag(name = "Auth", description = "인증 관련")
@RestController
public class PasswordResetController {

  private final PasswordResetRequestService requestService;
  private final PasswordResetConfirmService confirmService;
  private final PasswordResetRateLimitService reateLimitService;
  private final RefreshTokenCookieProvider cookieProvider;

  public PasswordResetController(
      PasswordResetRequestService requestService,
      PasswordResetConfirmService confirmService,
      PasswordResetRateLimitService rateLimitService,
      RefreshTokenCookieProvider cookieProvider) {
    this.requestService = requestService;
    this.confirmService = confirmService;
    this.reateLimitService = rateLimitService;
    this.cookieProvider = cookieProvider;
  }

  @PostMapping("/password-reset/request")
  public ResponseEntity<Map<String, String>> request(
      @Valid @RequestBody PasswordResetRequest body,
      HttpServletRequest httpRequest) {

    requestService.request(body, httpRequest.getRemoteAddr());

    return ResponseEntity.accepted().body(
        Map.of(
            "message",
            "登録済みのメールアドレスの場合、パスワード再設定の案内を送信します。"));
  }

  @PostMapping("/password-reset/confirm")
  public ResponseEntity<Void> confirm(
      @Valid @RequestBody PasswordResetConfirmRequest body,
      HttpServletRequest httpRequest) {

    if (!reateLimitService.tryAcquireIp(httpRequest.getRemoteAddr())) {
      return ResponseEntity
          .status(HttpStatus.TOO_MANY_REQUESTS)
          .build();
    }

    confirmService.confirm(body);

    ResponseCookie cookie = cookieProvider.delete();

    return ResponseEntity
        .noContent()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .build();
  }
}
