package com.maquinadebusca.app.model.repository;

import com.maquinadebusca.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Usuario findByLogin(String login);

    @Override
    Usuario save(Usuario usuario);

}
