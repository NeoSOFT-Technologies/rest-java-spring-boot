package com.springboot.rest.domain.port.api;

import com.springboot.rest.domain.dto.SampleEntityDTO;
import com.springboot.rest.infrastructure.entity.SampleEntity;

import java.util.List;
import java.util.Optional;


public interface SampleEntityServicePort {

    SampleEntity save(SampleEntityDTO sampleEntityDTO);

    SampleEntity update(Long id, SampleEntityDTO sampleEntityDTO);

    boolean existsById(Long id);

    List<SampleEntity> findAll();

    Optional<SampleEntity> findById(Long id);

    void deleteById(Long id);

    Optional<SampleEntity> patch(Long id, SampleEntityDTO sampleEntityDTO);



}
