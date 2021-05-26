package com.servico.autenticacao.models.login;

import com.servico.autenticacao.models.usuario.dto.UserLogin;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class Login extends UserLogin {

    public UsernamePasswordAuthenticationToken converter() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }

}
