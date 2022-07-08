package com.servico.autenticacao.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<Object> handleAnyExceptions(final ResponseStatusException error) {
        final var errorResponse = new ErrorResponse(error);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), error.getStatus());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            final MethodArgumentNotValidException methodArgumentNotValidException) {

        final var errors = methodArgumentNotValidException.getBindingResult().getAllErrors();
        final var errorsList = errors.stream().map(Errors::new).collect(Collectors.toList());

        final var errorResponse = new ErrorResponse(errorsList);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}