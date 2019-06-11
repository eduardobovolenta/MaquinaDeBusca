package com.maquinadebusca.app.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maquinadebusca.app.model.DocumentoModel;

public interface DocumentoRepository extends JpaRepository<DocumentoModel, Long> {

	@Override
	List<DocumentoModel> findAll();

	DocumentoModel findById(long id);

	@Query(value = "SELECT titulo, url, visao FROM documento WHERE visao like %:pesquisa%", nativeQuery = true)
	List<Object[]> obterDocumento(@Param("pesquisa") String pesquisa);
}
