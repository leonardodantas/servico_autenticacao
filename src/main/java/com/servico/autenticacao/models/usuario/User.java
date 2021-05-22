package com.servico.autenticacao.models.usuario;

import com.servico.autenticacao.models.usuario.dto.UserDTO;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Table(name = "usuario")
@Entity
public class User {

    @Id
    @Column(length = 32)
    private String id;

    @Column(name = "nome", length = 120)
    private String name;

    @Column(name = "email", length = 120)
    private String email;

    @Column(name = "senha", length = 8)
    private String password;

    public User(){}

    private User(UserDTO userDTO){
        this.id = UUID.randomUUID().toString();
        this.email = userDTO.getEmail();
        this.name = userDTO.getName();
        this.password = userDTO.getPassword();
    }

    public static User createUserAndGenerateUUID(UserDTO userDTO){
        return new User(userDTO);
    }

}
