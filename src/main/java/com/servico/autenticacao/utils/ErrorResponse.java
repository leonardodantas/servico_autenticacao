package com.servico.autenticacao.utils;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class ErrorResponse {

    private final String uuid;
    private final HttpStatus httpStatusCode;
    private final LocalDateTime date;
    private List<Errors> errors;

    public ErrorResponse(ResponseStatusException error) {
        uuid = UUID.randomUUID().toString();
        httpStatusCode = error.getStatus();
        date = LocalDateTime.now();
    }

    public ErrorResponse(MethodArgumentNotValidException exception, List<Errors> errors) {
        this.uuid = UUID.randomUUID().toString();
        this.httpStatusCode = HttpStatus.BAD_REQUEST;
        this.date = LocalDateTime.now();
        this.errors = errors;
    }
}