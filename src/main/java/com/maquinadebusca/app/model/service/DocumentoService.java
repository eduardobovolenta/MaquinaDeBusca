package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.Documento;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.maquinadebusca.app.model.repository.DocumentoRepository;

@Service
public class DocumentoService {

	@Autowired
	DocumentoRepository dr;

	public List<Documento> obterDocumentos() {
		Iterable<Documento> documentos = dr.findAll();
		List<Documento> resposta = new LinkedList();
		for (Documento documento : documentos) {
			resposta.add(documento);
		}
		return resposta;
	}

	public Documento obterDocumentoPorId(long id) {
		Documento documento = dr.findById(id);
		return documento;
	}

	public List<Documento> encontrarDocumentoPorTexto(String pesquisa) {
		List<Object[]> objs = dr.obterDocumento(pesquisa);
		List<Documento> docs = new ArrayList<Documento>();
		for (Object[] row : objs) {
			Documento doc = new Documento();
			doc.setTitulo(row[0].toString());
			doc.setUrl(row[1].toString());
			doc.setVisao(row[2].toString().substring(0, 240));
			docs.add(doc);
		}

		return docs;
	}

	public Documento save(Documento documento) {
		return dr.save(documento);
	}

}
