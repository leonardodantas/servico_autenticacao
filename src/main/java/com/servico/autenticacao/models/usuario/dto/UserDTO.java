package com.servico.autenticacao.models.usuario.dto;

import com.servico.autenticacao.models.usuario.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
public class UserDTO {

    @ApiModelProperty(name = "ID do usuario", example = "4b212d7e-ba27-11eb-8529-0242ac130003" ,dataType = "String",hidden = true)
    private String id;

    @ApiModelProperty(name = "Nome do usuario", example = "Leonardo Rodrigues Dantas" ,dataType = "String")
    @Length(min = 12, max = 120, message = "{validation.length.12.120}") @NotNull(message = "{validation.campo.not.null}")
    private String name;

    @ApiModelProperty(name = "Email do usuario", example = "leonardo1317@yahoo.com.br", dataType = "String")
    @Length(min = 20, max = 120 , message = "{validation.length.20.120}") @NotNull(message = "{validation.campo.not.null}")
    private String email;

    @ApiModelProperty(name = "Senha do usuario", example = "28Senha@" ,dataType = "String")
    @Length(min = 6, max = 8, message = "{validation.length.6.8}") @NotNull(message = "{validation.campo.not.null}")
    private String password;

    public UserDTO(){

    }

    private UserDTO(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.password = user.getPassword();
    }

    public static UserDTO createUserDTOWith(User userSave) {
        return new UserDTO(userSave);
    }
}
