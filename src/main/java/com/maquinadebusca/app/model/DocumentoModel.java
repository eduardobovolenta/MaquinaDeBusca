package com.maquinadebusca.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DocumentoModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String url;

	@Lob
	@NotBlank
	private String texto;

	@Lob
	@NotBlank
	private String visao;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "host_id")
	private HostModel host;

	@NotBlank
	@Column(nullable = false, length = 200)
	private String titulo;

	@OneToMany(mappedBy = "documento", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<LinkModel> links;
	
	@OneToMany(
            mappedBy = "documentoId", // Nome do atributo na classe IndiceInvertido.
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<IndiceInvertidoModel> indiceInvertido;
	
	private double frequenciaMaxima;

	private double somaQuadradosPesos;

	public DocumentoModel(String url, String texto, String visao, String titulo) {
		this.url = url;
		this.texto = texto;
		this.visao = visao;
		this.titulo = titulo;
		this.links = new HashSet();
		this.indiceInvertido = new LinkedList();
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public DocumentoModel() {
		links = new HashSet();
	}

	public String getTexto() {
		return texto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getVisao() {
		return visao;
	}

	public void setVisao(String visao) {
		this.visao = visao;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<LinkModel> getLinks() {
		return links;
	}

	public void setLinks(Set<LinkModel> links) {
		this.links = links;
	}

	public void addLink(LinkModel link) {
		link.setDocumento(this);
		this.links.add(link);
	}

	public void removeLink(LinkModel link) {
		link.setDocumento(null);
		links.remove(link);
	}

	public HostModel getHost() {
		return host;
	}

	public void setHost(HostModel host) {
		this.host = host;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 59 * hash + Objects.hashCode(this.id);
		hash = 59 * hash + Objects.hashCode(this.url);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		final DocumentoModel other = (DocumentoModel) obj;
		if (!Objects.equals(this.url, other.url)) {
			return false;
		}

		if (!Objects.equals(this.id, other.id)) {
			return false;
		}

		return true;
	}

	public double getFrequenciaMaxima() {
		return frequenciaMaxima;
	}

	public void setFrequenciaMaxima(double frequenciaMaxima) {
		this.frequenciaMaxima = frequenciaMaxima;
	}

	public double getSomaQuadradosPesos() {
		return somaQuadradosPesos;
	}

	public void setSomaQuadradosPesos(double somaQuadradosPesos) {
		this.somaQuadradosPesos = somaQuadradosPesos;
	}

//	public double getSimilaridade() {
//		return similaridade;
//	}
//
//	public void setSimilaridade(double similaridade) {
//		this.similaridade = similaridade;
//	}

	public List<IndiceInvertidoModel> getIndiceInvertido() {
		return indiceInvertido;
	}

	public void setIndiceInvertido(List<IndiceInvertidoModel> indiceInvertido) {
		this.indiceInvertido = indiceInvertido;
	}

	public void inserirTermo(TermoDocumentoModel termo) {
		IndiceInvertidoModel entradaIndiceInvertido = new IndiceInvertidoModel(termo, this); // Cria uma nova entrada para o
																					// índice invertido com o termo
																					// informado como parâmetro e com o
																					// documento corrente.
		this.indiceInvertido.add(entradaIndiceInvertido); // Insere a nova entrada no índice invertido do documento
															// corrente.
		termo.getIndiceInvertido().add(entradaIndiceInvertido); // Insere a nova entrada no índice invertido do termo
																// que foi informado como parâmetro.
	}

	public void removeTermo(TermoDocumentoModel termo) {
		Iterator<IndiceInvertidoModel> iterator = this.indiceInvertido.iterator();
		while (iterator.hasNext()) {
			IndiceInvertidoModel entradaIndiceInvertido = iterator.next();
			if (entradaIndiceInvertido.getTermo().equals(termo) && entradaIndiceInvertido.getDocumento().equals(this)) {
				iterator.remove(); // Remoção no Banco de Dados a partir da tabela Documento.
				entradaIndiceInvertido.getTermo().getIndiceInvertido().remove(entradaIndiceInvertido); // Remoção no
																									// TermoDocumento.
				entradaIndiceInvertido.setDocumento(null); // Remoção na memória RAM.
				entradaIndiceInvertido.setTermo(null); // Remoção na memória RAM.
			}
		}
	}
}
