package com.servico.autenticacao.service;

import com.servico.autenticacao.models.usuario.User;
import com.servico.autenticacao.models.usuario.dto.UserDTO;
import com.servico.autenticacao.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    public UserDTO signUpUser(UserDTO userDTO){
        User user = User.createUserAndGenerateUUID(userDTO);
        return saveUser(user);
    }

    private UserDTO saveUser(User user){
        UserDTO userDTO = new UserDTO();
        try {
            User userSave = userRepository.save(user);
            if(!Objects.isNull(userSave)) {
                userDTO = UserDTO.createUserDTOWith(userSave);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userDTO;
    }
}
