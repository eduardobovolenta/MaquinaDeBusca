package com.maquinadebusca.app.model.repository;

import com.maquinadebusca.app.model.Host;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HostRepository extends JpaRepository<Host, Long> {

    @Override
    List<Host> findAll();

    Host findById(long id);

}
