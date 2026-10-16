package com.example.chokokane.dto.user;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
    @NotBlank(message = "現在のパスワードを入力してください。") String currentPassword,

    @NotBlank(message = "新しいパスワードを入力してください。") String newPassword) {

}
