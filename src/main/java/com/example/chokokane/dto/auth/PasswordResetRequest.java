package com.example.chokokane.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetRequest(
    @NotBlank(message = "メールアドレスを入力してください。") @Email(message = "メールアドレスの形式が正しくありません。") @Size(max = 255, message = "メールアドレスは255文字以内で入力してください。") String email,

    @NotBlank(message = "認証を完了してください。") @Size(max = 2048, message = "認証情報が正しくありません。") String captchaToken) {

}
