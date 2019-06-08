package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.Usuario;
import com.maquinadebusca.app.model.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
	

	
    @Autowired
    private UsuarioRepository _usuarioRepository;
    

    public Usuario cadastrarUsuario(Usuario usuario) {
        Usuario user = null;
        try {
            user = _usuarioRepository.save(usuario);
        } catch (Exception e) {
            System.out.println("\n>>> Não foi possível cadastrar o usuario informado no banco de dados.\n");
            e.printStackTrace();
        }
        return user;
    }
    
    public Boolean verificaPermissao(String login, HttpMethod metodo) {  
		Usuario usuario = obterUsuarioPorUsuario(login);
//		if(!metodo.equals(HttpMethod.GET) && !usuario.getPermissao().equals(PermissaoEnum.Permissao.ADMIN.getCode())) {
//			return false;
//		}
		
		return true;
	}
    
    public Usuario obterUsuarioPorUsuario(String login) { 
		for (Usuario usuario : _usuarioRepository.findAll()) {
			if (usuario.getLogin().equals(login)) {
				return usuario;
			}
		}
		
		return null;
	}
    
    public List<Usuario> obterUsuarios() {
		Iterable<Usuario> usuarios = _usuarioRepository.findAll();
		List<Usuario> resposta = new ArrayList<Usuario>();
		for (Usuario usuario : usuarios) {
			resposta.add(usuario);
		}
		return resposta;
	}



    
    
    
}
