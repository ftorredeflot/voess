package com.kdejf.voess.web.rest;

import com.kdejf.voess.VoessApp;

import com.kdejf.voess.domain.Video;
import com.kdejf.voess.repository.VideoRepository;

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
 * Test class for the VideoResource REST controller.
 *
 * @see VideoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VoessApp.class)
public class VideoResourceIntTest {

    private static final String DEFAULT_VIDEO_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_VIDEO_DURATION = 1;
    private static final Integer UPDATED_VIDEO_DURATION = 2;

    private static final ZonedDateTime DEFAULT_VIDEO_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_VIDEO_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_VIDEO_VIEWER_COUNT = 1;
    private static final Integer UPDATED_VIDEO_VIEWER_COUNT = 2;

    private static final Integer DEFAULT_VIDEO_VIEWER_COUNT_LIVE = 1;
    private static final Integer UPDATED_VIDEO_VIEWER_COUNT_LIVE = 2;

    private static final String DEFAULT_VIDEO_URL = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_VIDEO_PICKS = 1;
    private static final Integer UPDATED_VIDEO_PICKS = 2;

    private static final Integer DEFAULT_VIDEO_GAME_START = 1;
    private static final Integer UPDATED_VIDEO_GAME_START = 2;

    @Inject
    private VideoRepository videoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restVideoMockMvc;

