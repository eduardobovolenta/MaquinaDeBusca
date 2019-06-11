package com.maquinadebusca.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;


@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class HostModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	@Column(unique=true, nullable=false, length = 200) 
	private String host;
	 
	private int qtdPaginas;
	
	@OneToMany(mappedBy = "host", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<DocumentoModel> documentos;

	public HostModel(Long id, String host, Integer qtdPaginas, Set<LinkModel> links) {
		this.id = id;
		this.host = host;
		this.qtdPaginas = qtdPaginas;
		this.documentos = new HashSet();
	}

	public HostModel() {
		this.documentos = new HashSet();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getQtdPaginas() {
		return qtdPaginas;
	}

	public void setQtdPaginas(int qtdPaginas) {
		this.qtdPaginas = qtdPaginas;
	}

	public Set<DocumentoModel> getDocumentos() {
		return documentos;
	}

	public void setDocumento(Set<DocumentoModel> documento) {
		this.documentos = documento;
	} 
	
	public void addDocumento(DocumentoModel documento) {
		documento.setHost(this);
		this.documentos.add(documento);
	} 

	public void removeDocumento(DocumentoModel documento) {
		documento.setHost(null);
		documentos.remove(documento);
	}
}