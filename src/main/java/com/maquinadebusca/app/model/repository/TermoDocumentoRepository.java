package com.maquinadebusca.app.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.maquinadebusca.app.model.TermoDocumentoModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TermoDocumentoRepository extends JpaRepository<TermoDocumentoModel, Long> {

    @Override
    List<TermoDocumentoModel> findAll();

    TermoDocumentoModel findById(long id);

    @Override
    TermoDocumentoModel save(TermoDocumentoModel termo);

    @Query(value = "select log (2, count(distinct d.id)/t.n) "
            + "from termodocumento t, documento d "
            + "where t.texto =:termo", nativeQuery = true)
    public double getIdf(@Param("termo") String termo);

}
