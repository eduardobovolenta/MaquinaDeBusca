package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.DocumentoModel;
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

	public List<DocumentoModel> obterDocumentos() {
		Iterable<DocumentoModel> documentos = dr.findAll();
		List<DocumentoModel> resposta = new LinkedList();
		for (DocumentoModel documento : documentos) {
			resposta.add(documento);
		}
		return resposta;
	}

	public DocumentoModel obterDocumentoPorId(long id) {
		DocumentoModel documento = dr.findById(id);
		return documento;
	}

	public List<DocumentoModel> encontrarDocumentoPorTexto(String pesquisa) {
		List<Object[]> objs = dr.obterDocumento(pesquisa);
		List<DocumentoModel> docs = new ArrayList<DocumentoModel>();
		for (Object[] row : objs) {
			DocumentoModel doc = new DocumentoModel();
			doc.setTitulo(row[0].toString());
			doc.setUrl(row[1].toString());
			doc.setVisao(row[2].toString().substring(0, 240));
			docs.add(doc);
		}

		return docs;
	}

	public DocumentoModel save(DocumentoModel documento) {
		return dr.save(documento);
	}

}
