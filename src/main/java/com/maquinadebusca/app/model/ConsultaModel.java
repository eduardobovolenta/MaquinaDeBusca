package com.maquinadebusca.app.model;

import java.util.LinkedList;
import java.util.List;

public class ConsultaModel {

    private String texto;
    private String visao;
    private List<TermoConsultaModel> termosConsulta = new LinkedList();
    private List<EntradaRankingModel> ranking = new LinkedList();

    public ConsultaModel() {
    }

    public ConsultaModel(String texto) {
        this.texto = texto;
        this.visao = texto;
    }

    public String getTexto() {
        return texto;
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

    public List<TermoConsultaModel> getTermosConsulta() {
        return termosConsulta;
    }

    public void setTermosConsulta(List<TermoConsultaModel> termosConsulta) {
        this.termosConsulta = termosConsulta;
    }

    public void adicionarTermoConsulta(TermoConsultaModel termoConsulta) {
        this.termosConsulta.add(termoConsulta);
    }

    public List<String> getListaTermos() {
        List<String> listaTermos = new LinkedList();

        String[] termos = this.texto.split(" ");
        for (String termo : termos) {
            listaTermos.add(termo);
        }

        return listaTermos;
    }

    public double getSomaQuadradosPesos() {
        double somaQuadradosPesos = 0;
        List<TermoConsultaModel> termosConsulta = this.getTermosConsulta();
        for (TermoConsultaModel termoConsulta : termosConsulta) {
            somaQuadradosPesos += Math.pow(termoConsulta.getPeso(), 2);
        }
        return somaQuadradosPesos;
    }

    public List<EntradaRankingModel> getRanking() {
        return ranking;
    }

    public void setRanking(List<EntradaRankingModel> ranking) {
        this.ranking = ranking;
    }

}
