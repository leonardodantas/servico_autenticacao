package com.servico.autenticacao.models.usuario;

import com.servico.autenticacao.models.perfil.Profile;
import com.servico.autenticacao.models.usuario.dto.UserDTO;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.*;

@Getter
@Entity
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "name", length = 120)
    private String name;

    @Column(name = "email", length = 120)
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Profile> profiles = new ArrayList<>();

    public User(){}

    private User(UserDTO userDTO){
        this.id = UUID.randomUUID().toString();
        this.email = userDTO.getEmail();
        this.name = userDTO.getName();
        this.password = new BCryptPasswordEncoder().encode(userDTO.getPassword());
        this.profiles = Collections.singletonList(new Profile("1"));
    }

    public static User createSimpleUserAndGenerateUUID(final UserDTO userDTO){
        return new User(userDTO);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return profiles;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
