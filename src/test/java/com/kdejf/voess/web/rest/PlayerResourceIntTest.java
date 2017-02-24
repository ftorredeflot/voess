package com.kdejf.voess.web.rest;

import com.kdejf.voess.VoessApp;

import com.kdejf.voess.domain.Player;
import com.kdejf.voess.repository.PlayerRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kdejf.voess.domain.enumeration.SexGender;
/**
 * Test class for the PlayerResource REST controller.
 *
 * @see PlayerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VoessApp.class)
public class PlayerResourceIntTest {

    private static final String DEFAULT_PLAYER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PLAYER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PLAYER_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PLAYER_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PLAYER_NICK = "AAAAAAAAAA";
    private static final String UPDATED_PLAYER_NICK = "BBBBBBBBBB";

    private static final Integer DEFAULT_PLAYER_AGE = 1;
    private static final Integer UPDATED_PLAYER_AGE = 2;

    private static final byte[] DEFAULT_PLAYER_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PLAYER_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PLAYER_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PLAYER_IMAGE_CONTENT_TYPE = "image/png";

    private static final SexGender DEFAULT_PLAYER_SEX = SexGender.MALE;
    private static final SexGender UPDATED_PLAYER_SEX = SexGender.FEMALE;

    private static final Integer DEFAULT_PLAYER_SCORE = 1;
    private static final Integer UPDATED_PLAYER_SCORE = 2;

    @Inject
    private PlayerRepository playerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPlayerMockMvc;

    private Player player;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PlayerResource playerResource = new PlayerResource();
        ReflectionTestUtils.setField(playerResource, "playerRepository", playerRepository);
        this.restPlayerMockMvc = MockMvcBuilders.standaloneSetup(playerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Player createEntity(EntityManager em) {
        Player player = new Player()
                .playerName(DEFAULT_PLAYER_NAME)
                .playerLastName(DEFAULT_PLAYER_LAST_NAME)
                .playerNick(DEFAULT_PLAYER_NICK)
                .playerAge(DEFAULT_PLAYER_AGE)
                .playerImage(DEFAULT_PLAYER_IMAGE)
                .playerImageContentType(DEFAULT_PLAYER_IMAGE_CONTENT_TYPE)
                .playerSex(DEFAULT_PLAYER_SEX)
                .playerScore(DEFAULT_PLAYER_SCORE);
        return player;
    }

    @Before
    public void initTest() {
        player = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlayer() throws Exception {
        int databaseSizeBeforeCreate = playerRepository.findAll().size();

        // Create the Player

        restPlayerMockMvc.perform(post("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(player)))
            .andExpect(status().isCreated());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeCreate + 1);
        Player testPlayer = playerList.get(playerList.size() - 1);
        assertThat(testPlayer.getPlayerName()).isEqualTo(DEFAULT_PLAYER_NAME);
        assertThat(testPlayer.getPlayerLastName()).isEqualTo(DEFAULT_PLAYER_LAST_NAME);
        assertThat(testPlayer.getPlayerNick()).isEqualTo(DEFAULT_PLAYER_NICK);
        assertThat(testPlayer.getPlayerAge()).isEqualTo(DEFAULT_PLAYER_AGE);
        assertThat(testPlayer.getPlayerImage()).isEqualTo(DEFAULT_PLAYER_IMAGE);
        assertThat(testPlayer.getPlayerImageContentType()).isEqualTo(DEFAULT_PLAYER_IMAGE_CONTENT_TYPE);
        assertThat(testPlayer.getPlayerSex()).isEqualTo(DEFAULT_PLAYER_SEX);
        assertThat(testPlayer.getPlayerScore()).isEqualTo(DEFAULT_PLAYER_SCORE);
    }

    @Test
    @Transactional
    public void createPlayerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = playerRepository.findAll().size();

        // Create the Player with an existing ID
        Player existingPlayer = new Player();
        existingPlayer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerMockMvc.perform(post("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingPlayer)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPlayers() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList
        restPlayerMockMvc.perform(get("/api/players?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(player.getId().intValue())))
            .andExpect(jsonPath("$.[*].playerName").value(hasItem(DEFAULT_PLAYER_NAME.toString())))
            .andExpect(jsonPath("$.[*].playerLastName").value(hasItem(DEFAULT_PLAYER_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].playerNick").value(hasItem(DEFAULT_PLAYER_NICK.toString())))
            .andExpect(jsonPath("$.[*].playerAge").value(hasItem(DEFAULT_PLAYER_AGE)))
            .andExpect(jsonPath("$.[*].playerImageContentType").value(hasItem(DEFAULT_PLAYER_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].playerImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_PLAYER_IMAGE))))
            .andExpect(jsonPath("$.[*].playerSex").value(hasItem(DEFAULT_PLAYER_SEX.toString())))
            .andExpect(jsonPath("$.[*].playerScore").value(hasItem(DEFAULT_PLAYER_SCORE)));
    }

    @Test
    @Transactional
    public void getPlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get the player
        restPlayerMockMvc.perform(get("/api/players/{id}", player.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(player.getId().intValue()))
            .andExpect(jsonPath("$.playerName").value(DEFAULT_PLAYER_NAME.toString()))
            .andExpect(jsonPath("$.playerLastName").value(DEFAULT_PLAYER_LAST_NAME.toString()))
            .andExpect(jsonPath("$.playerNick").value(DEFAULT_PLAYER_NICK.toString()))
            .andExpect(jsonPath("$.playerAge").value(DEFAULT_PLAYER_AGE))
            .andExpect(jsonPath("$.playerImageContentType").value(DEFAULT_PLAYER_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.playerImage").value(Base64Utils.encodeToString(DEFAULT_PLAYER_IMAGE)))
            .andExpect(jsonPath("$.playerSex").value(DEFAULT_PLAYER_SEX.toString()))
            .andExpect(jsonPath("$.playerScore").value(DEFAULT_PLAYER_SCORE));
    }

    @Test
    @Transactional
    public void getNonExistingPlayer() throws Exception {
        // Get the player
        restPlayerMockMvc.perform(get("/api/players/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);
        int databaseSizeBeforeUpdate = playerRepository.findAll().size();

        // Update the player
        Player updatedPlayer = playerRepository.findOne(player.getId());
        updatedPlayer
                .playerName(UPDATED_PLAYER_NAME)
                .playerLastName(UPDATED_PLAYER_LAST_NAME)
                .playerNick(UPDATED_PLAYER_NICK)
                .playerAge(UPDATED_PLAYER_AGE)
                .playerImage(UPDATED_PLAYER_IMAGE)
                .playerImageContentType(UPDATED_PLAYER_IMAGE_CONTENT_TYPE)
                .playerSex(UPDATED_PLAYER_SEX)
                .playerScore(UPDATED_PLAYER_SCORE);

        restPlayerMockMvc.perform(put("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlayer)))
            .andExpect(status().isOk());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeUpdate);
        Player testPlayer = playerList.get(playerList.size() - 1);
        assertThat(testPlayer.getPlayerName()).isEqualTo(UPDATED_PLAYER_NAME);
        assertThat(testPlayer.getPlayerLastName()).isEqualTo(UPDATED_PLAYER_LAST_NAME);
        assertThat(testPlayer.getPlayerNick()).isEqualTo(UPDATED_PLAYER_NICK);
        assertThat(testPlayer.getPlayerAge()).isEqualTo(UPDATED_PLAYER_AGE);
        assertThat(testPlayer.getPlayerImage()).isEqualTo(UPDATED_PLAYER_IMAGE);
        assertThat(testPlayer.getPlayerImageContentType()).isEqualTo(UPDATED_PLAYER_IMAGE_CONTENT_TYPE);
        assertThat(testPlayer.getPlayerSex()).isEqualTo(UPDATED_PLAYER_SEX);
        assertThat(testPlayer.getPlayerScore()).isEqualTo(UPDATED_PLAYER_SCORE);
    }

    @Test
    @Transactional
    public void updateNonExistingPlayer() throws Exception {
        int databaseSizeBeforeUpdate = playerRepository.findAll().size();

        // Create the Player

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPlayerMockMvc.perform(put("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(player)))
            .andExpect(status().isCreated());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);
        int databaseSizeBeforeDelete = playerRepository.findAll().size();

        // Get the player
        restPlayerMockMvc.perform(delete("/api/players/{id}", player.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
