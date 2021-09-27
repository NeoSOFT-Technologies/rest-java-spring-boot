package com.springboot.rest.domain.service;

import com.springboot.rest.domain.dto.ADTO;
import com.springboot.rest.domain.port.spi.APersistencePort;
import com.springboot.rest.infrastructure.entity.A;
import com.springboot.rest.web.rest.errors.BadRequestAlertException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class AService {

    private static final String ENTITY_NAME = "a";

    private final APersistencePort aPersistencePort;

    public AService(APersistencePort aPersistencePort) {
        this.aPersistencePort = aPersistencePort;
    }

    public A save(ADTO adto) {
        return aPersistencePort.save(adto);
    }

    public A update(Long id, ADTO adto) {

        if (adto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPersistencePort.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        return aPersistencePort.save(adto);
    }

    public boolean existsById(Long id) {
        return aPersistencePort.existsById(id);
    }

    public List<A> findAll() {
        return aPersistencePort.findAll();
    }

    public Optional<A> findById(Long id) {
        return aPersistencePort.findById(id);
    }

    public void deleteById(Long id) {
        aPersistencePort.deleteById(id);
    }

//    public Optional<A> patch(Long id, ADTO adto) {
//
//        if (adto.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, adto.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!aPersistencePort.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        Optional<A> result = aPersistencePort
//                .findById(adto.getId())
//                .map(
//                        existingA -> {
//                            if (adto.getName() != null) {
//                                existingA.setName(adto.getName());
//                            }
//                            if (adto.getPassword() != null) {
//                                existingA.setPassword(adto.getPassword());
//                            }
//                            if (adto.getAge() != null) {
//                                existingA.setAge(adto.getAge());
//                            }
//                            if (adto.getPhone() != null) {
//                                existingA.setPhone(adto.getPhone());
//                            }
//
//                            return existingA;
//                        }
//                );
//
//        return result;
//
//    }
}
