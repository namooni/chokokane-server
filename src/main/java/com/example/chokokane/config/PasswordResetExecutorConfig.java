package com.example.chokokane.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class PasswordResetExecutorConfig {

  @Bean("passwordResetExecutor")
  public ThreadPoolTaskExecutor passwordResetExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    executor.setCorePoolSize(2);
    executor.setMaxPoolSize(2);
    executor.setQueueCapacity(50);
    executor.setThreadNamePrefix("password-reset-");
    executor.setRejectedExecutionHandler(
        new ThreadPoolExecutor.AbortPolicy());

    return executor;
  }
}
