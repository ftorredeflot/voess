package com.kdejf.voess.web.rest;

import com.kdejf.voess.VoessApp;

import com.kdejf.voess.domain.TeamFormer;
import com.kdejf.voess.repository.TeamFormerRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.kdejf.voess.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TeamFormerResource REST controller.
 *
 * @see TeamFormerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VoessApp.class)
public class TeamFormerResourceIntTest {

    private static final ZonedDateTime DEFAULT_START_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_FINSH_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FINSH_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Inject
    private TeamFormerRepository teamFormerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTeamFormerMockMvc;

    private TeamFormer teamFormer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TeamFormerResource teamFormerResource = new TeamFormerResource();
        ReflectionTestUtils.setField(teamFormerResource, "teamFormerRepository", teamFormerRepository);
        this.restTeamFormerMockMvc = MockMvcBuilders.standaloneSetup(teamFormerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeamFormer createEntity(EntityManager em) {
        TeamFormer teamFormer = new TeamFormer()
                .startDateTime(DEFAULT_START_DATE_TIME)
                .finshDateTime(DEFAULT_FINSH_DATE_TIME);
        return teamFormer;
    }

    @Before
    public void initTest() {
        teamFormer = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeamFormer() throws Exception {
        int databaseSizeBeforeCreate = teamFormerRepository.findAll().size();

        // Create the TeamFormer

        restTeamFormerMockMvc.perform(post("/api/team-formers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamFormer)))
            .andExpect(status().isCreated());

        // Validate the TeamFormer in the database
        List<TeamFormer> teamFormerList = teamFormerRepository.findAll();
        assertThat(teamFormerList).hasSize(databaseSizeBeforeCreate + 1);
        TeamFormer testTeamFormer = teamFormerList.get(teamFormerList.size() - 1);
        assertThat(testTeamFormer.getStartDateTime()).isEqualTo(DEFAULT_START_DATE_TIME);
        assertThat(testTeamFormer.getFinshDateTime()).isEqualTo(DEFAULT_FINSH_DATE_TIME);
    }

    @Test
    @Transactional
    public void createTeamFormerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teamFormerRepository.findAll().size();

        // Create the TeamFormer with an existing ID
        TeamFormer existingTeamFormer = new TeamFormer();
        existingTeamFormer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamFormerMockMvc.perform(post("/api/team-formers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingTeamFormer)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TeamFormer> teamFormerList = teamFormerRepository.findAll();
        assertThat(teamFormerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTeamFormers() throws Exception {
        // Initialize the database
        teamFormerRepository.saveAndFlush(teamFormer);

        // Get all the teamFormerList
        restTeamFormerMockMvc.perform(get("/api/team-formers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamFormer.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDateTime").value(hasItem(sameInstant(DEFAULT_START_DATE_TIME))))
            .andExpect(jsonPath("$.[*].finshDateTime").value(hasItem(sameInstant(DEFAULT_FINSH_DATE_TIME))));
    }

    @Test
    @Transactional
    public void getTeamFormer() throws Exception {
        // Initialize the database
        teamFormerRepository.saveAndFlush(teamFormer);

        // Get the teamFormer
        restTeamFormerMockMvc.perform(get("/api/team-formers/{id}", teamFormer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(teamFormer.getId().intValue()))
            .andExpect(jsonPath("$.startDateTime").value(sameInstant(DEFAULT_START_DATE_TIME)))
            .andExpect(jsonPath("$.finshDateTime").value(sameInstant(DEFAULT_FINSH_DATE_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingTeamFormer() throws Exception {
        // Get the teamFormer
        restTeamFormerMockMvc.perform(get("/api/team-formers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeamFormer() throws Exception {
        // Initialize the database
        teamFormerRepository.saveAndFlush(teamFormer);
        int databaseSizeBeforeUpdate = teamFormerRepository.findAll().size();

        // Update the teamFormer
        TeamFormer updatedTeamFormer = teamFormerRepository.findOne(teamFormer.getId());
        updatedTeamFormer
                .startDateTime(UPDATED_START_DATE_TIME)
                .finshDateTime(UPDATED_FINSH_DATE_TIME);

        restTeamFormerMockMvc.perform(put("/api/team-formers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTeamFormer)))
            .andExpect(status().isOk());

        // Validate the TeamFormer in the database
        List<TeamFormer> teamFormerList = teamFormerRepository.findAll();
        assertThat(teamFormerList).hasSize(databaseSizeBeforeUpdate);
        TeamFormer testTeamFormer = teamFormerList.get(teamFormerList.size() - 1);
        assertThat(testTeamFormer.getStartDateTime()).isEqualTo(UPDATED_START_DATE_TIME);
        assertThat(testTeamFormer.getFinshDateTime()).isEqualTo(UPDATED_FINSH_DATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingTeamFormer() throws Exception {
        int databaseSizeBeforeUpdate = teamFormerRepository.findAll().size();

        // Create the TeamFormer

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTeamFormerMockMvc.perform(put("/api/team-formers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamFormer)))
            .andExpect(status().isCreated());

        // Validate the TeamFormer in the database
        List<TeamFormer> teamFormerList = teamFormerRepository.findAll();
        assertThat(teamFormerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTeamFormer() throws Exception {
        // Initialize the database
        teamFormerRepository.saveAndFlush(teamFormer);
        int databaseSizeBeforeDelete = teamFormerRepository.findAll().size();

        // Get the teamFormer
        restTeamFormerMockMvc.perform(delete("/api/team-formers/{id}", teamFormer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TeamFormer> teamFormerList = teamFormerRepository.findAll();
        assertThat(teamFormerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