    private Video video;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VideoResource videoResource = new VideoResource();
        ReflectionTestUtils.setField(videoResource, "videoRepository", videoRepository);
        this.restVideoMockMvc = MockMvcBuilders.standaloneSetup(videoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Video createEntity(EntityManager em) {
        Video video = new Video()
                .videoName(DEFAULT_VIDEO_NAME)
                .videoDuration(DEFAULT_VIDEO_DURATION)
                .videoDate(DEFAULT_VIDEO_DATE)
                .videoViewerCount(DEFAULT_VIDEO_VIEWER_COUNT)
                .videoViewerCountLive(DEFAULT_VIDEO_VIEWER_COUNT_LIVE)
                .videoUrl(DEFAULT_VIDEO_URL)
                .videoPicks(DEFAULT_VIDEO_PICKS)
                .videoGameStart(DEFAULT_VIDEO_GAME_START);
        return video;
    }

    @Before
    public void initTest() {
        video = createEntity(em);
    }

    @Test
    @Transactional
    public void createVideo() throws Exception {
        int databaseSizeBeforeCreate = videoRepository.findAll().size();

        // Create the Video

        restVideoMockMvc.perform(post("/api/videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isCreated());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeCreate + 1);
        Video testVideo = videoList.get(videoList.size() - 1);
        assertThat(testVideo.getVideoName()).isEqualTo(DEFAULT_VIDEO_NAME);
        assertThat(testVideo.getVideoDuration()).isEqualTo(DEFAULT_VIDEO_DURATION);
        assertThat(testVideo.getVideoDate()).isEqualTo(DEFAULT_VIDEO_DATE);
        assertThat(testVideo.getVideoViewerCount()).isEqualTo(DEFAULT_VIDEO_VIEWER_COUNT);
        assertThat(testVideo.getVideoViewerCountLive()).isEqualTo(DEFAULT_VIDEO_VIEWER_COUNT_LIVE);
        assertThat(testVideo.getVideoUrl()).isEqualTo(DEFAULT_VIDEO_URL);
        assertThat(testVideo.getVideoPicks()).isEqualTo(DEFAULT_VIDEO_PICKS);
        assertThat(testVideo.getVideoGameStart()).isEqualTo(DEFAULT_VIDEO_GAME_START);
    }

    @Test
    @Transactional
    public void createVideoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = videoRepository.findAll().size();

        // Create the Video with an existing ID
        Video existingVideo = new Video();
        existingVideo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideoMockMvc.perform(post("/api/videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingVideo)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllVideos() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        // Get all the videoList
        restVideoMockMvc.perform(get("/api/videos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(video.getId().intValue())))
            .andExpect(jsonPath("$.[*].videoName").value(hasItem(DEFAULT_VIDEO_NAME.toString())))
            .andExpect(jsonPath("$.[*].videoDuration").value(hasItem(DEFAULT_VIDEO_DURATION)))
            .andExpect(jsonPath("$.[*].videoDate").value(hasItem(sameInstant(DEFAULT_VIDEO_DATE))))
            .andExpect(jsonPath("$.[*].videoViewerCount").value(hasItem(DEFAULT_VIDEO_VIEWER_COUNT)))
            .andExpect(jsonPath("$.[*].videoViewerCountLive").value(hasItem(DEFAULT_VIDEO_VIEWER_COUNT_LIVE)))
            .andExpect(jsonPath("$.[*].videoUrl").value(hasItem(DEFAULT_VIDEO_URL.toString())))
            .andExpect(jsonPath("$.[*].videoPicks").value(hasItem(DEFAULT_VIDEO_PICKS)))
            .andExpect(jsonPath("$.[*].videoGameStart").value(hasItem(DEFAULT_VIDEO_GAME_START)));
    }

    @Test
    @Transactional
    public void getVideo() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        // Get the video
        restVideoMockMvc.perform(get("/api/videos/{id}", video.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(video.getId().intValue()))
            .andExpect(jsonPath("$.videoName").value(DEFAULT_VIDEO_NAME.toString()))
            .andExpect(jsonPath("$.videoDuration").value(DEFAULT_VIDEO_DURATION))
            .andExpect(jsonPath("$.videoDate").value(sameInstant(DEFAULT_VIDEO_DATE)))
            .andExpect(jsonPath("$.videoViewerCount").value(DEFAULT_VIDEO_VIEWER_COUNT))
            .andExpect(jsonPath("$.videoViewerCountLive").value(DEFAULT_VIDEO_VIEWER_COUNT_LIVE))
            .andExpect(jsonPath("$.videoUrl").value(DEFAULT_VIDEO_URL.toString()))
            .andExpect(jsonPath("$.videoPicks").value(DEFAULT_VIDEO_PICKS))
            .andExpect(jsonPath("$.videoGameStart").value(DEFAULT_VIDEO_GAME_START));
    }

    @Test
    @Transactional
    public void getNonExistingVideo() throws Exception {
        // Get the video
        restVideoMockMvc.perform(get("/api/videos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVideo() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);
        int databaseSizeBeforeUpdate = videoRepository.findAll().size();

        // Update the video
        Video updatedVideo = videoRepository.findOne(video.getId());
        updatedVideo
                .videoName(UPDATED_VIDEO_NAME)
                .videoDuration(UPDATED_VIDEO_DURATION)
                .videoDate(UPDATED_VIDEO_DATE)
                .videoViewerCount(UPDATED_VIDEO_VIEWER_COUNT)
                .videoViewerCountLive(UPDATED_VIDEO_VIEWER_COUNT_LIVE)
                .videoUrl(UPDATED_VIDEO_URL)
                .videoPicks(UPDATED_VIDEO_PICKS)
                .videoGameStart(UPDATED_VIDEO_GAME_START);

        restVideoMockMvc.perform(put("/api/videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVideo)))
            .andExpect(status().isOk());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeUpdate);
        Video testVideo = videoList.get(videoList.size() - 1);
        assertThat(testVideo.getVideoName()).isEqualTo(UPDATED_VIDEO_NAME);
        assertThat(testVideo.getVideoDuration()).isEqualTo(UPDATED_VIDEO_DURATION);
        assertThat(testVideo.getVideoDate()).isEqualTo(UPDATED_VIDEO_DATE);
        assertThat(testVideo.getVideoViewerCount()).isEqualTo(UPDATED_VIDEO_VIEWER_COUNT);
        assertThat(testVideo.getVideoViewerCountLive()).isEqualTo(UPDATED_VIDEO_VIEWER_COUNT_LIVE);
        assertThat(testVideo.getVideoUrl()).isEqualTo(UPDATED_VIDEO_URL);
        assertThat(testVideo.getVideoPicks()).isEqualTo(UPDATED_VIDEO_PICKS);
        assertThat(testVideo.getVideoGameStart()).isEqualTo(UPDATED_VIDEO_GAME_START);
    }

    @Test
    @Transactional
    public void updateNonExistingVideo() throws Exception {
        int databaseSizeBeforeUpdate = videoRepository.findAll().size();

        // Create the Video

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVideoMockMvc.perform(put("/api/videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isCreated());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVideo() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);
        int databaseSizeBeforeDelete = videoRepository.findAll().size();

        // Get the video
        restVideoMockMvc.perform(delete("/api/videos/{id}", video.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
