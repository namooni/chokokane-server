package com.example.chokokane.common.pagination;

import lombok.Getter;

@Getter
public class PageRequest {

  private final int page;
  private final int size;

  public PageRequest(
      int page,
      int size) {
    if (page < 0 || size <= 0) {
      throw new IllegalArgumentException("Invalid pagination");
    }

    this.page = page;
    this.size = size;

  }

  public int getOffset() {
    return Math.multiplyExact(size, page);
  }

}
