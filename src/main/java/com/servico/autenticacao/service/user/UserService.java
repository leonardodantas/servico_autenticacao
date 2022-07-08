package com.servico.autenticacao.service.user;

import com.servico.autenticacao.models.usuario.User;
import com.servico.autenticacao.models.usuario.dto.UserDTO;
import com.servico.autenticacao.repository.IUserRepository;
import com.servico.autenticacao.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {

    private final IUserRepository userRepository;

    public UserService(final IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO signUpUser(final UserDTO userDTO) {
        checkEmailExists(userDTO.getEmail());
        final var user = User.createSimpleUserAndGenerateUUID(userDTO);
        return saveUser(user);
    }

    private void checkEmailExists(final String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, Constants.EMAIL_ALREADY_REGISTERED));
    }

    public UserDTO getUserWithID(final String id) {
        final var user = getUserInDataBase(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constants.ID_USER_NOT_FOUND));
        return UserDTO.createUserDTOWith(user);
    }

    private Optional<User> getUserInDataBase(final String id) {
        return userRepository.findById(id);
    }

    private UserDTO saveUser(final User user) {
        final var userSave = userRepository.save(user);
        return UserDTO.createUserDTOWith(userSave);
    }
}
