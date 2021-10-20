package com.springboot.rest.rest;

import com.springboot.rest.IntegrationTest;
import com.springboot.rest.domain.port.spi.SampleEntityPersistencePort;
import com.springboot.rest.domain.service.SampleEntityService;
import com.springboot.rest.infrastructure.entity.SampleEntity;
import com.springboot.rest.infrastructure.repository.SampleEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link SampleEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SampleEntityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final Integer DEFAULT_PHONE = 1;
    private static final Integer UPDATED_PHONE = 2;

    private static final String ENTITY_API_URL = "/api/sample-entity";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SampleEntityRepository sampleEntityRepository;
    
    @Mock
    private SampleEntityPersistencePort sampleEntityPersistencePort;
    
    @Mock
    private SampleEntityService sampleEntityService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAMockMvc;

    private SampleEntity sampleEntity;

    /**
     * Create an entity for this test.
     *
     * This is sampleEntity static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SampleEntity createEntity(EntityManager em) {
        SampleEntity sampleEntity = new SampleEntity().name(DEFAULT_NAME).password(DEFAULT_PASSWORD).age(DEFAULT_AGE).phone(DEFAULT_PHONE);
        return sampleEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is sampleEntity static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SampleEntity createUpdatedEntity(EntityManager em) {
        SampleEntity sampleEntity = new SampleEntity().name(UPDATED_NAME).password(UPDATED_PASSWORD).age(UPDATED_AGE).phone(UPDATED_PHONE);
        return sampleEntity;
    }

    @BeforeEach
    public void initTest() {
        sampleEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createSampleEntity() throws Exception {
        int databaseSizeBeforeCreate = sampleEntityRepository.findAll().size();
        // Create the SampleEntity

        restAMockMvc
                .perform(MockMvcRequestBuilders.post(ENTITY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(sampleEntity)))
                .andExpect(status().isCreated());

        // Validate the SampleEntity in the database
        List<SampleEntity> sampleEntitiesList = sampleEntityRepository.findAll();
        assertThat(sampleEntitiesList.size()).isEqualTo(databaseSizeBeforeCreate + 1);

        SampleEntity testSampleEntity = sampleEntitiesList.get(sampleEntitiesList.size() - 1);
        assertThat(testSampleEntity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSampleEntity.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testSampleEntity.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testSampleEntity.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void createSampleEntityWithExistingId() throws Exception {
        // Create the SampleEntity with an existing ID
        sampleEntity.setId(1L);

        int databaseSizeBeforeCreate = sampleEntityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAMockMvc
                .perform(MockMvcRequestBuilders.post(ENTITY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(sampleEntity)))
                .andExpect(status().isBadRequest());

        // Validate the SampleEntity in the database
        List<SampleEntity> sampleEntityListList = sampleEntityRepository.findAll();
        assertThat(sampleEntityListList.size()).isEqualTo(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSampleEntities() throws Exception {
        // Initialize the database
        sampleEntityRepository.saveAndFlush(sampleEntity);

        // Get all the sampleEntityList
        restAMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").value(hasItem(sampleEntity.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*]['id']").value(hasItem(sampleEntity.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].password").value(hasItem(DEFAULT_PASSWORD)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].age").value(hasItem(DEFAULT_AGE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @Test
    @Transactional
    void getSampleEntity() throws Exception {
        // Initialize the database
        sampleEntityRepository.saveAndFlush(sampleEntity);

        // Get the sampleEntity
        restAMockMvc
            .perform(get(ENTITY_API_URL_ID, sampleEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(sampleEntity.getId().intValue()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    void getNonExistingSampleEntity() throws Exception {
        // Get the sampleEntity
        restAMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    //
    @Test
    @Transactional
    void putNewSampleEntity() throws Exception {
        // Initialize the database
        sampleEntityRepository.saveAndFlush(sampleEntity);

        int databaseSizeBeforeUpdate = sampleEntityRepository.findAll().size();

        // Update the sampleEntity
        SampleEntity updatedSampleEntity = sampleEntityRepository.findById(sampleEntity.getId()).get();
        // Disconnect from session so that the updates on updatedA are not directly saved in db
        em.detach(updatedSampleEntity);
        updatedSampleEntity.name(UPDATED_NAME).password(UPDATED_PASSWORD).age(UPDATED_AGE).phone(UPDATED_PHONE);

        restAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSampleEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSampleEntity))
            )
            .andExpect(status().isOk());

        // Validate the SampleEntity in the database
        List<SampleEntity> sampleEntityList = sampleEntityRepository.findAll();
        assertThat(sampleEntityList.size()).isEqualTo(databaseSizeBeforeUpdate);
        SampleEntity testSampleEntity = sampleEntityList.get(sampleEntityList.size() - 1);
        assertThat(testSampleEntity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSampleEntity.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testSampleEntity.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testSampleEntity.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void putNonExistingSampleEntity() throws Exception {
        int databaseSizeBeforeUpdate = sampleEntityRepository.findAll().size();
        sampleEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sampleEntity.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sampleEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the SampleEntity in the database
        List<SampleEntity> sampleEntityList = sampleEntityRepository.findAll();
        assertThat(sampleEntityList.size()).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSampleEntity() throws Exception {
        int databaseSizeBeforeUpdate = sampleEntityRepository.findAll().size();
        sampleEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sampleEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the SampleEntity in the database
        List<SampleEntity> sampleEntityList = sampleEntityRepository.findAll();
        assertThat(sampleEntityList.size()).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSampleEntity() throws Exception {
        int databaseSizeBeforeUpdate = sampleEntityRepository.findAll().size();
        sampleEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sampleEntity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SampleEntity in the database
        List<SampleEntity> sampleEntityList = sampleEntityRepository.findAll();
        assertThat(sampleEntityList.size()).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSampleEntityWithPatch() throws Exception {
        // Initialize the database
        sampleEntityRepository.saveAndFlush(sampleEntity);

        int databaseSizeBeforeUpdate = sampleEntityRepository.findAll().size();

        // Update the sampleEntity using partial update
        SampleEntity partialUpdatedSampleEntity = new SampleEntity();
        partialUpdatedSampleEntity.setId(sampleEntity.getId());

        partialUpdatedSampleEntity.name(UPDATED_NAME);

        restAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSampleEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSampleEntity))
            )
            .andExpect(status().isOk());

        // Validate the SampleEntity in the database
        List<SampleEntity> sampleEntityList = sampleEntityRepository.findAll();
        assertThat(sampleEntityList.size()).isEqualTo(databaseSizeBeforeUpdate);
        SampleEntity testSampleEntity = sampleEntityList.get(sampleEntityList.size() - 1);
        assertThat(testSampleEntity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSampleEntity.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testSampleEntity.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testSampleEntity.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void fullUpdateSampleEntityWithPatch() throws Exception {
        // Initialize the database
        sampleEntityRepository.saveAndFlush(sampleEntity);

        int databaseSizeBeforeUpdate = sampleEntityRepository.findAll().size();

        // Update the sampleEntity using partial update
        SampleEntity partialUpdatedSampleEntity = new SampleEntity();
        partialUpdatedSampleEntity.setId(sampleEntity.getId());

        partialUpdatedSampleEntity.name(UPDATED_NAME).password(UPDATED_PASSWORD).age(UPDATED_AGE).phone(UPDATED_PHONE);

        restAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSampleEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSampleEntity))
            )
            .andExpect(status().isOk());

        // Validate the SampleEntity in the database
        List<SampleEntity> sampleEntityList = sampleEntityRepository.findAll();
        assertThat(sampleEntityList.size()).isEqualTo(databaseSizeBeforeUpdate);
        SampleEntity testSampleEntity = sampleEntityList.get(sampleEntityList.size() - 1);
        assertThat(testSampleEntity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSampleEntity.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testSampleEntity.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testSampleEntity.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void patchNonExistingSampleEntity() throws Exception {
        int databaseSizeBeforeUpdate = sampleEntityRepository.findAll().size();
        sampleEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sampleEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sampleEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the SampleEntity in the database
        List<SampleEntity> sampleEntityList = sampleEntityRepository.findAll();
        assertThat(sampleEntityList.size()).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSampleEntity() throws Exception {
        int databaseSizeBeforeUpdate = sampleEntityRepository.findAll().size();
        sampleEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sampleEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the SampleEntity in the database
        List<SampleEntity> sampleEntityList = sampleEntityRepository.findAll();
        assertThat(sampleEntityList.size()).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSampleEntity() throws Exception {
        int databaseSizeBeforeUpdate = sampleEntityRepository.findAll().size();
        sampleEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sampleEntity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SampleEntity in the database
        List<SampleEntity> sampleEntityList = sampleEntityRepository.findAll();
        assertThat(sampleEntityList.size()).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSampleEntity() throws Exception {
        // Initialize the database
        sampleEntityRepository.saveAndFlush(sampleEntity);

        int databaseSizeBeforeDelete = sampleEntityRepository.findAll().size();

        // Delete the sampleEntity
        restAMockMvc.perform(delete(ENTITY_API_URL_ID, sampleEntity.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SampleEntity> sampleEntityList = sampleEntityRepository.findAll();
        assertThat(sampleEntityList.size()).isEqualTo(databaseSizeBeforeDelete - 1);
    }
}
