package com.springboot.rest.domain.service;

import com.springboot.rest.domain.port.api.AuthorityServicePort;
import com.springboot.rest.domain.port.spi.AuthorityPersistencePort;
import com.springboot.rest.infrastructure.entity.Authority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class AuthorityService implements AuthorityServicePort {

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
    @Override
    public List<String> getAuthorities() {
        return authorityPersistencePort.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }


}
