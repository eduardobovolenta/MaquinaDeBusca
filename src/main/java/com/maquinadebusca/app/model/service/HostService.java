package com.maquinadebusca.app.model.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Transient;

import org.hibernate.mapping.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.maquinadebusca.app.model.DocumentoModel;
import com.maquinadebusca.app.model.HostModel;
import com.maquinadebusca.app.model.LinkModel;
import com.maquinadebusca.app.model.repository.HostRepository; 

@Service
public class HostService {

	@Autowired
	private HostRepository hr;

	public HostModel addHost(HostModel host) {
		return hr.save(host);
	}

	public List<HostModel> getHosts() {
		return hr.findAll();
	}

	public HostModel getHostById(long id) {
		HostModel host = hr.findById(id);
		return host;
	}

	public HostModel getByHost(String hostUrl) {
		for (HostModel host : getHosts()) {
			if (host.getHost().equals(hostUrl)) {
				return host;
			}
		}

		return null;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public HostModel addLink(DocumentoModel documento, String hostUrl) {
		HostModel host = getByHost(hostUrl); 
		
		if(host == null) { 
			host = new HostModel();
			host.setHost(hostUrl);
			host.getDocumentos().add(documento);
			host.setQtdPaginas(1);
		}else {
			host.getDocumentos().add(documento);
			host.setQtdPaginas(host.getQtdPaginas() + 1);   
		}
		
		host.addDocumento(documento); 
		
		return hr.save(host);
	} 
	
}