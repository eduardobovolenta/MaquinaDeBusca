package com.maquinadebusca.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "Link")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") 
public class LinkModel implements Serializable {

	static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	@Column(unique=true, nullable=false, length = 999) 
	private String url;

	@Basic
	private LocalDateTime ultimaColeta;

	@Transient
	private boolean podeColetar;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "documentoId")
	private DocumentoModel documentoId; 
	
	public LinkModel() {
		this.podeColetar = true;
	}

	public LinkModel(String url, DocumentoModel documento) {
		this.url = url;
		this.ultimaColeta = null;
		this.documentoId = documento;
		this.podeColetar = true;
	}

	public boolean isPodeColetar() {
		return podeColetar;
	}

	public void setPodeColetar(boolean podeColetar) {
		this.podeColetar = podeColetar;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public LocalDateTime getUltimaColeta() {
		return ultimaColeta;
	}

	public void setUltimaColeta(LocalDateTime ultimaColeta) {
		this.ultimaColeta = ultimaColeta;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DocumentoModel getDocumento() {
		return documentoId;
	}

	public void setDocumento(DocumentoModel documento) {
		this.documentoId = documento;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 71 * hash + Objects.hashCode(this.id);
		hash = 71 * hash + Objects.hashCode(this.url);
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
		final LinkModel other = (LinkModel) obj;
		if (!Objects.equals(this.url, other.url)) {
			return false;
		}
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}
}