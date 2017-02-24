package com.kdejf.voess.web.rest;

import com.kdejf.voess.VoessApp;

import com.kdejf.voess.domain.UserLikedTeams;
import com.kdejf.voess.repository.UserLikedTeamsRepository;

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
 * Test class for the UserLikedTeamsResource REST controller.
 *
 * @see UserLikedTeamsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VoessApp.class)
public class UserLikedTeamsResourceIntTest {

    private static final ZonedDateTime DEFAULT_START_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_USER_LIKED = false;
    private static final Boolean UPDATED_USER_LIKED = true;

    @Inject
    private UserLikedTeamsRepository userLikedTeamsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUserLikedTeamsMockMvc;

    private UserLikedTeams userLikedTeams;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserLikedTeamsResource userLikedTeamsResource = new UserLikedTeamsResource();
        ReflectionTestUtils.setField(userLikedTeamsResource, "userLikedTeamsRepository", userLikedTeamsRepository);
        this.restUserLikedTeamsMockMvc = MockMvcBuilders.standaloneSetup(userLikedTeamsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserLikedTeams createEntity(EntityManager em) {
        UserLikedTeams userLikedTeams = new UserLikedTeams()
                .startDateTime(DEFAULT_START_DATE_TIME)
                .userLiked(DEFAULT_USER_LIKED);
        return userLikedTeams;
    }

    @Before
    public void initTest() {
        userLikedTeams = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserLikedTeams() throws Exception {
        int databaseSizeBeforeCreate = userLikedTeamsRepository.findAll().size();

        // Create the UserLikedTeams

        restUserLikedTeamsMockMvc.perform(post("/api/user-liked-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userLikedTeams)))
            .andExpect(status().isCreated());

        // Validate the UserLikedTeams in the database
        List<UserLikedTeams> userLikedTeamsList = userLikedTeamsRepository.findAll();
        assertThat(userLikedTeamsList).hasSize(databaseSizeBeforeCreate + 1);
        UserLikedTeams testUserLikedTeams = userLikedTeamsList.get(userLikedTeamsList.size() - 1);
        assertThat(testUserLikedTeams.getStartDateTime()).isEqualTo(DEFAULT_START_DATE_TIME);
        assertThat(testUserLikedTeams.isUserLiked()).isEqualTo(DEFAULT_USER_LIKED);
    }

    @Test
    @Transactional
    public void createUserLikedTeamsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userLikedTeamsRepository.findAll().size();

        // Create the UserLikedTeams with an existing ID
        UserLikedTeams existingUserLikedTeams = new UserLikedTeams();
        existingUserLikedTeams.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserLikedTeamsMockMvc.perform(post("/api/user-liked-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingUserLikedTeams)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserLikedTeams> userLikedTeamsList = userLikedTeamsRepository.findAll();
        assertThat(userLikedTeamsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUserLikedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLikedTeamsRepository.findAll().size();
        // set the field null
        userLikedTeams.setUserLiked(null);

        // Create the UserLikedTeams, which fails.

        restUserLikedTeamsMockMvc.perform(post("/api/user-liked-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userLikedTeams)))
            .andExpect(status().isBadRequest());

        List<UserLikedTeams> userLikedTeamsList = userLikedTeamsRepository.findAll();
        assertThat(userLikedTeamsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserLikedTeams() throws Exception {
        // Initialize the database
        userLikedTeamsRepository.saveAndFlush(userLikedTeams);

        // Get all the userLikedTeamsList
        restUserLikedTeamsMockMvc.perform(get("/api/user-liked-teams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userLikedTeams.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDateTime").value(hasItem(sameInstant(DEFAULT_START_DATE_TIME))))
            .andExpect(jsonPath("$.[*].userLiked").value(hasItem(DEFAULT_USER_LIKED.booleanValue())));
    }

    @Test
    @Transactional
    public void getUserLikedTeams() throws Exception {
        // Initialize the database
        userLikedTeamsRepository.saveAndFlush(userLikedTeams);

        // Get the userLikedTeams
        restUserLikedTeamsMockMvc.perform(get("/api/user-liked-teams/{id}", userLikedTeams.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userLikedTeams.getId().intValue()))
            .andExpect(jsonPath("$.startDateTime").value(sameInstant(DEFAULT_START_DATE_TIME)))
            .andExpect(jsonPath("$.userLiked").value(DEFAULT_USER_LIKED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserLikedTeams() throws Exception {
        // Get the userLikedTeams
        restUserLikedTeamsMockMvc.perform(get("/api/user-liked-teams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserLikedTeams() throws Exception {
        // Initialize the database
        userLikedTeamsRepository.saveAndFlush(userLikedTeams);
        int databaseSizeBeforeUpdate = userLikedTeamsRepository.findAll().size();

        // Update the userLikedTeams
        UserLikedTeams updatedUserLikedTeams = userLikedTeamsRepository.findOne(userLikedTeams.getId());
        updatedUserLikedTeams
                .startDateTime(UPDATED_START_DATE_TIME)
                .userLiked(UPDATED_USER_LIKED);

        restUserLikedTeamsMockMvc.perform(put("/api/user-liked-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserLikedTeams)))
            .andExpect(status().isOk());

        // Validate the UserLikedTeams in the database
        List<UserLikedTeams> userLikedTeamsList = userLikedTeamsRepository.findAll();
        assertThat(userLikedTeamsList).hasSize(databaseSizeBeforeUpdate);
        UserLikedTeams testUserLikedTeams = userLikedTeamsList.get(userLikedTeamsList.size() - 1);
        assertThat(testUserLikedTeams.getStartDateTime()).isEqualTo(UPDATED_START_DATE_TIME);
        assertThat(testUserLikedTeams.isUserLiked()).isEqualTo(UPDATED_USER_LIKED);
    }

    @Test
    @Transactional
    public void updateNonExistingUserLikedTeams() throws Exception {
        int databaseSizeBeforeUpdate = userLikedTeamsRepository.findAll().size();

        // Create the UserLikedTeams

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserLikedTeamsMockMvc.perform(put("/api/user-liked-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userLikedTeams)))
            .andExpect(status().isCreated());

        // Validate the UserLikedTeams in the database
        List<UserLikedTeams> userLikedTeamsList = userLikedTeamsRepository.findAll();
        assertThat(userLikedTeamsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserLikedTeams() throws Exception {
        // Initialize the database
        userLikedTeamsRepository.saveAndFlush(userLikedTeams);
        int databaseSizeBeforeDelete = userLikedTeamsRepository.findAll().size();

        // Get the userLikedTeams
        restUserLikedTeamsMockMvc.perform(delete("/api/user-liked-teams/{id}", userLikedTeams.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserLikedTeams> userLikedTeamsList = userLikedTeamsRepository.findAll();
        assertThat(userLikedTeamsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
