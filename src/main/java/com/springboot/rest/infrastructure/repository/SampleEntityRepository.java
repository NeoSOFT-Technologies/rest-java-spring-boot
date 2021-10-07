package com.springboot.rest.infrastructure.repository;

import com.springboot.rest.infrastructure.entity.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the A entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SampleEntityRepository extends JpaRepository<SampleEntity, Long> {}
