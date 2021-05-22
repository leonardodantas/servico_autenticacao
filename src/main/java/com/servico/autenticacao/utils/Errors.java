package com.servico.autenticacao.utils;

import lombok.Getter;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Getter
public class Errors {

    private final String field;
    private final String message;

    public Errors(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public Errors(ObjectError error) {
        this.field = ((FieldError) error).getField();
        this.message = error.getDefaultMessage();
    }
}
