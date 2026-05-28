package com.taskflow.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ApiError(int status, String error, String message, Object details, Instant timestamp) {
        public static ApiError of(HttpStatus s, String msg) { return of(s, msg, null); }
        public static ApiError of(HttpStatus s, String msg, Object details) {
            return new ApiError(s.value(), s.getReasonPhrase(), msg, details, Instant.now());
        }
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> notFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.of(HttpStatus.NOT_FOUND, ex.getMessage()));
    }
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiError> badRequest(BadCredentialsException ex) {
		return ResponseEntity
			.status(HttpStatus.UNAUTHORIZED)
			.body(ApiError.of(HttpStatus.UNAUTHORIZED, ex.getMessage()));
	}
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> badRequest(BadRequestException ex) {
        return ResponseEntity.badRequest().body(ApiError.of(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(ApiError.of(HttpStatus.BAD_REQUEST, "Validation failed", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> generic(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error"));
    }
}
