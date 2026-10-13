package com.example.chokokane.model.category;

import java.time.LocalDateTime;

public class Category {

  private Long id;
  private Long userId;
  private String name;
  private String color;
  private boolean isSystem;
  private LocalDateTime createdAt;
  private LocalDateTime deletedAt;

  public Category(
      Long id,
      Long userId,
      String name,
      String color,
      boolean isSystem,
      LocalDateTime createdAt,
      LocalDateTime deletedAt) {
    this.id = id;
    this.userId = userId;
    this.name = name;
    this.color = color;
    this.isSystem = isSystem;
    this.createdAt = createdAt;
    this.deletedAt = deletedAt;
  }

  public Long getId() {
    return id;
  }

  public Long getUserId() {
    return userId;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

  public boolean getIsSystem() {
    return isSystem;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getDeletedAt() {
    return deletedAt;
  }
}
