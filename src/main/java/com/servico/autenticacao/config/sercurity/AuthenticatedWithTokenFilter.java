package com.servico.autenticacao.config.sercurity;

import com.google.common.base.Strings;
import com.servico.autenticacao.infra.repository.IUserRepository;
import com.servico.autenticacao.app.usecases.TokenService;
import com.servico.autenticacao.app.usecases.Constants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AuthenticatedWithTokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final IUserRepository userRepository;

    public AuthenticatedWithTokenFilter(final TokenService tokenService, final IUserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {

        this.getToken(request).ifPresent(token -> {
            final var valid = tokenService.isTokenValid(token);
            if (valid) {
                authenticateClient(token);
            }
        });

        filterChain.doFilter(request, response);
    }

    private void authenticateClient(final String token) {
        final var userId = tokenService.getIdUser(token);
        final var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("UserId %s not found", userId)));
        final var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Optional<String> getToken(final HttpServletRequest request) {
        final var token = request.getHeader(Constants.AUTHORIZATION);
        if (Strings.isNullOrEmpty(token) || token.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(token);
    }

}
