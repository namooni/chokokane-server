package com.example.chokokane.dto.history;

import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistorySearchRequest {

  @NotNull(message = "表示する月を指定してください。")
  @DateTimeFormat(pattern = "yyyy-MM")
  private YearMonth month;

  @Positive(message = "カテゴリーの指定が正しくありません。")
  private Long categoryId;

  private boolean undated = false;

  private String cursor;

  @Min(value = 1, message = "取得件数は1件以上で指定してください。")
  @Max(value = 100, message = "取得件数は100件以下で指定してください。")
  private int size = 20;

}
