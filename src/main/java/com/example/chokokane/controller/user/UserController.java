package com.example.chokokane.controller.user;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.chokokane.dto.auth.RefreshResult;
import com.example.chokokane.dto.auth.RefreshTokenResponse;
import com.example.chokokane.dto.user.LoginRequest;
import com.example.chokokane.dto.user.LoginResponse;
import com.example.chokokane.dto.user.LoginResult;
import com.example.chokokane.dto.user.SignupRequest;
import com.example.chokokane.dto.user.UserResponse;
import com.example.chokokane.service.user.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Users", description = "유저 관리")
@RestController
public class UserController {

  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Operation(summary = "회원가입", description = "회원가입합니다.")
  @PostMapping("/signup")
  public UserResponse signUp(
      @RequestBody(required = true) @Valid SignupRequest request) {

    System.out.println("SIGNUP CONTROLLER");
    return userService.signUp(request);
  }

  @Operation(summary = "로그인", description = "로그인합니다.")
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(
      @RequestBody(required = true) LoginRequest request) {

    LoginResult result = userService.login(request);

    ResponseCookie cookie = ResponseCookie.from(
        "refreshToken",
        result.getRefreshToken())
        .httpOnly(true)
        .path("/")
        .maxAge(Duration.ofDays(30))
        .build();

    LoginResponse response = new LoginResponse(result.getAccessToken());

    return ResponseEntity
        .ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(response);
  }

  @Operation(summary = "로그아웃", description = "로그아웃, 토큰 삭제")
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(Authentication authentication) {

    Long userId = Long.valueOf(authentication.getName());

    userService.logout(userId);

    ResponseCookie cookie = ResponseCookie.from(
        "refreshToken",
        "")
        .httpOnly(true)
        .path("/")
        .maxAge(0)
        .build();

    return ResponseEntity
        .ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .build();
  }

  @Operation(summary = "토큰 리프레쉬", description = "토큰 재발급")
  @PostMapping("/refresh")
  public ResponseEntity<RefreshTokenResponse> refresh(
      @CookieValue("refreshToken") String refreshToken) {
    RefreshResult result = userService.refresh(refreshToken);

    ResponseCookie cookie = ResponseCookie.from(
        "refreshToken",
        result.getRefreshToken())
        .httpOnly(true)
        .path("/")
        .maxAge(Duration.ofDays(30))
        .build();

    RefreshTokenResponse response = new RefreshTokenResponse(result.getAccessToken());

    return ResponseEntity
        .ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(response);
  }

  @Operation(summary = "내 정보 조회", description = "내 정보를 조회합니다.")
  @GetMapping("/me")
  public UserResponse getMe(Authentication authentication) {
    Long userId = Long.valueOf(authentication.getName());

    return userService.getMe(userId);
  }

  @Operation(summary = "탈퇴", description = "유저를 삭제합니다.")
  @DeleteMapping("/me")
  public int deleteMe(Authentication authentication) {
    Long userId = Long.valueOf(authentication.getName());

    return userService.deleteMe(userId);
  }
}
