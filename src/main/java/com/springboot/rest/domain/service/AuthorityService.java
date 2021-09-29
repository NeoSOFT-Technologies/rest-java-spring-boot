package com.springboot.rest.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.rest.domain.port.spi.AuthorityPersistencePort;
import com.springboot.rest.infrastructure.entity.Authority;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class AuthorityService {

    private final Logger log = LoggerFactory.getLogger(AuthorityService.class);

    private final AuthorityPersistencePort authorityPersistencePort;

    public AuthorityService(AuthorityPersistencePort authorityPersistencePort) {
        this.authorityPersistencePort = authorityPersistencePort;

    }

    /**
     * Gets a list of all the authorities.
     * 
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityPersistencePort.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }


}
