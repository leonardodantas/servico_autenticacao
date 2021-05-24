package com.servico.autenticacao.models.perfil;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Table(name = "perfil")
@Entity
public class Profile implements GrantedAuthority {

    @Id
    private String id;

    @Column(name = "descricao", length = 40)
    private String description;

    @Override
    public String getAuthority() {
        return description;
    }

    public Profile(){}

    public Profile(String id){
        this.id = id;
    }
}
