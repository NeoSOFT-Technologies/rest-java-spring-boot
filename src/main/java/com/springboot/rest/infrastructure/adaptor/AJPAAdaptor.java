package com.springboot.rest.infrastructure.adaptor;

import com.springboot.rest.domain.dto.ADTO;
import com.springboot.rest.domain.port.spi.APersistencePort;
import com.springboot.rest.infrastructure.entity.A;
import com.springboot.rest.infrastructure.repository.ARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AJPAAdaptor implements APersistencePort {

    @Autowired
    private final ARepository aRepository;

    public AJPAAdaptor(ARepository aRepository) {
        this.aRepository = aRepository;
    }

    public List<A> findAll() {
        return aRepository.findAll();
    }

    @Override
    public Optional<A> findById(Long id) {
        return aRepository.findById(id);
    }

    public A save(ADTO adto) {

        A dtoToA=new A();
        dtoToA.setId(adto.getId());
        dtoToA.setName(adto.getName());
        dtoToA.setPassword(adto.getPassword());
        dtoToA.setAge(adto.getAge());
        dtoToA.setPhone(adto.getPhone());

        return aRepository.save(dtoToA);
    }

    @Override
    public boolean existsById(Long id) {
        return aRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        aRepository.deleteById(id);
    }

}