package com.example.chokokane.service.history;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.chokokane.dto.history.HistoryResponse;
import com.example.chokokane.dto.history.HistorySearchRequest;
import com.example.chokokane.exception.BadRequestException;
import com.example.chokokane.model.expense.ExpenseDetail;
import com.example.chokokane.model.history.HistoryCursor;
import com.example.chokokane.repository.expense.ExpenseRepository;

class HistoryServiceTest {
  private ExpenseRepository repository;
  private HistoryCursorCodec codec;
  private HistoryService service;
  private HistorySearchRequest request;

  @BeforeEach
  void setUp() {
    repository = mock(ExpenseRepository.class);
    codec = mock(HistoryCursorCodec.class);
    service = new HistoryService(repository, codec);
    request = new HistorySearchRequest();
    request.setMonth(YearMonth.of(2024, 2));
    request.setSize(2);
    when(repository.getMonthlySpent(anyLong(), any(), any(), any(), any())).thenReturn(5000L);
    when(repository.getHistoryFilteredTotal(anyLong(), any(), any(), any(), any(), any(), anyBoolean())).thenReturn(3000L);
    when(repository.getCategoryTotalByMonth(anyLong(), any(), any(), any(), any())).thenReturn(List.of());
  }

  private ExpenseDetail expense(long id) {
    ExpenseDetail row = new ExpenseDetail();
    row.setId(id); row.setAmount(1000); row.setCategoryId(11L);
    row.setCategoryName("交通"); row.setCategoryColor("#65A7D8");
    row.setExpenseDate(LocalDate.of(2024, 2, 15));
    row.setCreatedAt(LocalDateTime.of(2024, 2, 15, 1, 0));
    return row;
  }

  @Test
  void nextCursorUsesLastReturnedRowInsteadOfExtraRow() {
    ExpenseDetail first = expense(3L), last = expense(2L), extra = expense(1L);
    when(repository.findHistory(anyLong(), any(), any(), any(), any(), any(), anyBoolean(), any(), anyInt()))
        .thenReturn(List.of(first, last, extra));
    HistoryCursor expected = new HistoryCursor(last.getExpenseDate(), null, last.getCreatedAt(), 2L);
    when(codec.encode(expected)).thenReturn("next");

    HistoryResponse response = service.getHistory(9L, request);
    assertEquals(List.of(3L, 2L), response.content().stream().map(item -> item.id()).toList());
    assertTrue(response.hasNext()); assertEquals("next", response.nextCursor());
    assertEquals(5000L, response.monthlyTotal()); assertEquals(3000L, response.filteredTotal());
    verify(codec).encode(expected);
    verify(repository).findHistory(eq(9L), any(), any(), any(), any(), isNull(), eq(false), isNull(), eq(3));
  }

  @Test
  void exactPageSizeHasNoNextCursor() {
    when(repository.findHistory(anyLong(), any(), any(), any(), any(), any(), anyBoolean(), any(), anyInt()))
        .thenReturn(List.of(expense(2L), expense(1L)));
    HistoryResponse response = service.getHistory(9L, request);
    assertFalse(response.hasNext()); assertNull(response.nextCursor());
    verify(codec, never()).encode(any());
  }

  @Test
  void emptyPageAndCursorStillReturnFullTotals() {
    request.setCursor("previous"); request.setCategoryId(11L); request.setUndated(true);
    HistoryCursor cursor = new HistoryCursor(null, null, LocalDateTime.of(2024, 2, 1, 1, 0), 5L);
    when(codec.decode("previous")).thenReturn(cursor);
    when(repository.findHistory(anyLong(), any(), any(), any(), any(), any(), anyBoolean(), any(), anyInt()))
        .thenReturn(List.of());
    HistoryResponse response = service.getHistory(9L, request);
    assertTrue(response.content().isEmpty()); assertFalse(response.hasNext());
    assertNull(response.nextCursor()); assertEquals(3000L, response.filteredTotal());
    verify(repository).findHistory(eq(9L), any(), any(), any(), any(), eq(11L), eq(true), eq(cursor), eq(3));
    verify(repository).getHistoryFilteredTotal(eq(9L), any(), any(), any(), any(), eq(11L), eq(true));
  }

  @Test
  void rejectsInvalidSizeBeforeQuerying() {
    request.setSize(0);
    assertThrows(BadRequestException.class, () -> service.getHistory(9L, request));
    verify(repository, never()).findHistory(anyLong(), any(), any(), any(), any(), any(), anyBoolean(), any(), anyInt());
  }
}
