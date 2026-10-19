package com.example.chokokane.model.auth;

public record AuthRateLimitKey(
    Long id,
    String scope,
    String subjectHash) {

}
