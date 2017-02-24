package com.kdejf.voess.web.rest;

import com.kdejf.voess.VoessApp;

import com.kdejf.voess.domain.UserLikedPlayer;
import com.kdejf.voess.repository.UserLikedPlayerRepository;

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
 * Test class for the UserLikedPlayerResource REST controller.
 *
 * @see UserLikedPlayerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VoessApp.class)
public class UserLikedPlayerResourceIntTest {

    private static final ZonedDateTime DEFAULT_START_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_USER_LIKED = false;
    private static final Boolean UPDATED_USER_LIKED = true;

    @Inject
    private UserLikedPlayerRepository userLikedPlayerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUserLikedPlayerMockMvc;

    private UserLikedPlayer userLikedPlayer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserLikedPlayerResource userLikedPlayerResource = new UserLikedPlayerResource();
        ReflectionTestUtils.setField(userLikedPlayerResource, "userLikedPlayerRepository", userLikedPlayerRepository);
        this.restUserLikedPlayerMockMvc = MockMvcBuilders.standaloneSetup(userLikedPlayerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserLikedPlayer createEntity(EntityManager em) {
        UserLikedPlayer userLikedPlayer = new UserLikedPlayer()
                .startDateTime(DEFAULT_START_DATE_TIME)
                .userLiked(DEFAULT_USER_LIKED);
        return userLikedPlayer;
    }

    @Before
    public void initTest() {
        userLikedPlayer = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserLikedPlayer() throws Exception {
        int databaseSizeBeforeCreate = userLikedPlayerRepository.findAll().size();

        // Create the UserLikedPlayer

        restUserLikedPlayerMockMvc.perform(post("/api/user-liked-players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userLikedPlayer)))
            .andExpect(status().isCreated());

        // Validate the UserLikedPlayer in the database
        List<UserLikedPlayer> userLikedPlayerList = userLikedPlayerRepository.findAll();
        assertThat(userLikedPlayerList).hasSize(databaseSizeBeforeCreate + 1);
        UserLikedPlayer testUserLikedPlayer = userLikedPlayerList.get(userLikedPlayerList.size() - 1);
        assertThat(testUserLikedPlayer.getStartDateTime()).isEqualTo(DEFAULT_START_DATE_TIME);
        assertThat(testUserLikedPlayer.isUserLiked()).isEqualTo(DEFAULT_USER_LIKED);
    }

    @Test
    @Transactional
    public void createUserLikedPlayerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userLikedPlayerRepository.findAll().size();

        // Create the UserLikedPlayer with an existing ID
        UserLikedPlayer existingUserLikedPlayer = new UserLikedPlayer();
        existingUserLikedPlayer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserLikedPlayerMockMvc.perform(post("/api/user-liked-players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingUserLikedPlayer)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserLikedPlayer> userLikedPlayerList = userLikedPlayerRepository.findAll();
        assertThat(userLikedPlayerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUserLikedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLikedPlayerRepository.findAll().size();
        // set the field null
        userLikedPlayer.setUserLiked(null);

        // Create the UserLikedPlayer, which fails.

        restUserLikedPlayerMockMvc.perform(post("/api/user-liked-players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userLikedPlayer)))
            .andExpect(status().isBadRequest());

        List<UserLikedPlayer> userLikedPlayerList = userLikedPlayerRepository.findAll();
        assertThat(userLikedPlayerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserLikedPlayers() throws Exception {
        // Initialize the database
        userLikedPlayerRepository.saveAndFlush(userLikedPlayer);

        // Get all the userLikedPlayerList
        restUserLikedPlayerMockMvc.perform(get("/api/user-liked-players?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userLikedPlayer.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDateTime").value(hasItem(sameInstant(DEFAULT_START_DATE_TIME))))
            .andExpect(jsonPath("$.[*].userLiked").value(hasItem(DEFAULT_USER_LIKED.booleanValue())));
    }

    @Test
    @Transactional
    public void getUserLikedPlayer() throws Exception {
        // Initialize the database
        userLikedPlayerRepository.saveAndFlush(userLikedPlayer);

        // Get the userLikedPlayer
        restUserLikedPlayerMockMvc.perform(get("/api/user-liked-players/{id}", userLikedPlayer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userLikedPlayer.getId().intValue()))
            .andExpect(jsonPath("$.startDateTime").value(sameInstant(DEFAULT_START_DATE_TIME)))
            .andExpect(jsonPath("$.userLiked").value(DEFAULT_USER_LIKED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserLikedPlayer() throws Exception {
        // Get the userLikedPlayer
        restUserLikedPlayerMockMvc.perform(get("/api/user-liked-players/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserLikedPlayer() throws Exception {
        // Initialize the database
        userLikedPlayerRepository.saveAndFlush(userLikedPlayer);
        int databaseSizeBeforeUpdate = userLikedPlayerRepository.findAll().size();

        // Update the userLikedPlayer
        UserLikedPlayer updatedUserLikedPlayer = userLikedPlayerRepository.findOne(userLikedPlayer.getId());
        updatedUserLikedPlayer
                .startDateTime(UPDATED_START_DATE_TIME)
                .userLiked(UPDATED_USER_LIKED);

        restUserLikedPlayerMockMvc.perform(put("/api/user-liked-players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserLikedPlayer)))
            .andExpect(status().isOk());

        // Validate the UserLikedPlayer in the database
        List<UserLikedPlayer> userLikedPlayerList = userLikedPlayerRepository.findAll();
        assertThat(userLikedPlayerList).hasSize(databaseSizeBeforeUpdate);
        UserLikedPlayer testUserLikedPlayer = userLikedPlayerList.get(userLikedPlayerList.size() - 1);
        assertThat(testUserLikedPlayer.getStartDateTime()).isEqualTo(UPDATED_START_DATE_TIME);
        assertThat(testUserLikedPlayer.isUserLiked()).isEqualTo(UPDATED_USER_LIKED);
    }

    @Test
    @Transactional
    public void updateNonExistingUserLikedPlayer() throws Exception {
        int databaseSizeBeforeUpdate = userLikedPlayerRepository.findAll().size();

        // Create the UserLikedPlayer

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserLikedPlayerMockMvc.perform(put("/api/user-liked-players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userLikedPlayer)))
            .andExpect(status().isCreated());

        // Validate the UserLikedPlayer in the database
        List<UserLikedPlayer> userLikedPlayerList = userLikedPlayerRepository.findAll();
        assertThat(userLikedPlayerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserLikedPlayer() throws Exception {
        // Initialize the database
        userLikedPlayerRepository.saveAndFlush(userLikedPlayer);
        int databaseSizeBeforeDelete = userLikedPlayerRepository.findAll().size();

        // Get the userLikedPlayer
        restUserLikedPlayerMockMvc.perform(delete("/api/user-liked-players/{id}", userLikedPlayer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserLikedPlayer> userLikedPlayerList = userLikedPlayerRepository.findAll();
        assertThat(userLikedPlayerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
