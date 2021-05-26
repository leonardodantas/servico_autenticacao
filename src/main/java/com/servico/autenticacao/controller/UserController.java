package com.servico.autenticacao.controller;

import com.google.common.base.Strings;
import com.servico.autenticacao.models.login.Login;
import com.servico.autenticacao.models.token.TokenDTO;
import com.servico.autenticacao.models.usuario.dto.UserDTO;
import com.servico.autenticacao.service.authentication.TokenService;
import com.servico.autenticacao.service.user.UserService;
import com.servico.autenticacao.utils.Constantes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.net.HttpURLConnection;
import java.util.Objects;

@RestController
@RequestMapping("/v1/user")
@Api(tags = "Criação e autenticação de usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/signup")
    @ApiOperation(tags = "Criação e autenticação de usuarios", value = "Criação de uma conta")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_CREATED, message = "user successfully created", response = UserDTO.class),
            @ApiResponse(code = HttpURLConnection.HTTP_NO_CONTENT, message = "Error creating user"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "BAD REQUEST")
    })
    public ResponseEntity<UserDTO> signUpUser(@Valid @RequestBody UserDTO user){
        UserDTO userDTO = userService.signUpUser(user);
        if(Strings.isNullOrEmpty(userDTO.getId())) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @ApiOperation(tags = "Criação e autenticação de usuarios", value = "Login do usuario")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_CREATED, message = "Login successfully", response = TokenDTO.class),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Error creating user")
    })
    public ResponseEntity<?> loginUser(@Valid @RequestBody Login login){
        UsernamePasswordAuthenticationToken dadosLogin = login.converter();
        try {
            Authentication authentication = authManager.authenticate(dadosLogin);
            String token = tokenService.generateToken(authentication);
            return ResponseEntity.ok(new TokenDTO(token, Constantes.BEARER));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/id/user")
    @ApiOperation(tags = "Criação e autenticação de usuarios", value = "Recupera o id do usuario baseado em um token")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Get token successfully", response = UserDTO.class),
            @ApiResponse(code = HttpURLConnection.HTTP_NO_CONTENT, message = "Token invalid ")
    })
    public ResponseEntity<?> generateIdUserWithToken(
            @PathParam(value = "TOKEN de usuario autenticado")
            @RequestParam String token){
        UserDTO userDTO = tokenService.generateIdUserWithToken(token);
        if(Objects.isNull(userDTO)){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
