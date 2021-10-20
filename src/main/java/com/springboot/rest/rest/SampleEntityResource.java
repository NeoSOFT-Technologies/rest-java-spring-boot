package com.springboot.rest.rest;

import com.springboot.rest.domain.dto.SampleEntityDTO;
import com.springboot.rest.domain.port.api.SampleEntityServicePort;
import com.springboot.rest.infrastructure.entity.SampleEntity;
import com.springboot.rest.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link SampleEntity}.
 */
@RestController
@RequestMapping("/api")
public class SampleEntityResource {

    private final Logger log = LoggerFactory.getLogger(SampleEntityResource.class);

    private static final String ENTITY_NAME = "a";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SampleEntityServicePort sampleEntityServicePort;

    public SampleEntityResource(SampleEntityServicePort sampleEntityServicePort) {
        this.sampleEntityServicePort = sampleEntityServicePort;
    }

    /**
     * {@code POST  /sample-entity} : Create a new a.
     *
     * @parama the a to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new a, or with status {@code 400 (Bad Request)} if the a has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sample-entity")
    @Operation(summary = "/sample-entity", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<SampleEntityDTO> createA(@RequestBody SampleEntityDTO sampleEntityDTO) throws URISyntaxException {
        log.debug("REST request to save A : {}", sampleEntityDTO);
        if (sampleEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new a cannot already have an ID", ENTITY_NAME, "idexists");
        }

        SampleEntity sampleEntity = sampleEntityServicePort.save(sampleEntityDTO);
        SampleEntityDTO sampleEntityDTOResponse = new SampleEntityDTO(sampleEntity);

        return ResponseEntity
                .created(new URI("/api/sample-entity/" + sampleEntity.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, sampleEntityDTOResponse.getId().toString()))
                .body(sampleEntityDTOResponse);

    }

    /**
     * {@code PUT  /sample-entity/:id} : Updates an existing a.
     *
     * @param id the id of the a to save.
     * @parama the a to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated a,
     * or with status {@code 400 (Bad Request)} if the a is not valid,
     * or with status {@code 500 (Internal Server Error)} if the a couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sample-entity/{id}")
    @Operation(summary = "/sample-entity", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<SampleEntityDTO> updateA(@PathVariable(value = "id", required = false) final Long id, @RequestBody SampleEntityDTO sampleEntityDTO)
            throws URISyntaxException {
        log.debug("REST request to update A : {}, {}", id, sampleEntityDTO);

        SampleEntity sampleEntity = sampleEntityServicePort.update(id, sampleEntityDTO);
        SampleEntityDTO sampleEntityDTOResponse = new SampleEntityDTO(sampleEntity);

        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sampleEntityDTOResponse.getId().toString()))
                .body(sampleEntityDTOResponse);
    }

    /**
     * {@code PATCH  /sample-entity/:id} : Partial updates given fields of an existing a, field will ignore if it is null
     *
     * @parama the a to update.
     * @param id the id of the a to save.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated a,
     * or with status {@code 400 (Bad Request)} if the a is not valid,
     * or with status {@code 404 (Not Found)} if the a is not found,
     * or with status {@code 500 (Internal Server Error)} if the a couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sample-entity/{id}", consumes = "application/merge-patch+json")
    @Operation(summary = "/sample-entity", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<SampleEntity> partialUpdateA(@PathVariable(value = "id", required = false) final Long id, @RequestBody SampleEntityDTO sampleEntityDTO)
            throws URISyntaxException {
        log.debug("REST request to partial update A partially : {}, {}", id, sampleEntityDTO);

        Optional<SampleEntity> result = sampleEntityServicePort.patch(id, sampleEntityDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sampleEntityDTO.getId().toString())
        );

    }

    /**
     * {@code GET  /sample-entity} : get all the aS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aS in body.
     */
    @GetMapping("/sample-entity")
    @Operation(summary = "/sample-entity", security = @SecurityRequirement(name = "bearerAuth"))
    public List<SampleEntityDTO> getAllAS() {
        log.debug("REST request to get all AS");

        List<SampleEntityDTO> sampleEntityDTOS = sampleEntityServicePort.findAll()
                .stream()
                .map(a -> new SampleEntityDTO(a))
                .collect(Collectors.toList());

        return sampleEntityDTOS;
    }

    /**
     * {@code GET  /sample-entity/:id} : get the "id" a.
     *
     * @param id the id of the a to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the a, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sample-entity/{id}")
    @Operation(summary = "/sample-entity", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity getA(@PathVariable Long id) {
        log.debug("REST request to get A : {}", id);
        Optional<SampleEntity> a = sampleEntityServicePort.findById(id);

        return ResponseUtil.wrapOrNotFound(a);
    }

    /**
     * {@code DELETE  /sample-entity/:id} : delete the "id" a.
     *
     * @param id the id of the a to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sample-entity/{id}")
    @Operation(summary = "/sample-entity", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteA(@PathVariable Long id) {
        log.debug("REST request to delete A : {}", id);
        sampleEntityServicePort.deleteById(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                .build();
    }
}