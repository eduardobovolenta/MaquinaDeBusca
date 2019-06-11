package com.maquinadebusca.app.model;

import java.util.List;

public class PaginaModel {
	
	private int numeroPag;
	
	private int numeroElementos;
	
	private int tamanhoPag;
	
	private List<LinkModel> links;

	public int getNumeroPag() {
		return numeroPag;
	}

	public void setNumeroPag(int numeroPag) {
		this.numeroPag = numeroPag;
	}

	public int getNumeroElementos() {
		return numeroElementos;
	}

	public void setNumeroElementos(int numeroElementos) {
		this.numeroElementos = numeroElementos;
	}

	public int getTamanhoPag() {
		return tamanhoPag;
	}

	public void setTamanhoPag(int tamanhoPag) {
		this.tamanhoPag = tamanhoPag;
	}

	public List<LinkModel> getLinks() {
		return links;
	}

	public void setLinks(List<LinkModel> links) {
		this.links = links;
	}
}