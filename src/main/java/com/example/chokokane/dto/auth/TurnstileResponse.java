package com.example.chokokane.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TurnstileResponse(
    Boolean success,
    String hostname,
    String action) {

}