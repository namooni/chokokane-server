package com.example.chokokane.dto.user;

import jakarta.validation.constraints.NotBlank;

public record DeleteAccountRequest(
    @NotBlank(message = "パスワードを入力してください。") String password) {
}