package com.kdejf.voess.web.rest;

import com.kdejf.voess.VoessApp;

import com.kdejf.voess.domain.UserLikedVideo;
import com.kdejf.voess.repository.UserLikedVideoRepository;

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
 * Test class for the UserLikedVideoResource REST controller.
 *
 * @see UserLikedVideoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VoessApp.class)
public class UserLikedVideoResourceIntTest {

    private static final ZonedDateTime DEFAULT_START_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_USER_LIKED = false;
    private static final Boolean UPDATED_USER_LIKED = true;

    @Inject
    private UserLikedVideoRepository userLikedVideoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUserLikedVideoMockMvc;

    private UserLikedVideo userLikedVideo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserLikedVideoResource userLikedVideoResource = new UserLikedVideoResource();
        ReflectionTestUtils.setField(userLikedVideoResource, "userLikedVideoRepository", userLikedVideoRepository);
        this.restUserLikedVideoMockMvc = MockMvcBuilders.standaloneSetup(userLikedVideoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserLikedVideo createEntity(EntityManager em) {
        UserLikedVideo userLikedVideo = new UserLikedVideo()
                .startDateTime(DEFAULT_START_DATE_TIME)
                .userLiked(DEFAULT_USER_LIKED);
        return userLikedVideo;
    }

    @Before
    public void initTest() {
        userLikedVideo = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserLikedVideo() throws Exception {
        int databaseSizeBeforeCreate = userLikedVideoRepository.findAll().size();

        // Create the UserLikedVideo

        restUserLikedVideoMockMvc.perform(post("/api/user-liked-videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userLikedVideo)))
            .andExpect(status().isCreated());

        // Validate the UserLikedVideo in the database
        List<UserLikedVideo> userLikedVideoList = userLikedVideoRepository.findAll();
        assertThat(userLikedVideoList).hasSize(databaseSizeBeforeCreate + 1);
        UserLikedVideo testUserLikedVideo = userLikedVideoList.get(userLikedVideoList.size() - 1);
        assertThat(testUserLikedVideo.getStartDateTime()).isEqualTo(DEFAULT_START_DATE_TIME);
        assertThat(testUserLikedVideo.isUserLiked()).isEqualTo(DEFAULT_USER_LIKED);
    }

    @Test
    @Transactional
    public void createUserLikedVideoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userLikedVideoRepository.findAll().size();

        // Create the UserLikedVideo with an existing ID
        UserLikedVideo existingUserLikedVideo = new UserLikedVideo();
        existingUserLikedVideo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserLikedVideoMockMvc.perform(post("/api/user-liked-videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingUserLikedVideo)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserLikedVideo> userLikedVideoList = userLikedVideoRepository.findAll();
        assertThat(userLikedVideoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUserLikedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLikedVideoRepository.findAll().size();
        // set the field null
        userLikedVideo.setUserLiked(null);

        // Create the UserLikedVideo, which fails.

        restUserLikedVideoMockMvc.perform(post("/api/user-liked-videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userLikedVideo)))
            .andExpect(status().isBadRequest());

        List<UserLikedVideo> userLikedVideoList = userLikedVideoRepository.findAll();
        assertThat(userLikedVideoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserLikedVideos() throws Exception {
        // Initialize the database
        userLikedVideoRepository.saveAndFlush(userLikedVideo);

        // Get all the userLikedVideoList
        restUserLikedVideoMockMvc.perform(get("/api/user-liked-videos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userLikedVideo.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDateTime").value(hasItem(sameInstant(DEFAULT_START_DATE_TIME))))
            .andExpect(jsonPath("$.[*].userLiked").value(hasItem(DEFAULT_USER_LIKED.booleanValue())));
    }

    @Test
    @Transactional
    public void getUserLikedVideo() throws Exception {
        // Initialize the database
        userLikedVideoRepository.saveAndFlush(userLikedVideo);

        // Get the userLikedVideo
        restUserLikedVideoMockMvc.perform(get("/api/user-liked-videos/{id}", userLikedVideo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userLikedVideo.getId().intValue()))
            .andExpect(jsonPath("$.startDateTime").value(sameInstant(DEFAULT_START_DATE_TIME)))
            .andExpect(jsonPath("$.userLiked").value(DEFAULT_USER_LIKED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserLikedVideo() throws Exception {
        // Get the userLikedVideo
        restUserLikedVideoMockMvc.perform(get("/api/user-liked-videos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserLikedVideo() throws Exception {
        // Initialize the database
        userLikedVideoRepository.saveAndFlush(userLikedVideo);
        int databaseSizeBeforeUpdate = userLikedVideoRepository.findAll().size();

        // Update the userLikedVideo
        UserLikedVideo updatedUserLikedVideo = userLikedVideoRepository.findOne(userLikedVideo.getId());
        updatedUserLikedVideo
                .startDateTime(UPDATED_START_DATE_TIME)
                .userLiked(UPDATED_USER_LIKED);

        restUserLikedVideoMockMvc.perform(put("/api/user-liked-videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserLikedVideo)))
            .andExpect(status().isOk());

        // Validate the UserLikedVideo in the database
        List<UserLikedVideo> userLikedVideoList = userLikedVideoRepository.findAll();
        assertThat(userLikedVideoList).hasSize(databaseSizeBeforeUpdate);
        UserLikedVideo testUserLikedVideo = userLikedVideoList.get(userLikedVideoList.size() - 1);
        assertThat(testUserLikedVideo.getStartDateTime()).isEqualTo(UPDATED_START_DATE_TIME);
        assertThat(testUserLikedVideo.isUserLiked()).isEqualTo(UPDATED_USER_LIKED);
    }

    @Test
    @Transactional
    public void updateNonExistingUserLikedVideo() throws Exception {
        int databaseSizeBeforeUpdate = userLikedVideoRepository.findAll().size();

        // Create the UserLikedVideo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserLikedVideoMockMvc.perform(put("/api/user-liked-videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userLikedVideo)))
            .andExpect(status().isCreated());

        // Validate the UserLikedVideo in the database
        List<UserLikedVideo> userLikedVideoList = userLikedVideoRepository.findAll();
        assertThat(userLikedVideoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserLikedVideo() throws Exception {
        // Initialize the database
        userLikedVideoRepository.saveAndFlush(userLikedVideo);
        int databaseSizeBeforeDelete = userLikedVideoRepository.findAll().size();

        // Get the userLikedVideo
        restUserLikedVideoMockMvc.perform(delete("/api/user-liked-videos/{id}", userLikedVideo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserLikedVideo> userLikedVideoList = userLikedVideoRepository.findAll();
        assertThat(userLikedVideoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
