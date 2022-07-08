package com.servico.autenticacao.domains;

import com.servico.autenticacao.infra.jsons.UserLogin;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class Login extends UserLogin {

    public UsernamePasswordAuthenticationToken converter() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }

}
