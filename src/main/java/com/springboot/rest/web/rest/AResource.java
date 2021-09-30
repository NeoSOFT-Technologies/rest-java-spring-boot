package com.springboot.rest.web.rest;

import com.springboot.rest.domain.dto.ADTO;
import com.springboot.rest.domain.service.AService;
import com.springboot.rest.infrastructure.entity.A;
import com.springboot.rest.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.springboot.rest.infrastructure.entity.A}.
 */
@RestController
@RequestMapping("/api")
public class AResource {

    private final Logger log = LoggerFactory.getLogger(AResource.class);

    private static final String ENTITY_NAME = "a";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AService aService;

    public AResource(AService aService) {
        this.aService = aService;
    }

    /**
     * {@code POST  /as} : Create a new a.
     *
     * @parama the a to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new a, or with status {@code 400 (Bad Request)} if the a has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/as")
    @Operation(summary = "/as", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ADTO> createA(@RequestBody ADTO adto) throws URISyntaxException {
        log.debug("REST request to save A : {}", adto);
        if (adto.getId() != null) {
            throw new BadRequestAlertException("A new a cannot already have an ID", ENTITY_NAME, "idexists");
        }

        A a = aService.save(adto);
        ADTO adtoResponse = new ADTO(a);

        return ResponseEntity
                .created(new URI("/api/as/" + a.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, adtoResponse.getId().toString()))
                .body(adtoResponse);

    }

    /**
     * {@code PUT  /as/:id} : Updates an existing a.
     *
     * @param id the id of the a to save.
     * @parama the a to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated a,
     * or with status {@code 400 (Bad Request)} if the a is not valid,
     * or with status {@code 500 (Internal Server Error)} if the a couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/as/{id}")
    @Operation(summary = "/as", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ADTO> updateA(@PathVariable(value = "id", required = false) final Long id, @RequestBody ADTO adto)
            throws URISyntaxException {
        log.debug("REST request to update A : {}, {}", id, adto);

        A a = aService.update(id,adto);
        ADTO adtoResponse = new ADTO(a);

        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, adtoResponse.getId().toString()))
                .body(adtoResponse);
    }

    /**
     * {@code PATCH  /as/:id} : Partial updates given fields of an existing a, field will ignore if it is null
     *
     * @parama the a to update.
     * @param id the id of the a to save.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated a,
     * or with status {@code 400 (Bad Request)} if the a is not valid,
     * or with status {@code 404 (Not Found)} if the a is not found,
     * or with status {@code 500 (Internal Server Error)} if the a couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/as/{id}", consumes = "application/merge-patch+json")
    @Operation(summary = "/as", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<A> partialUpdateA(@PathVariable(value = "id", required = false) final Long id, @RequestBody ADTO adto)
            throws URISyntaxException {
        log.debug("REST request to partial update A partially : {}, {}", id, adto);

        Optional<A> result = aService.patch(id,adto);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, adto.getId().toString())
        );

    }

    /**
     * {@code GET  /as} : get all the aS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aS in body.
     */
    @GetMapping("/as")
    @Operation(summary = "/as", security = @SecurityRequirement(name = "bearerAuth"))
    public List<ADTO> getAllAS() {
        log.debug("REST request to get all AS");

        List<ADTO> adtos = aService.findAll()
                .stream()
                .map(a -> new ADTO(a))
                .collect(Collectors.toList());

        return adtos;
    }

    /**
     * {@code GET  /as/:id} : get the "id" a.
     *
     * @param id the id of the a to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the a, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/as/{id}")
    @Operation(summary = "/as", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity getA(@PathVariable Long id) {
        log.debug("REST request to get A : {}", id);
        Optional<A> a = aService.findById(id);

        return ResponseUtil.wrapOrNotFound(a);
    }

    /**
     * {@code DELETE  /as/:id} : delete the "id" a.
     *
     * @param id the id of the a to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/as/{id}")
    @Operation(summary = "/as", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteA(@PathVariable Long id) {
        log.debug("REST request to delete A : {}", id);
        aService.deleteById(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                .build();
    }
}