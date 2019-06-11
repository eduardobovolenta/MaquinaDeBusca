package com.maquinadebusca.app.model.repository;

import com.maquinadebusca.app.model.HostModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HostRepository extends JpaRepository<HostModel, Long> {

    @Override
    List<HostModel> findAll();

    HostModel findById(long id);

}
