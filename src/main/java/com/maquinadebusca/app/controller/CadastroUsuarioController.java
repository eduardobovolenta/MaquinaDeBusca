package com.maquinadebusca.app.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.Usuario;
import com.maquinadebusca.app.model.service.UsuarioService;
import java.awt.PageAttributes.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@RestController
public class CadastroUsuarioController {

    @Autowired
    UsuarioService usuarioService;
    
    private static final String HEADER_STRING = "Authorization"; 
    
     @PostMapping(value = "/usuario")
     // @JsonDeserialize(using = LocalDateTimeDeserializer.class)
     public ResponseEntity<Object> inserirUser(@RequestBody @Valid Usuario user, BindingResult resultado,
 			HttpServletRequest request) {
 		ResponseEntity<Object> resposta = null;
 		try {
 			String token = request.getHeader(HEADER_STRING);
 			if (resultado.hasErrors()) {
 				resposta = new ResponseEntity(
 						new Mensagem("erro", "Dados informados errados."),
 						HttpStatus.INTERNAL_SERVER_ERROR);
 			} else if (!usuarioService.verificaPermissao(Util.getUser(token), HttpMethod.POST)) {
 				resposta = new ResponseEntity<Object>(
 						new Mensagem("erro", "Sem permissão para executar esta ação."),
 						HttpStatus.NON_AUTHORITATIVE_INFORMATION);
 			} else {
 				user = usuarioService.criarUsuario(user);
 				if ((user != null) && (user.getId() > 0)) {
 					resposta = new ResponseEntity<Object>(user, HttpStatus.OK);
 				} else {
 					resposta = new ResponseEntity<Object>(new Mensagem("erro",
 							"não foi possível inserir o usuario informado no banco de dados"),
 							HttpStatus.INTERNAL_SERVER_ERROR);
 				}
 			}
 		} catch (Exception e) {
 			resposta = new ResponseEntity<Object>(new Mensagem("erro",
 					"não foi possível inserir o usuario informado no banco de dados"),
 					HttpStatus.INTERNAL_SERVER_ERROR);
 		}

 		return resposta;
 	}
}
