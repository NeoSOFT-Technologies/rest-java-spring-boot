package com.springboot.rest.infrastructure.adaptor;

import com.springboot.rest.domain.dto.ADTO;
import com.springboot.rest.domain.port.spi.APersistencePort;
import com.springboot.rest.infrastructure.entity.A;
import com.springboot.rest.infrastructure.repository.ARepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AJPAAdaptor implements APersistencePort {

    private final Logger log = LoggerFactory.getLogger(AJPAAdaptor.class);

    @Autowired
    ARepository aRepository;

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

        A result=aRepository.save(dtoToA);

        return result;
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
