package com.maquinadebusca.app.controller;

import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.maquinadebusca.app.model.service.ColetorService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/link") // URL: http://localhost:8080/link
public class Link_Controller {

	@Autowired
	ColetorService coletorService;

	// URL: http://localhost:8080/coletor/link
	@GetMapping(value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarLink() {
		return new ResponseEntity(coletorService.getLink(), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/coletor/link/{id}
	@GetMapping(value = "/link/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarLink(@PathVariable(value = "id") Long id) {
		return new ResponseEntity(coletorService.getLink(id), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/coletor/link
	@PostMapping(value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity inserirLink(@RequestBody @Valid Link link, BindingResult resultado) {
		ResponseEntity resposta = null;
		if (resultado.hasErrors()) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o link  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			link = coletorService.salvarLink(link);
			if ((link != null) && (link.getId() > 0)) {
				resposta = new ResponseEntity(link, HttpStatus.OK);
			} else {
				resposta = new ResponseEntity(
						new Mensagem("erro", "não foi possível inserir o link informado no banco de dados"),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return resposta;
	}

	// Request for: http://localhost:8080/coletor/link
	@PutMapping(value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity atualizarLink(@RequestBody @Valid Link link, BindingResult resultado) {
		ResponseEntity resposta = null;
		if (resultado.hasErrors()) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o link  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			link = coletorService.atualizarLink(link);
			if ((link != null) && (link.getId() > 0)) {
				resposta = new ResponseEntity(link, HttpStatus.OK);
			} else {
				resposta = new ResponseEntity(
						new Mensagem("erro", "não foi possível atualizar o link informado no banco de dados"),
						HttpStatus.NOT_ACCEPTABLE);
			}
		}
		return resposta;
	}

	// Request for: http://localhost:8080/coletor/link
	@DeleteMapping(value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity removerLink(@RequestBody @Valid Link link, BindingResult resultado) {
		ResponseEntity resposta = null;
		if (resultado.hasErrors()) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o link  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			link = coletorService.removerLink(link);
			if (link != null) {
				resposta = new ResponseEntity(new Mensagem("sucesso", "link removido com suceso"), HttpStatus.OK);
			} else {
				resposta = new ResponseEntity(
						new Mensagem("erro", "não foi possível remover o link informado no banco de dados"),
						HttpStatus.NOT_ACCEPTABLE);
			}
		}
		return resposta;
	}

	// Request for: http://localhost:8080/coletor/link
	@DeleteMapping(value = "/link/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity removerLink(@PathVariable(value = "id") Long id) {
		ResponseEntity resposta = null;
		if ((id != null) && (id <= 0)) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o link  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			boolean resp = coletorService.removerLink(id);
			if (resp == true) {
				resposta = new ResponseEntity(new Mensagem("sucesso", "link removido com suceso"), HttpStatus.OK);
			} else {
				resposta = new ResponseEntity(
						new Mensagem("erro", "não foi possível remover o link informado no banco de dados"),
						HttpStatus.NOT_ACCEPTABLE);
			}
		}
		return resposta;
	}
	
	@PostMapping(value = "/addLinks", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> inserirLinks(@RequestBody @Valid Iterable<Link> links, BindingResult resultado) {
		ResponseEntity<Object> resposta = null;
		try {
			if (resultado.hasErrors()) {
				resposta = new ResponseEntity<Object>(
						new Mensagem("erro", "Os dados não foram informados corretamente!"),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				Iterable<Link> l = coletorService.salvarLinks(links);
				if (l != null) {
					resposta = new ResponseEntity<Object>(l, HttpStatus.OK);
				} else {
					resposta = new ResponseEntity<Object>(
							new Mensagem("erro", "não foi possível inserir o link informado no banco de dados"),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		} catch (Exception e) {
			resposta = new ResponseEntity<Object>(
					new Mensagem("erro", "não foi possível inserir o link informado no banco de dados"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return resposta;
	}
}
