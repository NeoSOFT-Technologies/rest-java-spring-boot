package com.springboot.rest.domain.service;

import com.springboot.rest.domain.dto.SampleEntityDTO;
import com.springboot.rest.domain.port.api.SampleEntityServicePort;
import com.springboot.rest.domain.port.spi.SampleEntityPersistencePort;
import com.springboot.rest.infrastructure.entity.SampleEntity;
import com.springboot.rest.mapper.SampleEntityMapper;
import com.springboot.rest.rest.errors.BadRequestAlertException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class SampleEntityService implements SampleEntityServicePort {

    private static final String ENTITY_NAME = "a";

    private final SampleEntityPersistencePort sampleEntityPersistencePort;
    private final SampleEntityMapper sampleEntityMapper;

    public SampleEntityService(SampleEntityPersistencePort sampleEntityPersistencePort, SampleEntityMapper sampleEntityMapper) {
        this.sampleEntityPersistencePort = sampleEntityPersistencePort;
        this.sampleEntityMapper = sampleEntityMapper;
    }

    @Override
    public SampleEntity save(SampleEntityDTO sampleEntityDTO) {
        return sampleEntityPersistencePort.save(sampleEntityDTO);
    }

    @Override
    public SampleEntity update(Long id, SampleEntityDTO sampleEntityDTO) {

        if (sampleEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sampleEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sampleEntityPersistencePort.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        return sampleEntityPersistencePort.save(sampleEntityDTO);
    }

    @Override
    public boolean existsById(Long id) {
        return sampleEntityPersistencePort.existsById(id);
    }

    @Override
    public List<SampleEntity> findAll() {
        return sampleEntityPersistencePort.findAll();
    }

    @Override
    public Optional<SampleEntity> findById(Long id) {
        return sampleEntityPersistencePort.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        sampleEntityPersistencePort.deleteById(id);
    }

    @Override
    public Optional<SampleEntity> patch(Long id, SampleEntityDTO sampleEntityDTO) {

        if (sampleEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sampleEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sampleEntityPersistencePort.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        return sampleEntityPersistencePort
                .findById(sampleEntityDTO.getId())
                .map(
                        existingA -> {
                            if (sampleEntityDTO.getName() != null) {
                                existingA.setName(sampleEntityDTO.getName());
                            }
                            if (sampleEntityDTO.getPassword() != null) {
                                existingA.setPassword(sampleEntityDTO.getPassword());
                            }
                            if (sampleEntityDTO.getAge() != null) {
                                existingA.setAge(sampleEntityDTO.getAge());
                            }
                            if (sampleEntityDTO.getPhone() != null) {
                                existingA.setPhone(sampleEntityDTO.getPhone());
                            }
                            return existingA;
                        }
                )
                .map(updatedA -> {
                	SampleEntityDTO updatedSampleEntityDTO = sampleEntityMapper.entityToDto(updatedA);
                    sampleEntityPersistencePort.save(updatedSampleEntityDTO);
                    return updatedA;
                });
    
    }

}