package com.delaiglesia.doctorhouse.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.delaiglesia.doctorhouse.IntegrationTest;
import com.delaiglesia.doctorhouse.domain.Specialty;
import com.delaiglesia.doctorhouse.repository.SpecialtyRepository;
import com.delaiglesia.doctorhouse.service.dto.SpecialtyDTO;
import com.delaiglesia.doctorhouse.service.mapper.SpecialtyMapper;
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

/**
 * Integration tests for the {@link SpecialtyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecialtyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/specialties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private SpecialtyMapper specialtyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialtyMockMvc;

    private Specialty specialty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialty createEntity(EntityManager em) {
        Specialty specialty = new Specialty().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return specialty;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialty createUpdatedEntity(EntityManager em) {
        Specialty specialty = new Specialty().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return specialty;
    }

    @BeforeEach
    public void initTest() {
        specialty = createEntity(em);
    }

    @Test
    @Transactional
    void createSpecialty() throws Exception {
        int databaseSizeBeforeCreate = specialtyRepository.findAll().size();
        // Create the Specialty
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);
        restSpecialtyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialtyDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeCreate + 1);
        Specialty testSpecialty = specialtyList.get(specialtyList.size() - 1);
        assertThat(testSpecialty.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpecialty.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createSpecialtyWithExistingId() throws Exception {
        // Create the Specialty with an existing ID
        specialty.setId(1L);
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        int databaseSizeBeforeCreate = specialtyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialtyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialtyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialtyRepository.findAll().size();
        // set the field null
        specialty.setName(null);

        // Create the Specialty, which fails.
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        restSpecialtyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialtyDTO))
            )
            .andExpect(status().isBadRequest());

        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpecialties() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList
        restSpecialtyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialty.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getSpecialty() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get the specialty
        restSpecialtyMockMvc
            .perform(get(ENTITY_API_URL_ID, specialty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialty.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingSpecialty() throws Exception {
        // Get the specialty
        restSpecialtyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpecialty() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();

        // Update the specialty
        Specialty updatedSpecialty = specialtyRepository.findById(specialty.getId()).get();
        // Disconnect from session so that the updates on updatedSpecialty are not directly saved in db
        em.detach(updatedSpecialty);
        updatedSpecialty.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(updatedSpecialty);

        restSpecialtyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialtyDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialtyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
        Specialty testSpecialty = specialtyList.get(specialtyList.size() - 1);
        assertThat(testSpecialty.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpecialty.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingSpecialty() throws Exception {
        int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();
        specialty.setId(count.incrementAndGet());

        // Create the Specialty
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialtyDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialtyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecialty() throws Exception {
        int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();
        specialty.setId(count.incrementAndGet());

        // Create the Specialty
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialtyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecialty() throws Exception {
        int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();
        specialty.setId(count.incrementAndGet());

        // Create the Specialty
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialtyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecialtyWithPatch() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();

        // Update the specialty using partial update
        Specialty partialUpdatedSpecialty = new Specialty();
        partialUpdatedSpecialty.setId(specialty.getId());

        restSpecialtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialty.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialty))
            )
            .andExpect(status().isOk());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
        Specialty testSpecialty = specialtyList.get(specialtyList.size() - 1);
        assertThat(testSpecialty.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpecialty.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateSpecialtyWithPatch() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();

        // Update the specialty using partial update
        Specialty partialUpdatedSpecialty = new Specialty();
        partialUpdatedSpecialty.setId(specialty.getId());

        partialUpdatedSpecialty.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restSpecialtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialty.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialty))
            )
            .andExpect(status().isOk());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
        Specialty testSpecialty = specialtyList.get(specialtyList.size() - 1);
        assertThat(testSpecialty.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpecialty.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingSpecialty() throws Exception {
        int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();
        specialty.setId(count.incrementAndGet());

        // Create the Specialty
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specialtyDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialtyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecialty() throws Exception {
        int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();
        specialty.setId(count.incrementAndGet());

        // Create the Specialty
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialtyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecialty() throws Exception {
        int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();
        specialty.setId(count.incrementAndGet());

        // Create the Specialty
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialtyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecialty() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        int databaseSizeBeforeDelete = specialtyRepository.findAll().size();

        // Delete the specialty
        restSpecialtyMockMvc
            .perform(delete(ENTITY_API_URL_ID, specialty.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
