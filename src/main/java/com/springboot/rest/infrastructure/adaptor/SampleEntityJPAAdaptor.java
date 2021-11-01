package com.springboot.rest.infrastructure.adaptor;

import com.springboot.rest.domain.dto.SampleEntityDTO;
import com.springboot.rest.domain.port.spi.SampleEntityPersistencePort;
import com.springboot.rest.infrastructure.entity.SampleEntity;
import com.springboot.rest.infrastructure.repository.SampleEntityRepository;
import com.springboot.rest.mapper.SampleEntityMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SampleEntityJPAAdaptor implements SampleEntityPersistencePort {

    @Autowired
    private final SampleEntityRepository sampleEntityRepository;

    private final SampleEntityMapper sampleEntityMapper;
    
    public SampleEntityJPAAdaptor(SampleEntityRepository sampleEntityRepository, SampleEntityMapper sampleEntityMapper) {
        this.sampleEntityRepository = sampleEntityRepository;
        this.sampleEntityMapper = sampleEntityMapper;
    }

    public List<SampleEntity> findAll() {
        return sampleEntityRepository.findAll();
    }

    @Override
    public Optional<SampleEntity> findById(Long id) {
        return sampleEntityRepository.findById(id);
    }

    public SampleEntity save(SampleEntityDTO sampleEntityDTO) {

        // SampleEntityDTO to SampleEntity conversion
    	SampleEntity sampleEntity = sampleEntityMapper.dtoToEntity(sampleEntityDTO);
        return sampleEntityRepository.save(sampleEntity);
    }

    @Override
    public boolean existsById(Long id) {
        return sampleEntityRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        sampleEntityRepository.deleteById(id);
    }

}