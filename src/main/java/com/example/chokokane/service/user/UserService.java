package com.example.chokokane.service.user;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.chokokane.dto.auth.RefreshResult;
import com.example.chokokane.dto.user.ChangePasswordRequest;
import com.example.chokokane.dto.user.DeleteAccountRequest;
import com.example.chokokane.dto.user.LoginRequest;
import com.example.chokokane.dto.user.LoginResult;
import com.example.chokokane.dto.user.SignupRequest;
import com.example.chokokane.dto.user.UserResponse;
import com.example.chokokane.exception.BadRequestException;
import com.example.chokokane.exception.ConflictException;
import com.example.chokokane.exception.InvalidRefreshTokenException;
import com.example.chokokane.exception.LoginFailedException;
import com.example.chokokane.exception.NotFoundException;
import com.example.chokokane.model.auth.RefreshToken;
import com.example.chokokane.model.user.User;
import com.example.chokokane.repository.auth.RefreshTokenRepository;
import com.example.chokokane.repository.budget.MonthlyBudgetRepository;
import com.example.chokokane.repository.category.CategoryRepository;
import com.example.chokokane.repository.expense.ExpenseRepository;
import com.example.chokokane.repository.user.UserRepository;
import com.example.chokokane.service.category.CategoryService;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtEncoder jwtEncoder;
  private final JwtDecoder refreshJwtDecoder;
  private final RefreshTokenRepository refreshTokenRepository;
  private final ExpenseRepository expenseRepository;
  private final CategoryRepository categoryRepository;
  private final MonthlyBudgetRepository monthlyBudgetRepository;

  private final CategoryService categoryService;

  public UserService(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtEncoder jwtEncoder,
      @Qualifier("refreshJwtDecoder") JwtDecoder refreshJwtDecoder,
      RefreshTokenRepository refreshTokenRepository,
      CategoryService categoryService,
      ExpenseRepository expenseRepository,
      CategoryRepository categoryRepository,
      MonthlyBudgetRepository monthlyBudgetRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtEncoder = jwtEncoder;
    this.refreshJwtDecoder = refreshJwtDecoder;
    this.refreshTokenRepository = refreshTokenRepository;
    this.categoryService = categoryService;
    this.expenseRepository = expenseRepository;
    this.categoryRepository = categoryRepository;
    this.monthlyBudgetRepository = monthlyBudgetRepository;
  }

  @Transactional
  public UserResponse signUp(SignupRequest request) {
    User existingUser = userRepository.findByEmail(request.getEmail());

    if (existingUser != null) {
      throw new ConflictException("Email already exists.");
    }

    String encodedPassword = passwordEncoder.encode(request.getPassword());

    User user = new User(
        null,
        request.getName(),
        request.getEmail(),
        encodedPassword);

    userRepository.save(user);

    categoryService.createDefaultCategories(user.getId());

    return new UserResponse(user.getId(), user.getName(), user.getEmail());
  }

  public LoginResult login(LoginRequest request) {

    User user = userRepository.findByEmail(request.getEmail());

    if (user == null) {
      throw new LoginFailedException("Invalid email or password.");
    }

    boolean matches = passwordEncoder.matches(
        request.getPassword(),
        user.getPassword());

    if (!matches) {
      throw new LoginFailedException("Invalid email or password.");
    }

    Instant now = Instant.now();
    Instant refreshExpiresAt = now.plus(30, ChronoUnit.DAYS);

    JwtClaimsSet accessClaims = JwtClaimsSet.builder()
        .subject(user.getId().toString())
        .claim("type", "access")
        .issuedAt(now)
        .expiresAt(now.plusSeconds(900))
        .build();

    String accessToken = jwtEncoder
        .encode(JwtEncoderParameters.from(accessClaims))
        .getTokenValue();

    JwtClaimsSet refreshClaims = JwtClaimsSet.builder()
        .subject(user.getId().toString())
        .claim("type", "refresh")
        .issuedAt(now)
        .expiresAt(refreshExpiresAt)
        .build();

    String refreshToken = jwtEncoder
        .encode(JwtEncoderParameters.from(refreshClaims))
        .getTokenValue();

    LocalDateTime expiresAt = LocalDateTime.ofInstant(
        refreshExpiresAt,
        ZoneId.systemDefault());

    RefreshToken saveRefreshToken = new RefreshToken(
        null,
        user.getId(),
        refreshToken,
        expiresAt);

    refreshTokenRepository.save(saveRefreshToken);

    return new LoginResult(accessToken, refreshToken);
  }

  public void logout(Long userId) {
    refreshTokenRepository.deleteByUserId(userId);
  }

  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public UserResponse getMe(Long userId) {
    User user = userRepository.findById(userId);
    return new UserResponse(user.getId(), user.getName(), user.getEmail());
  }

  @Transactional
  public void changePassword(
      Long userId,
      ChangePasswordRequest request) {
    String currentPassword = request.currentPassword();
    String newPassword = request.newPassword();

    if (currentPassword == null || currentPassword.isBlank()) {
      throw new BadRequestException(
          "現在のパスワードを入力してください。");
    }

    if (newPassword == null || newPassword.isBlank()) {
      throw new BadRequestException(
          "新しいパスワードを入力してください。");
    }

    if (currentPassword.getBytes(StandardCharsets.UTF_8).length > 72) {
      throw new BadRequestException(
          "現在のパスワードが正しくありません。");
    }

    if (newPassword.getBytes(StandardCharsets.UTF_8).length > 72) {
      throw new BadRequestException(
          "新しいパスワードはUTF-8で 72バイト以内にしてください。");
    }

    User user = userRepository.findById(userId);

    if (user == null) {
      throw new NotFoundException(
          "アカウントが見つかりません。");
    }

    if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
      throw new BadRequestException(
          "現在のパスワードが正しくありません。");
    }

    String encodedPassword = passwordEncoder.encode(newPassword);

    int updated = userRepository.updatePassword(userId, encodedPassword);

    if (updated != 1) {
      throw new IllegalStateException("Password update failed.");
    }

    refreshTokenRepository.deleteByUserId(userId);
  }

  @Transactional
  public void deleteMe(Long userId, DeleteAccountRequest request) {
    String password = request.password();

    if (password == null || password.isBlank()) {
      throw new BadRequestException("パスワードを入力してください。");
    }

    if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
      throw new BadRequestException("パスワードが正しくありません。");
    }

    User user = userRepository.findById(userId);

    if (user == null) {
      throw new NotFoundException("アカウントが見つかりません。");
    }

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new BadRequestException("パスワードが正しくありません。");
    }

    expenseRepository.deleteAllByUserId(userId);
    categoryRepository.deleteAllByUserId(userId);
    monthlyBudgetRepository.deleteAllByUserId(userId);
    refreshTokenRepository.deleteByUserId(userId);

    int deleted = userRepository.deleteMe(userId);

    if (deleted != 1) {
      throw new IllegalStateException("Account deletion failed.");
    }
  }

  public RefreshResult refresh(String refreshToken) {
    Jwt jwt = refreshJwtDecoder.decode(refreshToken);

    Long userId = Long.valueOf(jwt.getSubject());

    RefreshToken savedToken = refreshTokenRepository.findByUserId(userId);

    if (savedToken == null || !savedToken.getToken().equals(refreshToken)) {
      throw new InvalidRefreshTokenException("Invalid refresh token.");
    }

    Instant now = Instant.now();

    Instant refreshExpiresAt = now.plus(30, ChronoUnit.DAYS);

    JwtClaimsSet refreshClaimSet = JwtClaimsSet.builder()
        .subject(userId.toString())
        .claim("type", "refresh")
        .issuedAt(now)
        .expiresAt(refreshExpiresAt)
        .build();

    String newRefreshToken = jwtEncoder
        .encode(JwtEncoderParameters.from(refreshClaimSet))
        .getTokenValue();

    JwtClaimsSet accessClaimSet = JwtClaimsSet.builder()
        .subject(userId.toString())
        .claim("type", "access")
        .issuedAt(now)
        .expiresAt(now.plusSeconds(900))
        .build();

    String accessToken = jwtEncoder
        .encode(JwtEncoderParameters.from(accessClaimSet))
        .getTokenValue();

    LocalDateTime expiresAt = LocalDateTime.ofInstant(
        refreshExpiresAt,
        ZoneId.systemDefault());

    RefreshToken newSavedToken = new RefreshToken(
        null,
        userId,
        newRefreshToken,
        expiresAt);

    refreshTokenRepository.save(newSavedToken);

    return new RefreshResult(accessToken, newRefreshToken);
  }
}
