package com.servico.autenticacao.config.sercurity;

import com.google.common.base.Strings;
import com.servico.autenticacao.models.usuario.User;
import com.servico.autenticacao.repository.IUserRepository;
import com.servico.autenticacao.service.authentication.TokenService;
import com.servico.autenticacao.utils.Constantes;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticatedWithTokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final IUserRepository userRepository;

    public AuthenticatedWithTokenFilter(TokenService tokenService, IUserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = getToken(request);
        boolean valid = tokenService.isTokenValid(token);
        if (valid) {
            authenticateClient(token);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateClient(String token) {
        String idUser = tokenService.getIdUser(token);
        User user = userRepository.findById(idUser).get();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(Constantes.AUTHORIZATION);
        if (Strings.isNullOrEmpty(token) || token.isEmpty()) {
            return null;
        }
        return token;
    }

}
