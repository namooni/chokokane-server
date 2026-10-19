package com.example.chokokane.service.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetMailSender {

  private final JavaMailSender mailSender;
  private final String from;

  public PasswordResetMailSender(
      JavaMailSender mailSender,
      @Value("${app.mail.from}") String from) {
    this.mailSender = mailSender;
    this.from = from;
  }

  public void sendResetLink(String to, String resetUrl) {
    SimpleMailMessage message = new SimpleMailMessage();

    message.setFrom(from);
    message.setTo(to);
    message.setSubject("【ちょこかね】パスワード再設定");
    message.setText("""
        ちょこかねのパスワード再設定を受け付けました。

        以下のリンクから新しいパスワードを設定してください。

        %s

        このリンクの有効期限は30分です。
        一度使用したリンクは再使用できません。

        このメールに心当たりがない場合は、何もせずに破棄してください。
        リンクから再設定を完了するまで、パスワードは変更されません。
        """.formatted(resetUrl));

    mailSender.send(message);
  }
}
