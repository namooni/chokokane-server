package com.example.chokokane.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordResetConfirmRequest(
    @NotBlank(message = "再設定リンクが正しくありません。") @Pattern(regexp = "[A-Za-z0-9_-]{43}", message = "再設定リンクが正しくありません。") String token,

    @NotBlank(message = "新しいパスワードを入力してください。") String newPassword) {

}
