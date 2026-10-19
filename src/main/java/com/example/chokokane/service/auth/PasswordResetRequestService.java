package com.example.chokokane.service.auth;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.chokokane.dto.auth.PasswordResetRequest;
import com.example.chokokane.exception.BadRequestException;
import com.example.chokokane.model.user.User;
import com.example.chokokane.repository.user.UserRepository;
import com.example.chokokane.security.TurnstileVerifier;
import com.example.chokokane.service.mail.PasswordResetMailSender;

@Service
public class PasswordResetRequestService {

  private static final Logger log = LoggerFactory.getLogger(PasswordResetRequestService.class);

  private final PasswordResetRateLimitService rateLimitService;
  private final TurnstileVerifier turnstileVerifier;
  private final UserRepository userRepository;
  private final PasswordResetTokenService tokenService;
  private final PasswordResetMailSender mailSender;
  private final TaskExecutor executor;
  private final String resetUrl;

  public PasswordResetRequestService(
      PasswordResetRateLimitService rateLimitService,
      TurnstileVerifier turnstileVerifier,
      UserRepository userRepository,
      PasswordResetTokenService tokenService,
      PasswordResetMailSender mailSender,
      @Qualifier("passwordResetExecutor") TaskExecutor executor,
      @Value("${app.password-reset.url}") String resetUrl) {

    this.rateLimitService = rateLimitService;
    this.turnstileVerifier = turnstileVerifier;
    this.userRepository = userRepository;
    this.tokenService = tokenService;
    this.mailSender = mailSender;
    this.executor = executor;
    this.resetUrl = resetUrl;
  }

  public void request(PasswordResetRequest request, String ipAddress) {
    if (!rateLimitService.tryAcquireIp(ipAddress)) {
      return;
    }

    if (!turnstileVerifier.verify(request.captchaToken())) {
      throw new BadRequestException(
          "認証を確認できませんでした。もう一度お試しください。");
    }

    String email = request.email()
        .trim()
        .toLowerCase(Locale.ROOT);

    try {
      executor.execute(() -> process(email));
    } catch (TaskRejectedException exception) {
      log.warn("Password reset queue is full or unavailable.");
    }
  }

  private void process(String email) {
    try {
      User user = userRepository.findByEmail(email);

      if (user == null) {
        return;
      }

      if (!rateLimitService.tryReserveEmail(user.getEmail())) {
        return;
      }

      String rawToken = tokenService.issue(user.getId());

      String link = UriComponentsBuilder
          .fromUriString(resetUrl)
          .fragment("token=" + rawToken)
          .build()
          .toUriString();

      mailSender.sendResetLink(user.getEmail(), link);

    } catch (RuntimeException exception) {
      log.error(
          "Password reset background task failed: {}",
          exception.getClass().getSimpleName());
    }
  }
}