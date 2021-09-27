package com.springboot.rest.infrastructure.repository;

import com.springboot.rest.domain.AOld;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the A entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ARepository extends JpaRepository<AOld, Long> {}
