package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Host;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.Pagina;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.HttpStatusException;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

@Service
public class ColetorService {

	private List<Link> urlsSementes;
	private List<Link> links;
	private List<String> urlsDisallow;

	public ColetorService() {
		urlsSementes = new ArrayList<Link>();
		links = new ArrayList<Link>();
	}

	private static final int SIZEPAG = 3;

	@Autowired
	private DocumentoRepository _documentoRepository;

	@Autowired
	private LinkRepository _linkRepository;

	@Autowired
	private HostRepository _hostRepository;

	@Autowired
	private DocumentoService documentoService;

	@Autowired
	private HostService hostService;

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

	public Link salvarLink(Link link) throws Exception {
		Link l = null;
		try {
			l = _linkRepository.save(link);
		} catch (Exception e) {
			System.out.println("\n>>> Não foi possível salvar o link informado no banco de dados.\n");
			e.printStackTrace();
			throw new Exception(e.getMessage());
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

	public List<Link> obterLinks() {
		Iterable<Link> Links = _linkRepository.findAll();
		List<Link> resposta = new LinkedList<Link>();
		for (Link Link : Links) {
			resposta.add(Link);
		}
		return resposta;
	}

	public Link obterProximaUrlAColetar() {
		for (Link link : obterLinks()) {
			if (link.getUltimaColeta() == null) {
				return link;
			}
		}
		return null;
	}

	public List<Documento> executar() {
		try {
			boolean existeLink = false;
			do {
				Link link = obterProximaUrlAColetar();
				if (link != null && documentoService.obterDocumentos().size() <= 10) {
					this.coletar(link);
					existeLink = true;
				} else {
					existeLink = false;
				}
			} while (existeLink);

		} catch (Exception e) {
			System.out.println("\n\n\n Erro ao executar o serviço de coleta! \n\n\n");
			e.printStackTrace();
		}

		return documentoService.obterDocumentos();
	}

	public Documento coletar(Link link) throws InterruptedException {
		String urlDocumento = link.getUrl();
		Documento documento = new Documento();

		try {
			link = verificaUltimaColetaURL(urlDocumento);

			if (link.isPodeColetar()) {
				urlsDisallow = recuperaRobots(urlDocumento);
				Document d = Jsoup.connect(urlDocumento).get();
				Elements urls = d.select("a[href]");

				documento.setUrl(urlDocumento);
				documento.setTexto(d.html());
				documento.setVisao(retiraStopWords(d.text()));
				documento.setTitulo(recuperaTitulo(d));

				link.setUltimaColeta(LocalDateTime.now());

				int i = 0;
				for (Element url : urls) {
					i++;
					String u = url.attr("abs:href");
					Link linkEncontrado = null;
					if ((!u.equals("")) && (u != null) && verificaUrlAllow(u)) {
						if (!verificaLinkExistente(u) && !u.equals(urlDocumento)) {
							linkEncontrado = new Link();
							linkEncontrado.setUrl(u);
							linkEncontrado.setUltimaColeta(null);
							links.add(linkEncontrado);
							documento.addLink(linkEncontrado);
						}
					}
				}
				System.out.println("Número de links coletados: " + i);

				URL urlH = new URL(urlDocumento);
				hostService.addLink(documento, urlH.getHost());

				if (link.getDocumento() == null) {
					link.setDocumento(documento);
					salvarLink(link);
				}
			}
		} catch (Exception e) {
			System.out.println("\n\n\n Erro ao coletar a página! \n\n\n");
			e.printStackTrace();
		}

		new Thread();
		Thread.sleep(1000);
		return documento;
	}
	
	private String retiraStopWords(String text) {
		List<String> stopWords = lerStopWords();
		for (String stopWord : stopWords) {
			text.replace(stopWord, "");
		}
		return text;
	}
	
	public Boolean verificaLinkExistente(String url) {
		for (Link link : getLinks()) {
			if (link.getUrl().equals(url)) {
				return true;
			}
		}

		return false;
	}
	
	public List<Link> getLinks() {
		Iterable<Link> Links = _linkRepository.findAll();
		List<Link> resposta = new LinkedList<Link>();
		for (Link Link : Links) {
			resposta.add(Link);
		}
		return resposta;
	}
	
	public List<String> lerStopWords() {
		String palavra;
		List<String> stopWords = new LinkedList<String>();
		try {
			FileReader fr = new FileReader("stopwords/stopwords.txt");
			BufferedReader br = new BufferedReader(fr);
			while ((palavra = br.readLine()) != null) {
				stopWords.add(palavra.toLowerCase().trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return stopWords;
	}
	
	public List<String> recuperaRobots(String url_str) throws MalformedURLException {
		URL url = new URL(url_str);
		String host = url.getProtocol() + "://" + url.getHost();
		List<String> urlsDisallow = null;
		try {
			urlsDisallow = new ArrayList<String>();
			Document d = Jsoup.connect(host.concat("/robots.txt")).get();
			String[] urlsDisallowStr = d.text().split("Disallow:");
			for (String urlD : urlsDisallowStr) {
				if (!urlD.contains("Allow") && !urlD.isEmpty()) {
					urlsDisallow.add(host.concat(urlD.trim()));
				}
			}
		} catch (HttpStatusException e) {
			if (e.getStatusCode() == 404) {
				System.out.println("\n\n\n Erro ao coletar a página Robots TXT! Página não encontrada! \n\n\n");
				return urlsDisallow;
			}
		} catch (Exception e) {
			System.out.println("\n\n\n Erro ao coletar a página Robots TXT! \n\n\n");
			e.printStackTrace();
		}

		return urlsDisallow;
	}
	
	public Link verificaUltimaColetaURL(String urlDocumento) {
		Link link = getByLink(urlDocumento);
		if (link == null) {
			link = new Link();
			link.setPodeColetar(true);
		}

		if (link.getUltimaColeta() != null) {
			link.setPodeColetar(false);
		} else {
			link.setPodeColetar(true);
		}

		link.setUrl(urlDocumento);

		return link;
	}
	
	public Link getByLink(String url) {
		Link l = _linkRepository.findByUrl(url);
		return l;
	}

	private String recuperaTitulo(Document d) { 
		Elements urls = d.select("title"); 
		return urls.tagName("title").get(0).childNodes().get(0).toString(); 
	}

	private boolean verificaUrlAllow(String u) {
		for (String url : urlsDisallow) {
			if (u.contains(url)) {
				return false;
			}
		}
		return true;
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
