package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.ConsultaModel;
import com.maquinadebusca.app.model.EntradaRankingModel;
import com.maquinadebusca.app.model.IndiceInvertidoModel;
import com.maquinadebusca.app.model.TermoConsultaModel;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ProcessadorConsultaService {

    @Autowired
    TermoDocumentoService ts;

    @Autowired
    DocumentoService ds;

    @Autowired
    IndiceInvertidoService iis;

    @Autowired
    IndexadorService is;

    private Map<String, EntradaRankingModel> mergeListasInvertidas = new Hashtable();

    public ProcessadorConsultaService() {
    }

    public ConsultaModel processarConsulta(String textoConsulta) {
        ConsultaModel consulta = new ConsultaModel(textoConsulta);
        this.iniciarTermosConsulta(consulta);
        this.processarListasInvertidas(consulta);
        this.computarSimilaridade();
        consulta.setRanking(this.getRanking());
        return consulta;
    }

    public void iniciarTermosConsulta(ConsultaModel consulta) {
        String visaoConsulta = consulta.getVisao();
        String[] termos = visaoConsulta.split(" ");
        for (String termo : termos) {
            if (!termo.equals("")) {
                int f = is.frequencia(termo, termos);
                double idf = ts.getIdf(termo);
                TermoConsultaModel termoConsulta = new TermoConsultaModel(termo, f, idf);
                consulta.adicionarTermoConsulta(termoConsulta);
            }
        }
    }

    public void processarListasInvertidas(ConsultaModel consulta) {
        List<TermoConsultaModel> termosConsulta = consulta.getTermosConsulta();
        for (TermoConsultaModel termoConsulta : termosConsulta) {
            List<IndiceInvertidoModel> entradasIndiceInvertido = iis.getEntradasIndiceInvertido(termoConsulta.getTexto());
            for (IndiceInvertidoModel entradaIndiceInvertido : entradasIndiceInvertido) {
                if (this.mergeListasInvertidas.containsKey(entradaIndiceInvertido.getDocumento().getUrl())) {
                    EntradaRankingModel entradaRanking = this.mergeListasInvertidas.get(entradaIndiceInvertido.getDocumento().getUrl());
                    entradaRanking.adicionarProdutoPesos(termoConsulta.getPeso() * entradaIndiceInvertido.getPeso());
                } else {
                    EntradaRankingModel entradaRanking = new EntradaRankingModel();
                    entradaRanking.setUrl(entradaIndiceInvertido.getDocumento().getUrl());
                    entradaRanking.adicionarProdutoPesos(termoConsulta.getPeso() * entradaIndiceInvertido.getPeso());
                    entradaRanking.setSomaQuadradosPesosDocumento(entradaIndiceInvertido.getDocumento().getSomaQuadradosPesos());
                    entradaRanking.setSomaQuadradosPesosConsulta(consulta.getSomaQuadradosPesos());
                    this.mergeListasInvertidas.put(entradaIndiceInvertido.getDocumento().getUrl(), entradaRanking);
                }
            }
        }
    }

    public void computarSimilaridade() {
        Collection<EntradaRankingModel> ranking = this.mergeListasInvertidas.values();
        for (EntradaRankingModel entradaRanking : ranking) {
            entradaRanking.computarSimilaridade();
        }
    }

    public List<EntradaRankingModel> getRanking() {
        List<EntradaRankingModel> resp = new LinkedList();
        Collection<EntradaRankingModel> ranking = this.mergeListasInvertidas.values();
        for (EntradaRankingModel entradaRanking : ranking) {
            resp.add(entradaRanking);
        }
        return resp;
    }
}
