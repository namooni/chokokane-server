package com.example.chokokane.service.history;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.chokokane.common.time.MonthRange;
import com.example.chokokane.dto.category.CategorySummaryResponse;
import com.example.chokokane.dto.expense.ExpenseResponse;
import com.example.chokokane.dto.history.HistoryResponse;
import com.example.chokokane.dto.history.HistorySearchRequest;
import com.example.chokokane.exception.BadRequestException;
import com.example.chokokane.model.expense.ExpenseDetail;
import com.example.chokokane.model.history.HistoryCursor;
import com.example.chokokane.repository.expense.ExpenseRepository;

@Service
public class HistoryService {

  private final ExpenseRepository expenseRepository;
  private final HistoryCursorCodec cursorCodec;

  public HistoryService(ExpenseRepository expenseRepository, HistoryCursorCodec cursorCodec) {
    this.expenseRepository = expenseRepository;
    this.cursorCodec = cursorCodec;
  }

  @Transactional(readOnly = true)
  public HistoryResponse getHistory(Long userId, HistorySearchRequest request) {
    MonthRange range = MonthRange.of(request.getMonth());
    int size = request.getSize();

    if (size < 1 || size > 100) {
      throw new BadRequestException("取得件数は1〜100件で指定してください。");

    }
    Long categoryId = request.getCategoryId();
    if (categoryId != null && categoryId <= 0) {
      throw new BadRequestException("カテゴリーの指定が正しくありません。");
    }

    HistoryCursor cursor = cursorCodec.decode(request.getCursor());

    List<ExpenseDetail> rows = expenseRepository
        .findHistory(
            userId,
            range.monthStart(),
            range.nextMonthStart(),
            range.createdAtStart(),
            range.createdAtEnd(),
            categoryId,
            request.isUndated(),
            cursor,
            size + 1);

    boolean hasNext = rows.size() > size;

    List<ExpenseDetail> visibleRows = rows.subList(0, Math.min(size, rows.size()));

    String nextCursor = null;

    if (hasNext) {
      ExpenseDetail last = visibleRows.getLast();
      nextCursor = cursorCodec
          .encode(new HistoryCursor(
              last.getExpenseDate(),
              last.getExpenseTime(),
              last.getCreatedAt(),
              last.getId()));
    }

    long monthlyTotal = expenseRepository
        .getMonthlySpent(
            userId,
            range.monthStart(),
            range.nextMonthStart(),
            range.createdAtStart(),
            range.createdAtEnd());

    long filteredTotal = expenseRepository
        .getHistoryFilteredTotal(
            userId,
            range.monthStart(),
            range.nextMonthStart(),
            range.createdAtStart(),
            range.createdAtEnd(),
            categoryId,
            request.isUndated());

    List<CategorySummaryResponse> availableCategories = expenseRepository
        .getCategoryTotalByMonth(
            userId,
            range.monthStart(),
            range.nextMonthStart(),
            range.createdAtStart(),
            range.createdAtEnd())
        .stream()
        .map(item -> new CategorySummaryResponse(
            item.getCategoryId(),
            item.getCategoryName(),
            item.getCategoryColor()))
        .toList();

    List<ExpenseResponse> content = visibleRows.stream()
        .map(item -> new ExpenseResponse(
            item.getId(),
            item.getAmount(),
            new CategorySummaryResponse(
                item.getCategoryId(),
                item.getCategoryName(),
                item.getCategoryColor()),
            item.getMemo(),
            item.getExpenseDate(),
            item.getExpenseTime(),
            item.getCreatedAt()))
        .toList();

    return new HistoryResponse(
        range.month().toString(),
        monthlyTotal,
        filteredTotal,
        availableCategories,
        content,
        nextCursor,
        hasNext);
  }
}
