package com.springboot.rest.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.rest.infrastructure.entity.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
