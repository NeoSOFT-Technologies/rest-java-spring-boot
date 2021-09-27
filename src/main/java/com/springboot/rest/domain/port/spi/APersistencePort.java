package com.springboot.rest.domain.port.spi;

import com.springboot.rest.domain.dto.ADTO;
import com.springboot.rest.infrastructure.entity.A;

import java.util.List;
import java.util.Optional;


public interface APersistencePort {
    List<A> findAll();

    Optional<A> findById(Long id);

    A save(ADTO adto);

    boolean existsById(Long id);

    void deleteById(Long id);
}
