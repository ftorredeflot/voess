package com.kdejf.voess.web.rest;

import com.kdejf.voess.VoessApp;

import com.kdejf.voess.domain.UserExt;
import com.kdejf.voess.repository.UserExtRepository;

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
 * Test class for the UserExtResource REST controller.
 *
 * @see UserExtResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VoessApp.class)
public class UserExtResourceIntTest {

    private static final Integer DEFAULT_USER_AGE = 1;
    private static final Integer UPDATED_USER_AGE = 2;

    private static final SexGender DEFAULT_USER_SEX = SexGender.MALE;
    private static final SexGender UPDATED_USER_SEX = SexGender.FEMALE;

    private static final byte[] DEFAULT_USER_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_USER_IMAGE = TestUtil.createByteArray(5242880, "1");
    private static final String DEFAULT_USER_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_USER_IMAGE_CONTENT_TYPE = "image/png";

    @Inject
    private UserExtRepository userExtRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUserExtMockMvc;

    private UserExt userExt;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserExtResource userExtResource = new UserExtResource();
        ReflectionTestUtils.setField(userExtResource, "userExtRepository", userExtRepository);
        this.restUserExtMockMvc = MockMvcBuilders.standaloneSetup(userExtResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserExt createEntity(EntityManager em) {
        UserExt userExt = new UserExt()
                .userAge(DEFAULT_USER_AGE)
                .userSex(DEFAULT_USER_SEX)
                .userImage(DEFAULT_USER_IMAGE)
                .userImageContentType(DEFAULT_USER_IMAGE_CONTENT_TYPE);
        return userExt;
    }

    @Before
    public void initTest() {
        userExt = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserExt() throws Exception {
        int databaseSizeBeforeCreate = userExtRepository.findAll().size();

        // Create the UserExt

        restUserExtMockMvc.perform(post("/api/user-exts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userExt)))
            .andExpect(status().isCreated());

        // Validate the UserExt in the database
        List<UserExt> userExtList = userExtRepository.findAll();
        assertThat(userExtList).hasSize(databaseSizeBeforeCreate + 1);
        UserExt testUserExt = userExtList.get(userExtList.size() - 1);
        assertThat(testUserExt.getUserAge()).isEqualTo(DEFAULT_USER_AGE);
        assertThat(testUserExt.getUserSex()).isEqualTo(DEFAULT_USER_SEX);
        assertThat(testUserExt.getUserImage()).isEqualTo(DEFAULT_USER_IMAGE);
        assertThat(testUserExt.getUserImageContentType()).isEqualTo(DEFAULT_USER_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createUserExtWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userExtRepository.findAll().size();

        // Create the UserExt with an existing ID
        UserExt existingUserExt = new UserExt();
        existingUserExt.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserExtMockMvc.perform(post("/api/user-exts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingUserExt)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserExt> userExtList = userExtRepository.findAll();
        assertThat(userExtList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUserAgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = userExtRepository.findAll().size();
        // set the field null
        userExt.setUserAge(null);

        // Create the UserExt, which fails.

        restUserExtMockMvc.perform(post("/api/user-exts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userExt)))
            .andExpect(status().isBadRequest());

        List<UserExt> userExtList = userExtRepository.findAll();
        assertThat(userExtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserSexIsRequired() throws Exception {
        int databaseSizeBeforeTest = userExtRepository.findAll().size();
        // set the field null
        userExt.setUserSex(null);

        // Create the UserExt, which fails.

        restUserExtMockMvc.perform(post("/api/user-exts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userExt)))
            .andExpect(status().isBadRequest());

        List<UserExt> userExtList = userExtRepository.findAll();
        assertThat(userExtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserImageIsRequired() throws Exception {
        int databaseSizeBeforeTest = userExtRepository.findAll().size();
        // set the field null
        userExt.setUserImage(null);

        // Create the UserExt, which fails.

        restUserExtMockMvc.perform(post("/api/user-exts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userExt)))
            .andExpect(status().isBadRequest());

        List<UserExt> userExtList = userExtRepository.findAll();
        assertThat(userExtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserExts() throws Exception {
        // Initialize the database
        userExtRepository.saveAndFlush(userExt);

        // Get all the userExtList
        restUserExtMockMvc.perform(get("/api/user-exts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userExt.getId().intValue())))
            .andExpect(jsonPath("$.[*].userAge").value(hasItem(DEFAULT_USER_AGE)))
            .andExpect(jsonPath("$.[*].userSex").value(hasItem(DEFAULT_USER_SEX.toString())))
            .andExpect(jsonPath("$.[*].userImageContentType").value(hasItem(DEFAULT_USER_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].userImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_USER_IMAGE))));
    }

    @Test
    @Transactional
    public void getUserExt() throws Exception {
        // Initialize the database
        userExtRepository.saveAndFlush(userExt);

        // Get the userExt
        restUserExtMockMvc.perform(get("/api/user-exts/{id}", userExt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userExt.getId().intValue()))
            .andExpect(jsonPath("$.userAge").value(DEFAULT_USER_AGE))
            .andExpect(jsonPath("$.userSex").value(DEFAULT_USER_SEX.toString()))
            .andExpect(jsonPath("$.userImageContentType").value(DEFAULT_USER_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.userImage").value(Base64Utils.encodeToString(DEFAULT_USER_IMAGE)));
    }

    @Test
    @Transactional
    public void getNonExistingUserExt() throws Exception {
        // Get the userExt
        restUserExtMockMvc.perform(get("/api/user-exts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserExt() throws Exception {
        // Initialize the database
        userExtRepository.saveAndFlush(userExt);
        int databaseSizeBeforeUpdate = userExtRepository.findAll().size();

        // Update the userExt
        UserExt updatedUserExt = userExtRepository.findOne(userExt.getId());
        updatedUserExt
                .userAge(UPDATED_USER_AGE)
                .userSex(UPDATED_USER_SEX)
                .userImage(UPDATED_USER_IMAGE)
                .userImageContentType(UPDATED_USER_IMAGE_CONTENT_TYPE);

        restUserExtMockMvc.perform(put("/api/user-exts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserExt)))
            .andExpect(status().isOk());

        // Validate the UserExt in the database
        List<UserExt> userExtList = userExtRepository.findAll();
        assertThat(userExtList).hasSize(databaseSizeBeforeUpdate);
        UserExt testUserExt = userExtList.get(userExtList.size() - 1);
        assertThat(testUserExt.getUserAge()).isEqualTo(UPDATED_USER_AGE);
        assertThat(testUserExt.getUserSex()).isEqualTo(UPDATED_USER_SEX);
        assertThat(testUserExt.getUserImage()).isEqualTo(UPDATED_USER_IMAGE);
        assertThat(testUserExt.getUserImageContentType()).isEqualTo(UPDATED_USER_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserExt() throws Exception {
        int databaseSizeBeforeUpdate = userExtRepository.findAll().size();

        // Create the UserExt

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserExtMockMvc.perform(put("/api/user-exts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userExt)))
            .andExpect(status().isCreated());

        // Validate the UserExt in the database
        List<UserExt> userExtList = userExtRepository.findAll();
        assertThat(userExtList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserExt() throws Exception {
        // Initialize the database
        userExtRepository.saveAndFlush(userExt);
        int databaseSizeBeforeDelete = userExtRepository.findAll().size();

        // Get the userExt
        restUserExtMockMvc.perform(delete("/api/user-exts/{id}", userExt.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserExt> userExtList = userExtRepository.findAll();
        assertThat(userExtList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
