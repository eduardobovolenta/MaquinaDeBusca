package com.maquinadebusca.app.model.repository;

import com.maquinadebusca.app.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, String> {

    UsuarioModel findByLogin(String login);

    @Override
    UsuarioModel save(UsuarioModel usuario);

}
