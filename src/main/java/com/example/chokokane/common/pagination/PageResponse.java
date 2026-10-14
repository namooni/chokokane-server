package com.example.chokokane.common.pagination;

import java.util.List;

public class PageResponse<T> {

  private List<T> content;
  private int page;
  private int size;
  private int totalElements;
  private int totalPages;

  public PageResponse(
      List<T> content,
      PageRequest pageRequest,
      int totalElements) {
    this.content = content;
    this.page = pageRequest.getPage();
    this.size = pageRequest.getSize();
    this.totalElements = totalElements;
    this.totalPages = (int) Math.ceil((double) totalElements / size);

  }

  public List<T> getContent() {
    return content;
  }

  public int getPage() {
    return page;
  }

  public int getSize() {
    return size;
  }

  public int getTotalElements() {
    return totalElements;
  }

  public int getTotalPages() {
    return totalPages;
  }
}
