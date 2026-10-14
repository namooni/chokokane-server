package com.example.chokokane.dto.category;

public record CategoryResponse(
    Long id,
    String name,
    String color,
    boolean isSystem) {

}
