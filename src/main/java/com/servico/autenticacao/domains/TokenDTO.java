package com.servico.autenticacao.domains;

import lombok.Getter;

@Getter
public class TokenDTO {
    private final String token;
    private final String type;

    public TokenDTO(final String token, final String type) {
        this.token = token;
        this.type = type;
    }
}
