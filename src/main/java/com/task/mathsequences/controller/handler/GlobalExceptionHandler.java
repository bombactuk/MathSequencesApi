package com.task.mathsequences.controller.handler;


import com.task.mathsequences.exception.AuthException;
import com.task.mathsequences.exception.ExceptionResponse;
import com.task.mathsequences.exception.FactorialException;
import com.task.mathsequences.exception.FibonacciException;
import com.task.mathsequences.exception.enums.ErrorCode;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private ExceptionResponse exceptionBuilder(ErrorCode errorCode, String message) {
        return ExceptionResponse.builder()
                .errorcode(errorCode)
                .message(message).timestamp(LocalDateTime.now()).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation failed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exceptionBuilder(ErrorCode.BAD_REQUEST, errorMessage));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ExceptionResponse> handleCreateUser(AuthException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exceptionBuilder(ErrorCode.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCreateUser(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exceptionBuilder(ErrorCode.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Invalid value for parameter '%s': '%s' is not a valid number",
                ex.getName(), ex.getValue());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exceptionBuilder(ErrorCode.BAD_REQUEST, message));
    }

    @ExceptionHandler(FactorialException.class)
    public ResponseEntity<ExceptionResponse> handleCreateUser(FactorialException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exceptionBuilder(ErrorCode.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(FibonacciException.class)
    public ResponseEntity<ExceptionResponse> handleCreateUser(FibonacciException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exceptionBuilder(ErrorCode.BAD_REQUEST, ex.getMessage()));
    }

}