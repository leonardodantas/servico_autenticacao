package com.servico.autenticacao.controller;

import com.google.common.base.Strings;
import com.servico.autenticacao.models.usuario.dto.UserDTO;
import com.servico.autenticacao.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.HttpURLConnection;

@RestController
@RequestMapping("/v1/user")
@Api(tags = "Criação, autenticação e recuperação de senhas")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    @ApiOperation(tags = "Criação, autenticação e recuperação de senhas", value = "Criação de uma conta")
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
}
