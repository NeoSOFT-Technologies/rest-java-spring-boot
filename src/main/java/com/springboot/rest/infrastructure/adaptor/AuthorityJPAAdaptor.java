package com.springboot.rest.infrastructure.adaptor;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.springboot.rest.domain.port.spi.AuthorityPersistencePort;
import com.springboot.rest.infrastructure.entity.Authority;
import com.springboot.rest.infrastructure.repository.AuthorityRepository;
@Service
public class AuthorityJPAAdaptor implements AuthorityPersistencePort{



    private final AuthorityRepository authRepository;

    public AuthorityJPAAdaptor(AuthorityRepository authRepository) {
        this.authRepository = authRepository;
    }
    
    public List<Authority> findAll() {
        return authRepository.findAll();
    }
    
    public Optional<Authority> findById(String id) {
        return     authRepository.findById(id);

    }

}
