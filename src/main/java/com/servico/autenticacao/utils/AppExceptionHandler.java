package com.servico.autenticacao.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<Object> handleAnyExcepton(ResponseStatusException error){
        ErrorResponse errorResponse = new ErrorResponse(error);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), error.getStatus());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            MethodArgumentNotValidException methodArgumentNotValidException) {

        List<ObjectError> errors = methodArgumentNotValidException.getBindingResult().getAllErrors();
        List<Errors> errosList = errors.stream().map(Errors::new).collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(methodArgumentNotValidException, errosList);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}