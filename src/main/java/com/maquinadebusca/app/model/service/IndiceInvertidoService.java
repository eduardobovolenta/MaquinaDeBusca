package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.IndiceInvertidoModel;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.maquinadebusca.app.model.repository.IndiceInvertidoRepository;

@Service
public class IndiceInvertidoService {

    @Autowired
    IndiceInvertidoRepository iir;

    public IndiceInvertidoService() {
    }

    public List<IndiceInvertidoModel> getEntradasIndiceInvertido(String termo) {
        return iir.getEntradasIndiceInvertido(termo);
    }

}
