package com.servico.autenticacao.repository;

import com.servico.autenticacao.models.usuario.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {
}
