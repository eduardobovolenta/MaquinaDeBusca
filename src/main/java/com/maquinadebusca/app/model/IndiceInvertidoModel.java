package com.maquinadebusca.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "IndiceInvertido")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class IndiceInvertidoModel implements Serializable {

    static final long serialVersionUID = 1L;

    @EmbeddedId
    private IdIndiceInvertidoModel id;

    private int frequencia;

    private double frequenciaNormalizada;

    private double peso;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idTermo")
    private TermoDocumentoModel termo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idDocumento")
    private DocumentoModel documento;

    public IndiceInvertidoModel() {
    }

    public IndiceInvertidoModel(TermoDocumentoModel termo, DocumentoModel documento) {
        this.termo = termo;
        this.documento = documento;
        this.id = new IdIndiceInvertidoModel(termo.getId(), documento.getId());
    }

    public IndiceInvertidoModel(TermoDocumentoModel termo, DocumentoModel documento, int frequencia) {
        this.termo = termo;
        this.documento = documento;
        this.frequencia = frequencia;
        this.id = new IdIndiceInvertidoModel(termo.getId(), documento.getId());
    }

    public IdIndiceInvertidoModel getId() {
        return id;
    }

    public void setId(IdIndiceInvertidoModel id) {
        this.id = id;
    }

    public int getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }

    public double getFrequenciaNormalizada() {
        return frequenciaNormalizada;
    }

    public void setFrequenciaNormalizada(double frequenciaNormalizada) {
        this.frequenciaNormalizada = frequenciaNormalizada;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public TermoDocumentoModel getTermo() {
        return termo;
    }

    public void setTermo(TermoDocumentoModel termo) {
        this.termo = termo;
    }

    public DocumentoModel getDocumento() {
        return documento;
    }

    public void setDocumento(DocumentoModel documento) {
        this.documento = documento;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.id.getIdTermo());
        hash = 83 * hash + Objects.hashCode(this.id.getIdDocumento());
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
        final IndiceInvertidoModel other = (IndiceInvertidoModel) obj;
        if (!Objects.equals(this.id.getIdTermo(), other.id.getIdTermo())) {
            return false;
        }
        if (!Objects.equals(this.id.getIdDocumento(), other.id.getIdDocumento())) {
            return false;
        }
        return true;
    }

}
