package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Host;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.Pagina;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.maquinadebusca.app.model.repository.DocumentoRepository;
import com.maquinadebusca.app.model.repository.HostRepository;
import com.maquinadebusca.app.model.repository.LinkRepository;
import java.time.LocalDateTime;

@Service
public class ColetorService {

	private static final int SIZEPAG = 3;

	@Autowired
	private DocumentoRepository _documentoRepository;

	@Autowired
	private LinkRepository _linkRepository;
	
	@Autowired
	private HostRepository _hostRepository;

	public boolean removerLink(Long id) {
		boolean resp = false;
		try {
			_linkRepository.deleteById(id);
			resp = true;
		} catch (Exception e) {
			System.out.println("\n>>> Não foi possível remover o link informado no banco de dados.\n");
			e.printStackTrace();
		}
		return resp;
	}

	public Link removerLink(Link link) {
		try {
			_linkRepository.delete(link);
		} catch (Exception e) {
			link = null;
			System.out.println("\n>>> Não foi possível remover o link informado no banco de dados.\n");
			e.printStackTrace();
		}
		return link;
	}

	public Link salvarLink(Link link) {
		Link l = null;
		try {
			l = _linkRepository.save(link);
		} catch (Exception e) {
			System.out.println("\n>>> Não foi possível salvar o link informado no banco de dados.\n");
			e.printStackTrace();
		}
		return l;
	}

	public Link atualizarLink(Link link) {
		Link l = null;
		try {
			l = _linkRepository.save(link);
		} catch (Exception e) {
			System.out.println("\n>>> Não foi possível atualizar o link informado no banco de dados.\n");
			e.printStackTrace();
		}
		return l;
	}

	public List<Documento> executar() {
		List<Documento> documentos = new LinkedList();
		List<String> sementes = new LinkedList();

		try {
			sementes.add("https://www.youtube.com/");
			sementes.add("https://www.facebook.com/");
			sementes.add("https://www.twitter.com/");

			for (String url : sementes) {
				documentos.add(this.coletar(url));
			}
		} catch (Exception e) {
			System.out.println("\n\n\n Erro ao executar o serviço de coleta! \n\n\n");
			e.printStackTrace();
		}
		return documentos;
	}

	public Documento coletar(String urlDocumento) {
		Documento documento = new Documento();

		try {
			Link link = new Link();
			Document d = Jsoup.connect(urlDocumento).get();
			Elements urls = d.select("a[href]");

			documento.setUrl(urlDocumento);
			documento.setTexto(d.html());
			documento.setVisao(d.text());

			link.setUrl(urlDocumento);
			link.setUltimaColeta(LocalDateTime.now());
			link.addDocumento(documento);
			documento.addLink(link);
			int i = 0;
			for (Element url : urls) {
				i++;
				String u = url.attr("abs:href");
				if ((!u.equals("")) && (u != null)) {
					link = _linkRepository.findByUrl(u);
					if (link == null) {
						link = new Link();
						link.setUrl(u);
						link.setUltimaColeta(null);
					}
					link.addDocumento(documento);
					documento.addLink(link);
				}
			}
			System.out.println("Número de links coletados: " + i);
			System.out.println("Tamanho da lista links: " + documento.getLinks().size());
			// Salvar o documento no banco de dados.
			documento = _documentoRepository.save(documento);
		} catch (Exception e) {
			System.out.println("\n\n\n Erro ao coletar a página! \n\n\n");
			e.printStackTrace();
		}
		return documento;
	}

	public List<Documento> getDocumento() {
		Iterable<Documento> documentos = _documentoRepository.findAll();
		List<Documento> resposta = new LinkedList();
		for (Documento documento : documentos) {
			resposta.add(documento);
		}
		return resposta;
	}

	public Documento getDocumento(long id) {
		Documento documento = _documentoRepository.findById(id);
		return documento;
	}

	public List<Link> getLink() {
		Iterable<Link> links = _linkRepository.findAll();
		List<Link> resposta = new LinkedList();
		for (Link link : links) {
			resposta.add(link);
		}
		return resposta;
	}

	public Link getLink(long id) {
		Link link = _linkRepository.findById(id);
		return link;
	}

	public List<Link> salvarLinks(Iterable<Link> links) throws Exception {
		List<Link> l = null;
		try {
			l = _linkRepository.saveAll(links);
		} catch (Exception e) {
			System.out.println("\n>>> Não foi possível salvar o link informado no banco de dados.\n");
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return l;
	}

	public List<Link> encontrarLinkUrl(String url) {
		return _linkRepository.findByUrlIgnoreCaseContaining(url);
	}

	public List<Link> listarEmOrdemAlfabetica() {
		return _linkRepository.getInLexicalOrder();
	}

	public String buscarPagina() {
		Slice<Link> pagina = null;
		Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "url"));

		while (true) {
			pagina = _linkRepository.getPage(pageable);
			int numeroDaPagina = pagina.getNumber();
			int numeroDeElementosNaPagina = pagina.getNumberOfElements();
			int tamanhoDaPagina = pagina.getSize();
			System.out.println("\n\nPágina: " + numeroDaPagina + " Número de Elementos: " + numeroDeElementosNaPagina
					+ " Tamaho da Página: " + tamanhoDaPagina);
			List<Link> links = pagina.getContent();
			links.forEach(System.out::println);
			if (!pagina.hasNext()) {
				break;
			}
			pageable = pagina.nextPageable();
		}
		return "{\"resposta\": \"Ok\"}";
	}

	public Pagina obterPagina(int pag) {
		Slice<Link> pagina = null;
		Pageable pageable = PageRequest.of(pag, SIZEPAG, Sort.by(Sort.Direction.DESC, "url"));

		Pagina resultadoPag = new Pagina();
		pagina = _linkRepository.getPage(pageable);
		if (pagina != null) {
			resultadoPag.setTamanhoPag(pagina.getSize());
			resultadoPag.setNumeroElementos(pagina.getNumberOfElements());
			resultadoPag.setNumeroPag(pagina.getNumber());
			resultadoPag.setLinks(pagina.getContent());
		}

		return resultadoPag;
	}

	public List<Link> pesquisarLinkPorIntervaloDeIdentificacao(Long id1, Long id2) {
		return _linkRepository.findLinkByIdRange(id1, id2);
	}
	
	public List<Host> obterPorHost() {
		return _hostRepository.findAll();
	}
	
	public Host obterPorHost(String hostUrl) {
		for (Host host : obterPorHost()) {
			if (host.getHost().equals(hostUrl)) {
				return host;
			}
		}

		return null;
	}
	
	public Long contarLinkPorIntervaloDeIdentificacao(Long id1, Long id2) {
		return _linkRepository.countLinkByIdRange(id1, id2);
	}

	public List<Link> pesquisarLinkPorIntervaloDeDataUltimaColeta(Date date1, Date date2) {
		return _linkRepository.LinkByDateColetaRange(date1, date2);
	}

	public int atualizarDataUltimaColeta(String host, LocalDateTime dataUltimaColeta) {
		return _linkRepository.updateLastCrawlingDate(dataUltimaColeta, host);
	}
}
