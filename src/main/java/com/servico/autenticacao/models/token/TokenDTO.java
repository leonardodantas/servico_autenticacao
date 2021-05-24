package com.servico.autenticacao.models.token;

import lombok.Getter;

@Getter
public class TokenDTO {
    private final String token;
    private final String type;

    public TokenDTO(String token, String type) {
        this.token = token;
        this.type = type;
    }
}
