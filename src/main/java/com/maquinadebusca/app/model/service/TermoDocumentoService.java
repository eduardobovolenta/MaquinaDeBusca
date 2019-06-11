package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.TermoDocumentoModel;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.maquinadebusca.app.model.repository.TermoDocumentoRepository;

@Service
public class TermoDocumentoService {

    @Autowired
    TermoDocumentoRepository tdr;

    public TermoDocumentoService() {
    }

    public TermoDocumentoModel save(TermoDocumentoModel termoDocumento) {
        return tdr.save(termoDocumento);
    }

    public double getIdf(String termoDocumento) {
        return tdr.getIdf(termoDocumento);
    }

}
