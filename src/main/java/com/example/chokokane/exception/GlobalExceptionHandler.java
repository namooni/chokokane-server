package com.example.chokokane.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidRefreshTokenException.class)
  public ResponseEntity<ApiError> handleInvalidRefreshToken(
      InvalidRefreshTokenException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        new ApiError(
            HttpStatus.UNAUTHORIZED.value(),
            e.getMessage()));
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ApiError> handleConflict(
      ConflictException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(
        new ApiError(
            HttpStatus.CONFLICT.value(),
            e.getMessage()));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiError> handleConstraintViolation(
      ConstraintViolationException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            e.getMessage()));
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiError> handleBadRequest(
      BadRequestException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            e.getMessage()));
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(
      NotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        new ApiError(
            HttpStatus.NOT_FOUND.value(),
            e.getMessage()));
  }

  @ExceptionHandler(LoginFailedException.class)
  public ResponseEntity<ApiError> handleLoginFailed(
      LoginFailedException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        new ApiError(
            HttpStatus.UNAUTHORIZED.value(),
            e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(
      MethodArgumentNotValidException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Validation failed.",
            getValidationErrors(e)));
  }

  private Map<String, String> getValidationErrors(
      MethodArgumentNotValidException e) {
    Map<String, String> errors = new HashMap<>();

    List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

    for (FieldError fieldError : fieldErrors) {
      errors.put(
          fieldError.getField(),
          fieldError.getDefaultMessage());
    }

    return errors;
  }
}
