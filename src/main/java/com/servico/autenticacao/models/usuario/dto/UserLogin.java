package com.servico.autenticacao.models.usuario.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLogin {

    @ApiModelProperty(name = "Email do usuario", example = "leonardo1317@yahoo.com.br", dataType = "String")
    @Length(min = 20, max = 120 , message = "{validation.length.20.120}") @NotNull(message = "{validation.campo.not.null}")
    protected String email;

    @ApiModelProperty(name = "Senha do usuario", example = "28Senha@" ,dataType = "String")
    @Length(min = 6, max = 8, message = "{validation.length.6.8}") @NotNull(message = "{validation.campo.not.null}")
    protected String password;

}
