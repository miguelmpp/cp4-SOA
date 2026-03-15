package br.com.fiap3espa.auto_escola_3espa.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    UserDetails findByLogin(String login);

    Optional<Usuario> findUsuarioByLogin(String login);

    boolean existsByLogin(String login);
}