package com.servico.autenticacao.service.user;

import com.servico.autenticacao.models.usuario.User;
import com.servico.autenticacao.models.usuario.dto.UserDTO;
import com.servico.autenticacao.repository.IUserRepository;
import com.servico.autenticacao.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    public UserDTO signUpUser(UserDTO userDTO){
        checksForEmail(userDTO.getEmail());
        User user = User.createSimpleUserAndGenerateUUID(userDTO);
        return saveUser(user);
    }

    private void checksForEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, Constantes.EMAIL_ALREADY_REGISTERED);
        }
    }

    public UserDTO getUserWithID(String id){
        Optional<User> user = getUserInDataBase(id);
        if(user.isPresent()) {
            return UserDTO.createUserDTOWith(user.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constantes.ID_USER_NOT_FOUND);
    }

    private Optional<User> getUserInDataBase(String id){
        Optional<User> user = Optional.empty();
        try {
            user = userRepository.findById(id);
        } catch (Exception e){
            e.printStackTrace();
        }
        return user;
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
