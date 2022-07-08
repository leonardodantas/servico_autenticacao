package com.servico.autenticacao.infra;

import com.servico.autenticacao.domains.Login;
import com.servico.autenticacao.domains.TokenDTO;
import com.servico.autenticacao.infra.jsons.UserDTO;
import com.servico.autenticacao.app.usecases.TokenService;
import com.servico.autenticacao.app.usecases.UserService;
import com.servico.autenticacao.app.usecases.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.net.HttpURLConnection;

@RestController
@RequestMapping("/v1/user")
@Api(tags = "AUTHORIZATION")
public class UserController {

    private final UserService userService;

    private final AuthenticationManager authManager;

    private final TokenService tokenService;

    public UserController(final UserService userService, final AuthenticationManager authManager, final TokenService tokenService) {
        this.userService = userService;
        this.authManager = authManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/signup")
    @ApiOperation(tags = "AUTHORIZATION", value = "Criação de uma conta")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_CREATED, message = "user successfully created", response = UserDTO.class),
            @ApiResponse(code = HttpURLConnection.HTTP_NO_CONTENT, message = "Error creating user"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "BAD REQUEST")
    })
    public ResponseEntity<UserDTO> signUpUser(@Valid @RequestBody final UserDTO user) {
        final var userDTO = userService.signUpUser(user);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @ApiOperation(tags = "AUTHORIZATION", value = "Login")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_CREATED, message = "Login successfully", response = TokenDTO.class),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Error creating user")
    })
    public ResponseEntity<?> loginUser(@Valid @RequestBody final Login login) {
        final var usernamePasswordAuthenticationToken = login.converter();
        try {
            final var authentication = authManager.authenticate(usernamePasswordAuthenticationToken);
            final var token = tokenService.generateToken(authentication);
            return ResponseEntity.ok(new TokenDTO(token, Constants.BEARER));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/id/user")
    @ApiOperation(tags = "AUTHORIZATION", value = "Recuperação de dados de um usuario atraves de um token")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Get token successfully", response = UserDTO.class),
            @ApiResponse(code = HttpURLConnection.HTTP_NO_CONTENT, message = "Token invalid ")
    })
    public ResponseEntity<?> generateIdUserWithToken(
            @PathParam(value = "TOKEN de usuario autenticado")
            @RequestParam final String token) {
        final var userDTO = tokenService.generateIdUserWithToken(token);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
