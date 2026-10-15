package com.example.chokokane.controller.history;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.example.chokokane.dto.history.HistoryResponse;
import com.example.chokokane.dto.history.HistorySearchRequest;
import com.example.chokokane.service.history.HistoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "History", description = "히스토리 관리")
@RestController
public class HistoryController {

  private HistoryService historyService;

  public HistoryController(HistoryService historyService) {
    this.historyService = historyService;
  }

  @Operation(summary = "", description = "")
  @GetMapping("/history")
  public HistoryResponse getHistory(
      Authentication authentication,
      @Valid @ParameterObject @ModelAttribute HistorySearchRequest request) {
    Long userId = Long.valueOf(authentication.getName());

    return historyService.getHistory(userId, request);
  }
}
