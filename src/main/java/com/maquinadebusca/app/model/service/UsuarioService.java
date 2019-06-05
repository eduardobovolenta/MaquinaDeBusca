package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.Usuario;
import com.maquinadebusca.app.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lucas
 */
@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastrarUsuario(Usuario usuario) {
        Usuario user = null;
        try {
            user = usuarioRepository.save(usuario);
        } catch (Exception e) {
            System.out.println("\n>>> Não foi possível cadastrar o usuario informado no banco de dados.\n");
            e.printStackTrace();
        }
        return user;
    }
    
}
