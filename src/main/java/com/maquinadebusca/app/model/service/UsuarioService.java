package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.MyUserPrincipalModel;
import com.maquinadebusca.app.model.UsuarioModel;
import com.maquinadebusca.app.model.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService  {
	

	
    @Autowired
    private UsuarioRepository _usuarioRepository;
    

    public UsuarioModel cadastrarUsuario(UsuarioModel usuario) {
        UsuarioModel user = usuario;
        try {
            user = _usuarioRepository.save(user);
        } catch (Exception e) {
            System.out.println("\n>>> Não foi possível cadastrar o usuario informado no banco de dados.\n");
            e.printStackTrace();
        }
        return user;
    }
    
    public Boolean verificaPermissao(String login, HttpMethod metodo) {  
		UsuarioModel usuario = obterUsuarioPorUsuario(login);
//		if(!metodo.equals(HttpMethod.GET) && !usuario.getPermissao().equals(PermissaoEnum.Permissao.ADMIN.getCode())) {
//			return false;
//		}
		
		return true;
	}
    
    public UsuarioModel obterUsuarioPorUsuario(String login) { 
		for (UsuarioModel usuario : _usuarioRepository.findAll()) {
			if (usuario.getLogin().equals(login)) {
				return usuario;
			}
		}
		
		return null;
	}
    
    public List<UsuarioModel> obterUsuarios() {
		Iterable<UsuarioModel> usuarios = _usuarioRepository.findAll();
		List<UsuarioModel> resposta = new ArrayList<UsuarioModel>();
		for (UsuarioModel usuario : usuarios) {
			resposta.add(usuario);
		}
		return resposta;
	}

    
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 
		for (UsuarioModel usuario : _usuarioRepository.findAll()) {
			if (usuario.getLogin().equals(username)) {
				return new MyUserPrincipalModel(usuario);
			}
		}
		throw new UsernameNotFoundException(username); 
	}
    
    
    
}
