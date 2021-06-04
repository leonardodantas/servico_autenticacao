package com.servico.autenticacao.service.authentication;

import com.google.common.base.Strings;
import com.servico.autenticacao.models.usuario.User;
import com.servico.autenticacao.models.usuario.dto.UserDTO;
import com.servico.autenticacao.service.user.UserService;
import com.servico.autenticacao.utils.Constantes;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
public class TokenService {

    @Autowired
    private UserService userService;

    @Value("${jwt.expiration}")
    private String expiration;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.password}")
    private String password;

    public String generateToken(Authentication authentication) {
        User logged = (User) authentication.getPrincipal();
        Date today = new Date();
        Date expirationDate = new Date(today.getTime() + Long.parseLong(expiration));

        return Jwts.builder()
                .setIssuer("API User authorization")
                .claim("password", password)
                .setSubject(logged.getId())
                .setIssuedAt(today)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public UserDTO generateIdUserWithToken(String token){
        String id = convertTokenInIdUser(token);
        if(Strings.isNullOrEmpty(id)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constantes.ID_EMPTY_OR_NULL);
        }
        return userService.getUserWithID(id);
    }

    private String convertTokenInIdUser(String token){
        String id = "";
        try{
            id = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, Constantes.TOKEN_INVALID_FORMAT );
        }
        return id;
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getIdUser(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return String.valueOf(claims.getSubject());
    }
}
