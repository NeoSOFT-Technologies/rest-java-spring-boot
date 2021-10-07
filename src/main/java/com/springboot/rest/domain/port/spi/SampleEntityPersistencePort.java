package com.springboot.rest.domain.port.spi;

import com.springboot.rest.domain.dto.SampleEntityDTO;
import com.springboot.rest.infrastructure.entity.SampleEntity;

import java.util.List;
import java.util.Optional;


public interface SampleEntityPersistencePort {
    List<SampleEntity> findAll();

    Optional<SampleEntity> findById(Long id);

    SampleEntity save(SampleEntityDTO sampleEntityDTO);

    boolean existsById(Long id);

    void deleteById(Long id);
}