package com.maquinadebusca.app.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.maquinadebusca.app.model.TermoDocumentoModel;

public interface TermoRepository extends JpaRepository<TermoDocumentoModel, Long> {

    @Override
    List<TermoDocumentoModel> findAll();

    TermoDocumentoModel findById(long id);

    @Override
    TermoDocumentoModel save(TermoDocumentoModel termo);

}
