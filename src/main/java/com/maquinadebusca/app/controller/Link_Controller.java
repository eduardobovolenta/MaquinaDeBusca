package com.maquinadebusca.app.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.Host;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.Pagina;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.maquinadebusca.app.model.service.ColetorService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
		try {
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
		} catch (Exception e) {
			resposta = new ResponseEntity<Object>(
					new Mensagem("erro", "não foi possível inserir o link informado no banco de dados"),
					HttpStatus.INTERNAL_SERVER_ERROR);
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

	@PostMapping(value = "/inserir-links", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

	@GetMapping(value = "/encontrar/{url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> encontrarLink(@PathVariable(value = "url") String url) {
		ResponseEntity<Object> resposta = null;
		List<Link> links = coletorService.encontrarLinkUrl(url);
		if (!links.isEmpty()) {
			resposta = new ResponseEntity<Object>(links, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		return resposta;
	}

	@GetMapping(value = "/links/ordem-alfabetica", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> listarEmOrdemAlfabetica() {
		ResponseEntity<Object> resposta = null;
		List<Link> links = coletorService.listarEmOrdemAlfabetica();
		if (!links.isEmpty()) {
			resposta = new ResponseEntity<Object>(links, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		return resposta;
	}

	// Request for: http://localhost:8080/coletor/link/pagina
	@GetMapping(value = "/link/pagina", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarPagina() {
		ResponseEntity<Object> resposta = null;
		String links = coletorService.buscarPagina();
		if (!links.isEmpty()) {
			resposta = new ResponseEntity(links, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return resposta;
	}

	@GetMapping(value = "/obter-pagina/{pagina}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getPagina(@PathVariable(value = "pagina") int pag) {
		ResponseEntity<Object> resposta = null;
		Pagina result = coletorService.obterPagina(pag);
		if (result != null) {
			resposta = new ResponseEntity(result, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return resposta;
	}

	// Request for: http://localhost:8080/coletor/link/intervalo/{id1}/{id2}
	@GetMapping(value = "/link/intervalo/{id1}/{id2}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity encontrarLinkPorIntervaloDeId(@PathVariable(value = "id1") Long id1,
			@PathVariable(value = "id2") Long id2) {
		return new ResponseEntity(coletorService.pesquisarLinkPorIntervaloDeIdentificacao(id1, id2), HttpStatus.OK);
	}

	@GetMapping(value = "/listar/{host}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listar(@PathVariable(value = "host") String host) {
		ResponseEntity<Object> resposta = null;
		Host hosts = coletorService.obterPorHost(host);
		if (hosts != null) {
			resposta = new ResponseEntity<Object>(hosts, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		return resposta;
	}

	@GetMapping(value = "/intervalo/contar/{id1}/{id2}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity contarLinkPorIntervaloDeId(@PathVariable(value = "id1") Long id1,
			@PathVariable(value = "id2") Long id2) {
		return new ResponseEntity(coletorService.contarLinkPorIntervaloDeIdentificacao(id1, id2), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/link/intervalo/{id1}/{id2}
	@GetMapping(value = "/intervalo-data/{date1}/{date2}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> encontrarLinkPorIntervaloDeData(@PathVariable(value = "date1") Date date1,
			@PathVariable(value = "date2") Date date2) {
		ResponseEntity<Object> resposta = null;
		List<Link> resultado = coletorService.pesquisarLinkPorIntervaloDeDataUltimaColeta(date1, date2);
		if (!resultado.isEmpty()) {
			resposta = new ResponseEntity(resultado, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return resposta;
	}

	// Request for: http://localhost:8080/link/ultima/coleta/{host}/{data}
	@PutMapping(value = "/link/ultima-coleta/{host}/{data}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity atualizarUltimaColeta(@PathVariable(value = "host") String host,
			@PathVariable(value = "data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data) {
		int n = coletorService.atualizarDataUltimaColeta(host, data);
		ResponseEntity resposta = new ResponseEntity(new Mensagem("sucesso", "número de registros atualizados: "),
				HttpStatus.OK);
		return resposta;
	}
}
