package com.springboot.rest.domain.port.spi;

import java.util.List;
import java.util.Optional;

import com.springboot.rest.infrastructure.entity.Authority;


public interface AuthorityPersistencePort {
    List<Authority> findAll();
    Optional<Authority> findById(String id);
}
