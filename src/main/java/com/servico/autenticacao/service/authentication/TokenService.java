package com.servico.autenticacao.service.authentication;

import com.servico.autenticacao.models.usuario.User;
import com.servico.autenticacao.models.usuario.dto.UserDTO;
import com.servico.autenticacao.service.user.UserService;
import com.servico.autenticacao.utils.Constants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
public class TokenService {

    private final UserService userService;

    private final String expiration;

    private final String secret;

    private final String password;

    public TokenService(final UserService userService,
                        @Value("${jwt.expiration}") final String expiration,
                        @Value("${jwt.secret}") final String secret,
                        @Value("${jwt.password}") final String password) {
        this.userService = userService;
        this.expiration = expiration;
        this.secret = secret;
        this.password = password;
    }

    public String generateToken(final Authentication authentication) {
        final var logged = (User) authentication.getPrincipal();
        final var today = new Date();
        final var expirationDate = new Date(today.getTime() + Long.parseLong(expiration));

        return Jwts.builder()
                .setIssuer("API User authorization")
                .claim("password", password)
                .setSubject(logged.getId())
                .setIssuedAt(today)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public UserDTO generateIdUserWithToken(final String token) {
        final var userId = getUserId(token);
        return userService.getUserWithID(userId);
    }

    private String getUserId(final String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, Constants.TOKEN_INVALID_FORMAT);
        }
    }

    public boolean isTokenValid(final String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getIdUser(final String token) {
        final var claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return String.valueOf(claims.getSubject());
    }
}
