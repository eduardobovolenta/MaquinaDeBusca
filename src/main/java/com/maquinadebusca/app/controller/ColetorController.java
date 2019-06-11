package com.maquinadebusca.app.controller;

import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.LinkModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.maquinadebusca.app.model.service.ColetorService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/coletor") // URL: http://localhost:8080/coletor
public class ColetorController {

	@Autowired
	ColetorService cs;
	

	  // URL: http://localhost:8080/coletor/iniciar
	  @GetMapping (value = "/iniciar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public ResponseEntity iniciar () {
	    return new ResponseEntity (cs.executar (), HttpStatus.OK);
	  }

	  // URL: http://localhost:8080/coletor/documento
	  @GetMapping (value = "/documento", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public ResponseEntity listarDocumento () {
	    return new ResponseEntity (cs.getDocumento (), HttpStatus.OK);
	  }

	  // Request for: http://localhost:8080/coletor/documento/{id}
	  @GetMapping (value = "/documento/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public ResponseEntity listarDocumento (@PathVariable (value = "id") Long id) {
	    return new ResponseEntity (cs.getDocumento (id), HttpStatus.OK);
	  }

}
