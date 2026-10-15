package com.example.chokokane.service.history;

import java.util.Base64;

import org.springframework.stereotype.Component;

import com.example.chokokane.exception.BadRequestException;
import com.example.chokokane.model.history.HistoryCursor;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Component
public class HistoryCursorCodec {

  private final JsonMapper jsonMapper;

  public HistoryCursorCodec(JsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  public String encode(HistoryCursor cursor) {
    byte[] json = jsonMapper.writeValueAsBytes(cursor);

    return Base64.getUrlEncoder()
        .withoutPadding()
        .encodeToString(json);
  }

  public HistoryCursor decode(String value) {
    if (value == null) {
      return null;
    }

    if (value.isBlank() || value.length() > 2048) {
      throw invalidCursor();
    }

    HistoryCursor cursor;

    try {
      byte[] json = Base64.getUrlDecoder().decode(value);
      cursor = jsonMapper.readValue(json, HistoryCursor.class);
    } catch (IllegalArgumentException | JacksonException e) {
      throw invalidCursor();
    }

    if (cursor == null || cursor.createdAt() == null) {
      throw invalidCursor();
    }

    Long id = cursor.id();

    if (id == null || id <= 0) {
      throw invalidCursor();
    }

    if (cursor.expenseDate() == null && cursor.expenseTime() != null) {
      throw invalidCursor();
    }

    return cursor;
  }

  private BadRequestException invalidCursor() {
    return new BadRequestException(
        "続きの取得情報が正しくありません。最初から読み込んでください。");
  }
}
