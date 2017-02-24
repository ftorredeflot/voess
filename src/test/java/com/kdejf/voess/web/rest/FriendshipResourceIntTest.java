package com.kdejf.voess.web.rest;

import com.kdejf.voess.VoessApp;

import com.kdejf.voess.domain.Friendship;
import com.kdejf.voess.repository.FriendshipRepository;

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
 * Test class for the FriendshipResource REST controller.
 *
 * @see FriendshipResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VoessApp.class)
public class FriendshipResourceIntTest {

    private static final ZonedDateTime DEFAULT_START_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_FINISH_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FINISH_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Inject
    private FriendshipRepository friendshipRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFriendshipMockMvc;

    private Friendship friendship;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FriendshipResource friendshipResource = new FriendshipResource();
        ReflectionTestUtils.setField(friendshipResource, "friendshipRepository", friendshipRepository);
        this.restFriendshipMockMvc = MockMvcBuilders.standaloneSetup(friendshipResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Friendship createEntity(EntityManager em) {
        Friendship friendship = new Friendship()
                .startDateTime(DEFAULT_START_DATE_TIME)
                .finishDateTime(DEFAULT_FINISH_DATE_TIME);
        return friendship;
    }

    @Before
    public void initTest() {
        friendship = createEntity(em);
    }

    @Test
    @Transactional
    public void createFriendship() throws Exception {
        int databaseSizeBeforeCreate = friendshipRepository.findAll().size();

        // Create the Friendship

        restFriendshipMockMvc.perform(post("/api/friendships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(friendship)))
            .andExpect(status().isCreated());

        // Validate the Friendship in the database
        List<Friendship> friendshipList = friendshipRepository.findAll();
        assertThat(friendshipList).hasSize(databaseSizeBeforeCreate + 1);
        Friendship testFriendship = friendshipList.get(friendshipList.size() - 1);
        assertThat(testFriendship.getStartDateTime()).isEqualTo(DEFAULT_START_DATE_TIME);
        assertThat(testFriendship.getFinishDateTime()).isEqualTo(DEFAULT_FINISH_DATE_TIME);
    }

    @Test
    @Transactional
    public void createFriendshipWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = friendshipRepository.findAll().size();

        // Create the Friendship with an existing ID
        Friendship existingFriendship = new Friendship();
        existingFriendship.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFriendshipMockMvc.perform(post("/api/friendships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingFriendship)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Friendship> friendshipList = friendshipRepository.findAll();
        assertThat(friendshipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFriendships() throws Exception {
        // Initialize the database
        friendshipRepository.saveAndFlush(friendship);

        // Get all the friendshipList
        restFriendshipMockMvc.perform(get("/api/friendships?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(friendship.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDateTime").value(hasItem(sameInstant(DEFAULT_START_DATE_TIME))))
            .andExpect(jsonPath("$.[*].finishDateTime").value(hasItem(sameInstant(DEFAULT_FINISH_DATE_TIME))));
    }

    @Test
    @Transactional
    public void getFriendship() throws Exception {
        // Initialize the database
        friendshipRepository.saveAndFlush(friendship);

        // Get the friendship
        restFriendshipMockMvc.perform(get("/api/friendships/{id}", friendship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(friendship.getId().intValue()))
            .andExpect(jsonPath("$.startDateTime").value(sameInstant(DEFAULT_START_DATE_TIME)))
            .andExpect(jsonPath("$.finishDateTime").value(sameInstant(DEFAULT_FINISH_DATE_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingFriendship() throws Exception {
        // Get the friendship
        restFriendshipMockMvc.perform(get("/api/friendships/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFriendship() throws Exception {
        // Initialize the database
        friendshipRepository.saveAndFlush(friendship);
        int databaseSizeBeforeUpdate = friendshipRepository.findAll().size();

        // Update the friendship
        Friendship updatedFriendship = friendshipRepository.findOne(friendship.getId());
        updatedFriendship
                .startDateTime(UPDATED_START_DATE_TIME)
                .finishDateTime(UPDATED_FINISH_DATE_TIME);

        restFriendshipMockMvc.perform(put("/api/friendships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFriendship)))
            .andExpect(status().isOk());

        // Validate the Friendship in the database
        List<Friendship> friendshipList = friendshipRepository.findAll();
        assertThat(friendshipList).hasSize(databaseSizeBeforeUpdate);
        Friendship testFriendship = friendshipList.get(friendshipList.size() - 1);
        assertThat(testFriendship.getStartDateTime()).isEqualTo(UPDATED_START_DATE_TIME);
        assertThat(testFriendship.getFinishDateTime()).isEqualTo(UPDATED_FINISH_DATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingFriendship() throws Exception {
        int databaseSizeBeforeUpdate = friendshipRepository.findAll().size();

        // Create the Friendship

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFriendshipMockMvc.perform(put("/api/friendships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(friendship)))
            .andExpect(status().isCreated());

        // Validate the Friendship in the database
        List<Friendship> friendshipList = friendshipRepository.findAll();
        assertThat(friendshipList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFriendship() throws Exception {
        // Initialize the database
        friendshipRepository.saveAndFlush(friendship);
        int databaseSizeBeforeDelete = friendshipRepository.findAll().size();

        // Get the friendship
        restFriendshipMockMvc.perform(delete("/api/friendships/{id}", friendship.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Friendship> friendshipList = friendshipRepository.findAll();
        assertThat(friendshipList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
