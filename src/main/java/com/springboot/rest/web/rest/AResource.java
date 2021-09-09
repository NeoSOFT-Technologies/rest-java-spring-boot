package com.springboot.rest.web.rest;

import com.springboot.rest.domain.A;
import com.springboot.rest.repository.ARepository;
import com.springboot.rest.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.springboot.rest.domain.A}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AResource {

    private final Logger log = LoggerFactory.getLogger(AResource.class);

    private static final String ENTITY_NAME = "a";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ARepository aRepository;

    public AResource(ARepository aRepository) {
        this.aRepository = aRepository;
    }

    /**
     * {@code POST  /as} : Create a new a.
     *
     * @param a the a to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new a, or with status {@code 400 (Bad Request)} if the a has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/as")
    public ResponseEntity<A> createA(@RequestBody A a) throws URISyntaxException {
        log.debug("REST request to save A : {}", a);
        if (a.getId() != null) {
            throw new BadRequestAlertException("A new a cannot already have an ID", ENTITY_NAME, "idexists");
        }
        A result = aRepository.save(a);
        return ResponseEntity
            .created(new URI("/api/as/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /as/:id} : Updates an existing a.
     *
     * @param id the id of the a to save.
     * @param a the a to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated a,
     * or with status {@code 400 (Bad Request)} if the a is not valid,
     * or with status {@code 500 (Internal Server Error)} if the a couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/as/{id}")
    public ResponseEntity<A> updateA(@PathVariable(value = "id", required = false) final Long id, @RequestBody A a)
        throws URISyntaxException {
        log.debug("REST request to update A : {}, {}", id, a);
        if (a.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, a.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        A result = aRepository.save(a);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, a.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /as/:id} : Partial updates given fields of an existing a, field will ignore if it is null
     *
     * @param id the id of the a to save.
     * @param a the a to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated a,
     * or with status {@code 400 (Bad Request)} if the a is not valid,
     * or with status {@code 404 (Not Found)} if the a is not found,
     * or with status {@code 500 (Internal Server Error)} if the a couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/as/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<A> partialUpdateA(@PathVariable(value = "id", required = false) final Long id, @RequestBody A a)
        throws URISyntaxException {
        log.debug("REST request to partial update A partially : {}, {}", id, a);
        if (a.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, a.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<A> result = aRepository
            .findById(a.getId())
            .map(
                existingA -> {
                    if (a.getName() != null) {
                        existingA.setName(a.getName());
                    }
                    if (a.getPassword() != null) {
                        existingA.setPassword(a.getPassword());
                    }
                    if (a.getAge() != null) {
                        existingA.setAge(a.getAge());
                    }
                    if (a.getPhone() != null) {
                        existingA.setPhone(a.getPhone());
                    }

                    return existingA;
                }
            )
            .map(aRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, a.getId().toString())
        );
    }

    /**
     * {@code GET  /as} : get all the aS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aS in body.
     */
    @GetMapping("/as")
    public List<A> getAllAS() {
        log.debug("REST request to get all AS");
        return aRepository.findAll();
    }

    /**
     * {@code GET  /as/:id} : get the "id" a.
     *
     * @param id the id of the a to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the a, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/as/{id}")
    public ResponseEntity<A> getA(@PathVariable Long id) {
        log.debug("REST request to get A : {}", id);
        Optional<A> a = aRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(a);
    }

    /**
     * {@code DELETE  /as/:id} : delete the "id" a.
     *
     * @param id the id of the a to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/as/{id}")
    public ResponseEntity<Void> deleteA(@PathVariable Long id) {
        log.debug("REST request to delete A : {}", id);
        aRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
