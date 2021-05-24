package com.servico.autenticacao.models.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
public class Login {

    @ApiModelProperty(value = "Email para autenticação", name = "email", dataType = "String", example = "leonardodantas@email.com.br")
    private String email;
    @ApiModelProperty(value = "Senha para autenticação", name = "senha", dataType = "String", example = "123456")
    private String senha;

    public UsernamePasswordAuthenticationToken converter() {
        return new UsernamePasswordAuthenticationToken(email, senha);
    }

}
