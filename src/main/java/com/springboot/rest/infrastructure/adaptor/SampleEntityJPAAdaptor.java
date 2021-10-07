package com.springboot.rest.infrastructure.adaptor;

import com.springboot.rest.domain.dto.SampleEntityDTO;
import com.springboot.rest.domain.port.spi.SampleEntityPersistencePort;
import com.springboot.rest.infrastructure.entity.SampleEntity;
import com.springboot.rest.infrastructure.repository.SampleEntityRepository;
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

    public SampleEntityJPAAdaptor(SampleEntityRepository sampleEntityRepository) {
        this.sampleEntityRepository = sampleEntityRepository;
    }

    public List<SampleEntity> findAll() {
        return sampleEntityRepository.findAll();
    }

    @Override
    public Optional<SampleEntity> findById(Long id) {
        return sampleEntityRepository.findById(id);
    }

    public SampleEntity save(SampleEntityDTO sampleEntityDTO) {

        SampleEntity dtoToSampleEntity =new SampleEntity();
        dtoToSampleEntity.setId(sampleEntityDTO.getId());
        dtoToSampleEntity.setName(sampleEntityDTO.getName());
        dtoToSampleEntity.setPassword(sampleEntityDTO.getPassword());
        dtoToSampleEntity.setAge(sampleEntityDTO.getAge());
        dtoToSampleEntity.setPhone(sampleEntityDTO.getPhone());

        return sampleEntityRepository.save(dtoToSampleEntity);
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