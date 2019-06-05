package com.maquinadebusca.app.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.Usuario;
import com.maquinadebusca.app.model.service.UsuarioService;
import java.awt.PageAttributes.MediaType;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 *
 * @author Lucas Silva
 */
@RestController
public class CadastroUsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @RequestMapping(value = "/cadastrarUsuario", method = RequestMethod.GET)
    public String form() {
        System.out.println("\n>>> TESTE\n");
        return "/usuario";
    }

    /*@RequestMapping(value = "/cadastrarUsuario", method = RequestMethod.POST)
    public String form(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/cadastrarUsuario";
        }

        usuarioService.save(usuario);
        attributes.addFlashAttribute("mensagem", "Usuario cadastrado com sucesso!");
        return "redirect:/cadastrarUsuario";
    }*/
     @PostMapping(value = "/usuario")
   // @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public ResponseEntity cadastrarUsuario(@RequestBody @Valid Usuario usuario, BindingResult resultado) {
        ResponseEntity resposta = null;
        if (resultado.hasErrors()) {
            resposta = new ResponseEntity(new Mensagem("erro", "os dados sobre o usuario não foram informados corretamente"), HttpStatus.BAD_REQUEST);
        } else {
            usuario = usuarioService.cadastrarUsuario(usuario);
            if ((usuario != null) && (usuario.getId() > 0)) {
                resposta = new ResponseEntity(usuario, HttpStatus.OK);
            } else {
                resposta = new ResponseEntity(new Mensagem("erro", "não foi possível inserir o usuario informado no banco de dados"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return resposta;
    }
}
