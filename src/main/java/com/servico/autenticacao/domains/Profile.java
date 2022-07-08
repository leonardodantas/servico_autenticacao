package com.servico.autenticacao.domains;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "profile")
public class Profile implements GrantedAuthority {

    @Id
    private String id;

    @Column(name = "description", length = 40)
    private String description;

    @Override
    public String getAuthority() {
        return description;
    }

    public Profile(){}

    public Profile(final String id){
        this.id = id;
    }
}
