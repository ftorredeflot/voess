package com.kdejf.voess.web.rest;

import com.kdejf.voess.VoessApp;

import com.kdejf.voess.domain.Tournament;
import com.kdejf.voess.repository.TournamentRepository;

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
import org.springframework.util.Base64Utils;

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
 * Test class for the TournamentResource REST controller.
 *
 * @see TournamentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VoessApp.class)
public class TournamentResourceIntTest {

    private static final String DEFAULT_TOURNAMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TOURNAMENT_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TOURNAMENT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TOURNAMENT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final byte[] DEFAULT_TOURNAMENT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_TOURNAMENT_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_TOURNAMENT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_TOURNAMENT_IMAGE_CONTENT_TYPE = "image/png";

    @Inject
    private TournamentRepository tournamentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTournamentMockMvc;

    private Tournament tournament;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TournamentResource tournamentResource = new TournamentResource();
        ReflectionTestUtils.setField(tournamentResource, "tournamentRepository", tournamentRepository);
        this.restTournamentMockMvc = MockMvcBuilders.standaloneSetup(tournamentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tournament createEntity(EntityManager em) {
        Tournament tournament = new Tournament()
                .tournamentName(DEFAULT_TOURNAMENT_NAME)
                .tournamentDate(DEFAULT_TOURNAMENT_DATE)
                .tournamentImage(DEFAULT_TOURNAMENT_IMAGE)
                .tournamentImageContentType(DEFAULT_TOURNAMENT_IMAGE_CONTENT_TYPE);
        return tournament;
    }

    @Before
    public void initTest() {
        tournament = createEntity(em);
    }

    @Test
    @Transactional
    public void createTournament() throws Exception {
        int databaseSizeBeforeCreate = tournamentRepository.findAll().size();

        // Create the Tournament

        restTournamentMockMvc.perform(post("/api/tournaments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournament)))
            .andExpect(status().isCreated());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeCreate + 1);
        Tournament testTournament = tournamentList.get(tournamentList.size() - 1);
        assertThat(testTournament.getTournamentName()).isEqualTo(DEFAULT_TOURNAMENT_NAME);
        assertThat(testTournament.getTournamentDate()).isEqualTo(DEFAULT_TOURNAMENT_DATE);
        assertThat(testTournament.getTournamentImage()).isEqualTo(DEFAULT_TOURNAMENT_IMAGE);
        assertThat(testTournament.getTournamentImageContentType()).isEqualTo(DEFAULT_TOURNAMENT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createTournamentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tournamentRepository.findAll().size();

        // Create the Tournament with an existing ID
        Tournament existingTournament = new Tournament();
        existingTournament.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTournamentMockMvc.perform(post("/api/tournaments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingTournament)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTournaments() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList
        restTournamentMockMvc.perform(get("/api/tournaments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tournament.getId().intValue())))
            .andExpect(jsonPath("$.[*].tournamentName").value(hasItem(DEFAULT_TOURNAMENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].tournamentDate").value(hasItem(sameInstant(DEFAULT_TOURNAMENT_DATE))))
            .andExpect(jsonPath("$.[*].tournamentImageContentType").value(hasItem(DEFAULT_TOURNAMENT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].tournamentImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_TOURNAMENT_IMAGE))));
    }

    @Test
    @Transactional
    public void getTournament() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get the tournament
        restTournamentMockMvc.perform(get("/api/tournaments/{id}", tournament.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tournament.getId().intValue()))
            .andExpect(jsonPath("$.tournamentName").value(DEFAULT_TOURNAMENT_NAME.toString()))
            .andExpect(jsonPath("$.tournamentDate").value(sameInstant(DEFAULT_TOURNAMENT_DATE)))
            .andExpect(jsonPath("$.tournamentImageContentType").value(DEFAULT_TOURNAMENT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.tournamentImage").value(Base64Utils.encodeToString(DEFAULT_TOURNAMENT_IMAGE)));
    }

    @Test
    @Transactional
    public void getNonExistingTournament() throws Exception {
        // Get the tournament
        restTournamentMockMvc.perform(get("/api/tournaments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTournament() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);
        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();

        // Update the tournament
        Tournament updatedTournament = tournamentRepository.findOne(tournament.getId());
        updatedTournament
                .tournamentName(UPDATED_TOURNAMENT_NAME)
                .tournamentDate(UPDATED_TOURNAMENT_DATE)
                .tournamentImage(UPDATED_TOURNAMENT_IMAGE)
                .tournamentImageContentType(UPDATED_TOURNAMENT_IMAGE_CONTENT_TYPE);

        restTournamentMockMvc.perform(put("/api/tournaments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTournament)))
            .andExpect(status().isOk());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
        Tournament testTournament = tournamentList.get(tournamentList.size() - 1);
        assertThat(testTournament.getTournamentName()).isEqualTo(UPDATED_TOURNAMENT_NAME);
        assertThat(testTournament.getTournamentDate()).isEqualTo(UPDATED_TOURNAMENT_DATE);
        assertThat(testTournament.getTournamentImage()).isEqualTo(UPDATED_TOURNAMENT_IMAGE);
        assertThat(testTournament.getTournamentImageContentType()).isEqualTo(UPDATED_TOURNAMENT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingTournament() throws Exception {
        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();

        // Create the Tournament

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTournamentMockMvc.perform(put("/api/tournaments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournament)))
            .andExpect(status().isCreated());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTournament() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);
        int databaseSizeBeforeDelete = tournamentRepository.findAll().size();

        // Get the tournament
        restTournamentMockMvc.perform(delete("/api/tournaments/{id}", tournament.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
