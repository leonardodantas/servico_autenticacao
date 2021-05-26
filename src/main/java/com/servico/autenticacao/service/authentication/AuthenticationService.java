package com.servico.autenticacao.service.authentication;

import com.servico.autenticacao.models.usuario.User;
import com.servico.autenticacao.repository.IUserRepository;
import com.servico.autenticacao.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            return user.get();
        }
        throw new UsernameNotFoundException(Constantes.INVALID_DATA);
    }
}
