package com.example.chokokane.security;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.example.chokokane.dto.auth.TurnstileResponse;

import org.springframework.core.env.Environment;

@Component
public class TurnstileVerifier {

  private final RestClient restClient;
  private final String secretKey;
  private final String expectedHostname;
  private final String expectedAction;
  private final boolean testMode;

  public TurnstileVerifier(
      @Value("${turnstile.secret-key}") String secretKey,
      @Value("${turnstile.expected-hostname}") String expectedHostname,
      @Value("${turnstile.expected-action}") String expectedAction,
      @Value("${turnstile.test-mode:false}") boolean testMode,
      Environment environment) {
    if (testMode
        && (!environment.matchesProfiles("local & !prod")
            || !"1x0000000000000000000000000000000AA".equals(secretKey))) {
      throw new IllegalStateException(
          "Turnstile test mode requires the local profile and test secret.");
    }

    this.testMode = testMode;

    if (secretKey.isBlank()
        || expectedHostname.isBlank()
        || expectedAction.isBlank()) {
      throw new IllegalStateException(
          "Turnstile configuration is required.");
    }

    this.secretKey = secretKey;
    this.expectedHostname = expectedHostname;
    this.expectedAction = expectedAction;

    HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(3))
        .build();

    JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);

    requestFactory.setReadTimeout(Duration.ofSeconds(5));

    this.restClient = RestClient.builder()
        .requestFactory(requestFactory)
        .build();
  }

  public boolean verify(String token) {
    if (token == null || token.isBlank() || token.length() > 2048) {
      return false;
    }

    try {
      TurnstileResponse response = restClient.post()
          .uri("https://challenges.cloudflare.com/turnstile/v0/siteverify")
          .contentType(MediaType.APPLICATION_JSON)
          .body(Map.of(
              "secret", secretKey,
              "response", token))
          .retrieve()
          .body(TurnstileResponse.class);

      if (response == null || !Boolean.TRUE.equals(response.success())) {
        return false;
      }

      if (testMode) {
        return true;
      }

      return expectedHostname.equals(response.hostname())
          && expectedAction.equals(response.action());
    } catch (RestClientException exception) {
      return false;
    }
  }
}
