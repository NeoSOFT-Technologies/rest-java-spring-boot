package com.springboot.rest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.rest.IntegrationTest;
import com.springboot.rest.domain.AOld;
import com.springboot.rest.infrastructure.repository.ARepository;

/**
 * Integration tests for the {@link AResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final Integer DEFAULT_PHONE = 1;
    private static final Integer UPDATED_PHONE = 2;

    private static final String ENTITY_API_URL = "/api/as";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ARepository aRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAMockMvc;

    private AOld a;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AOld createEntity(EntityManager em) {
        AOld a = new AOld().name(DEFAULT_NAME).password(DEFAULT_PASSWORD).age(DEFAULT_AGE).phone(DEFAULT_PHONE);
        return a;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AOld createUpdatedEntity(EntityManager em) {
        AOld a = new AOld().name(UPDATED_NAME).password(UPDATED_PASSWORD).age(UPDATED_AGE).phone(UPDATED_PHONE);
        return a;
    }

    @BeforeEach
    public void initTest() {
        a = createEntity(em);
    }

    @Test
    @Transactional
    void createA() throws Exception {
        int databaseSizeBeforeCreate = aRepository.findAll().size();
        // Create the A
        restAMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(a)))
            .andExpect(status().isCreated());

        // Validate the A in the database
        List<AOld> aList = aRepository.findAll();
        assertThat(aList).hasSize(databaseSizeBeforeCreate + 1);
        AOld testA = aList.get(aList.size() - 1);
        assertThat(testA.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testA.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testA.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testA.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void createAWithExistingId() throws Exception {
        // Create the A with an existing ID
        a.setId(1L);

        int databaseSizeBeforeCreate = aRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(a)))
            .andExpect(status().isBadRequest());

        // Validate the A in the database
        List<AOld> aList = aRepository.findAll();
        assertThat(aList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAS() throws Exception {
        // Initialize the database
        aRepository.saveAndFlush(a);

        // Get all the aList
        restAMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(a.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @Test
    @Transactional
    void getA() throws Exception {
        // Initialize the database
        aRepository.saveAndFlush(a);

        // Get the a
        restAMockMvc
            .perform(get(ENTITY_API_URL_ID, a.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(a.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    void getNonExistingA() throws Exception {
        // Get the a
        restAMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewA() throws Exception {
        // Initialize the database
        aRepository.saveAndFlush(a);

        int databaseSizeBeforeUpdate = aRepository.findAll().size();

        // Update the a
        AOld updatedA = aRepository.findById(a.getId()).get();
        // Disconnect from session so that the updates on updatedA are not directly saved in db
        em.detach(updatedA);
        updatedA.name(UPDATED_NAME).password(UPDATED_PASSWORD).age(UPDATED_AGE).phone(UPDATED_PHONE);

        restAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedA.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedA))
            )
            .andExpect(status().isOk());

        // Validate the A in the database
        List<AOld> aList = aRepository.findAll();
        assertThat(aList).hasSize(databaseSizeBeforeUpdate);
        AOld testA = aList.get(aList.size() - 1);
        assertThat(testA.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testA.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testA.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testA.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void putNonExistingA() throws Exception {
        int databaseSizeBeforeUpdate = aRepository.findAll().size();
        a.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, a.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(a))
            )
            .andExpect(status().isBadRequest());

        // Validate the A in the database
        List<AOld> aList = aRepository.findAll();
        assertThat(aList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchA() throws Exception {
        int databaseSizeBeforeUpdate = aRepository.findAll().size();
        a.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(a))
            )
            .andExpect(status().isBadRequest());

        // Validate the A in the database
        List<AOld> aList = aRepository.findAll();
        assertThat(aList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamA() throws Exception {
        int databaseSizeBeforeUpdate = aRepository.findAll().size();
        a.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(a)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the A in the database
        List<AOld> aList = aRepository.findAll();
        assertThat(aList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAWithPatch() throws Exception {
        // Initialize the database
        aRepository.saveAndFlush(a);

        int databaseSizeBeforeUpdate = aRepository.findAll().size();

        // Update the a using partial update
        AOld partialUpdatedA = new AOld();
        partialUpdatedA.setId(a.getId());

        partialUpdatedA.name(UPDATED_NAME);

        restAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedA.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedA))
            )
            .andExpect(status().isOk());

        // Validate the A in the database
        List<AOld> aList = aRepository.findAll();
        assertThat(aList).hasSize(databaseSizeBeforeUpdate);
        AOld testA = aList.get(aList.size() - 1);
        assertThat(testA.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testA.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testA.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testA.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void fullUpdateAWithPatch() throws Exception {
        // Initialize the database
        aRepository.saveAndFlush(a);

        int databaseSizeBeforeUpdate = aRepository.findAll().size();

        // Update the a using partial update
        AOld partialUpdatedA = new AOld();
        partialUpdatedA.setId(a.getId());

        partialUpdatedA.name(UPDATED_NAME).password(UPDATED_PASSWORD).age(UPDATED_AGE).phone(UPDATED_PHONE);

        restAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedA.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedA))
            )
            .andExpect(status().isOk());

        // Validate the A in the database
        List<AOld> aList = aRepository.findAll();
        assertThat(aList).hasSize(databaseSizeBeforeUpdate);
        AOld testA = aList.get(aList.size() - 1);
        assertThat(testA.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testA.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testA.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testA.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void patchNonExistingA() throws Exception {
        int databaseSizeBeforeUpdate = aRepository.findAll().size();
        a.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, a.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(a))
            )
            .andExpect(status().isBadRequest());

        // Validate the A in the database
        List<AOld> aList = aRepository.findAll();
        assertThat(aList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchA() throws Exception {
        int databaseSizeBeforeUpdate = aRepository.findAll().size();
        a.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(a))
            )
            .andExpect(status().isBadRequest());

        // Validate the A in the database
        List<AOld> aList = aRepository.findAll();
        assertThat(aList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamA() throws Exception {
        int databaseSizeBeforeUpdate = aRepository.findAll().size();
        a.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(a)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the A in the database
        List<AOld> aList = aRepository.findAll();
        assertThat(aList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteA() throws Exception {
        // Initialize the database
        aRepository.saveAndFlush(a);

        int databaseSizeBeforeDelete = aRepository.findAll().size();

        // Delete the a
        restAMockMvc.perform(delete(ENTITY_API_URL_ID, a.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AOld> aList = aRepository.findAll();
        assertThat(aList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
