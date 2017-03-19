package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.FoodApp;

import com.mycompany.myapp.domain.MonAn;
import com.mycompany.myapp.repository.MonAnRepository;
import com.mycompany.myapp.repository.search.MonAnSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MonAnResource REST controller.
 *
 * @see MonAnResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodApp.class)
public class MonAnResourceIntTest {

    private static final String DEFAULT_TEN = "AAAAA";
    private static final String UPDATED_TEN = "BBBBB";

    private static final Integer DEFAULT_GIA = 1;
    private static final Integer UPDATED_GIA = 2;

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_THELOAI = "AAAAA";
    private static final String UPDATED_THELOAI = "BBBBB";

    private static final Integer DEFAULT_SALE = 0;
    private static final Integer UPDATED_SALE = 1;

    private static final LocalDate DEFAULT_DATE_CREATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private MonAnRepository monAnRepository;

    @Inject
    private MonAnSearchRepository monAnSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMonAnMockMvc;

    private MonAn monAn;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MonAnResource monAnResource = new MonAnResource();
        ReflectionTestUtils.setField(monAnResource, "monAnSearchRepository", monAnSearchRepository);
        ReflectionTestUtils.setField(monAnResource, "monAnRepository", monAnRepository);
        this.restMonAnMockMvc = MockMvcBuilders.standaloneSetup(monAnResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonAn createEntity(EntityManager em) {
        MonAn monAn = new MonAn()
                .ten(DEFAULT_TEN)
                .gia(DEFAULT_GIA)
                .image(DEFAULT_IMAGE)
                .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
                .theloai(DEFAULT_THELOAI)
                .sale(DEFAULT_SALE)
                .date_create(DEFAULT_DATE_CREATE);
        return monAn;
    }

    @Before
    public void initTest() {
        monAnSearchRepository.deleteAll();
        monAn = createEntity(em);
    }

    @Test
    @Transactional
    public void createMonAn() throws Exception {
        int databaseSizeBeforeCreate = monAnRepository.findAll().size();

        // Create the MonAn

        restMonAnMockMvc.perform(post("/api/mon-ans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(monAn)))
                .andExpect(status().isCreated());

        // Validate the MonAn in the database
        List<MonAn> monAns = monAnRepository.findAll();
        assertThat(monAns).hasSize(databaseSizeBeforeCreate + 1);
        MonAn testMonAn = monAns.get(monAns.size() - 1);
        assertThat(testMonAn.getTen()).isEqualTo(DEFAULT_TEN);
        assertThat(testMonAn.getGia()).isEqualTo(DEFAULT_GIA);
        assertThat(testMonAn.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testMonAn.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testMonAn.getTheloai()).isEqualTo(DEFAULT_THELOAI);
        assertThat(testMonAn.getSale()).isEqualTo(DEFAULT_SALE);
        assertThat(testMonAn.getDate_create()).isEqualTo(DEFAULT_DATE_CREATE);

        // Validate the MonAn in ElasticSearch
        MonAn monAnEs = monAnSearchRepository.findOne(testMonAn.getId());
        assertThat(monAnEs).isEqualToComparingFieldByField(testMonAn);
    }

    @Test
    @Transactional
    public void checkTenIsRequired() throws Exception {
        int databaseSizeBeforeTest = monAnRepository.findAll().size();
        // set the field null
        monAn.setTen(null);

        // Create the MonAn, which fails.

        restMonAnMockMvc.perform(post("/api/mon-ans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(monAn)))
                .andExpect(status().isBadRequest());

        List<MonAn> monAns = monAnRepository.findAll();
        assertThat(monAns).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGiaIsRequired() throws Exception {
        int databaseSizeBeforeTest = monAnRepository.findAll().size();
        // set the field null
        monAn.setGia(null);

        // Create the MonAn, which fails.

        restMonAnMockMvc.perform(post("/api/mon-ans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(monAn)))
                .andExpect(status().isBadRequest());

        List<MonAn> monAns = monAnRepository.findAll();
        assertThat(monAns).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImageIsRequired() throws Exception {
        int databaseSizeBeforeTest = monAnRepository.findAll().size();
        // set the field null
        monAn.setImage(null);

        // Create the MonAn, which fails.

        restMonAnMockMvc.perform(post("/api/mon-ans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(monAn)))
                .andExpect(status().isBadRequest());

        List<MonAn> monAns = monAnRepository.findAll();
        assertThat(monAns).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTheloaiIsRequired() throws Exception {
        int databaseSizeBeforeTest = monAnRepository.findAll().size();
        // set the field null
        monAn.setTheloai(null);

        // Create the MonAn, which fails.

        restMonAnMockMvc.perform(post("/api/mon-ans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(monAn)))
                .andExpect(status().isBadRequest());

        List<MonAn> monAns = monAnRepository.findAll();
        assertThat(monAns).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSaleIsRequired() throws Exception {
        int databaseSizeBeforeTest = monAnRepository.findAll().size();
        // set the field null
        monAn.setSale(null);

        // Create the MonAn, which fails.

        restMonAnMockMvc.perform(post("/api/mon-ans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(monAn)))
                .andExpect(status().isBadRequest());

        List<MonAn> monAns = monAnRepository.findAll();
        assertThat(monAns).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMonAns() throws Exception {
        // Initialize the database
        monAnRepository.saveAndFlush(monAn);

        // Get all the monAns
        restMonAnMockMvc.perform(get("/api/mon-ans?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(monAn.getId().intValue())))
                .andExpect(jsonPath("$.[*].ten").value(hasItem(DEFAULT_TEN.toString())))
                .andExpect(jsonPath("$.[*].gia").value(hasItem(DEFAULT_GIA)))
                .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
                .andExpect(jsonPath("$.[*].theloai").value(hasItem(DEFAULT_THELOAI.toString())))
                .andExpect(jsonPath("$.[*].sale").value(hasItem(DEFAULT_SALE)))
                .andExpect(jsonPath("$.[*].date_create").value(hasItem(DEFAULT_DATE_CREATE.toString())));
    }

    @Test
    @Transactional
    public void getMonAn() throws Exception {
        // Initialize the database
        monAnRepository.saveAndFlush(monAn);

        // Get the monAn
        restMonAnMockMvc.perform(get("/api/mon-ans/{id}", monAn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(monAn.getId().intValue()))
            .andExpect(jsonPath("$.ten").value(DEFAULT_TEN.toString()))
            .andExpect(jsonPath("$.gia").value(DEFAULT_GIA))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.theloai").value(DEFAULT_THELOAI.toString()))
            .andExpect(jsonPath("$.sale").value(DEFAULT_SALE))
            .andExpect(jsonPath("$.date_create").value(DEFAULT_DATE_CREATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMonAn() throws Exception {
        // Get the monAn
        restMonAnMockMvc.perform(get("/api/mon-ans/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMonAn() throws Exception {
        // Initialize the database
        monAnRepository.saveAndFlush(monAn);
        monAnSearchRepository.save(monAn);
        int databaseSizeBeforeUpdate = monAnRepository.findAll().size();

        // Update the monAn
        MonAn updatedMonAn = monAnRepository.findOne(monAn.getId());
        updatedMonAn
                .ten(UPDATED_TEN)
                .gia(UPDATED_GIA)
                .image(UPDATED_IMAGE)
                .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
                .theloai(UPDATED_THELOAI)
                .sale(UPDATED_SALE)
                .date_create(UPDATED_DATE_CREATE);

        restMonAnMockMvc.perform(put("/api/mon-ans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMonAn)))
                .andExpect(status().isOk());

        // Validate the MonAn in the database
        List<MonAn> monAns = monAnRepository.findAll();
        assertThat(monAns).hasSize(databaseSizeBeforeUpdate);
        MonAn testMonAn = monAns.get(monAns.size() - 1);
        assertThat(testMonAn.getTen()).isEqualTo(UPDATED_TEN);
        assertThat(testMonAn.getGia()).isEqualTo(UPDATED_GIA);
        assertThat(testMonAn.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMonAn.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testMonAn.getTheloai()).isEqualTo(UPDATED_THELOAI);
        assertThat(testMonAn.getSale()).isEqualTo(UPDATED_SALE);
        assertThat(testMonAn.getDate_create()).isEqualTo(UPDATED_DATE_CREATE);

        // Validate the MonAn in ElasticSearch
        MonAn monAnEs = monAnSearchRepository.findOne(testMonAn.getId());
        assertThat(monAnEs).isEqualToComparingFieldByField(testMonAn);
    }

    @Test
    @Transactional
    public void deleteMonAn() throws Exception {
        // Initialize the database
        monAnRepository.saveAndFlush(monAn);
        monAnSearchRepository.save(monAn);
        int databaseSizeBeforeDelete = monAnRepository.findAll().size();

        // Get the monAn
        restMonAnMockMvc.perform(delete("/api/mon-ans/{id}", monAn.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean monAnExistsInEs = monAnSearchRepository.exists(monAn.getId());
        assertThat(monAnExistsInEs).isFalse();

        // Validate the database is empty
        List<MonAn> monAns = monAnRepository.findAll();
        assertThat(monAns).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMonAn() throws Exception {
        // Initialize the database
        monAnRepository.saveAndFlush(monAn);
        monAnSearchRepository.save(monAn);

        // Search the monAn
        restMonAnMockMvc.perform(get("/api/_search/mon-ans?query=id:" + monAn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monAn.getId().intValue())))
            .andExpect(jsonPath("$.[*].ten").value(hasItem(DEFAULT_TEN.toString())))
            .andExpect(jsonPath("$.[*].gia").value(hasItem(DEFAULT_GIA)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].theloai").value(hasItem(DEFAULT_THELOAI.toString())))
            .andExpect(jsonPath("$.[*].sale").value(hasItem(DEFAULT_SALE)))
            .andExpect(jsonPath("$.[*].date_create").value(hasItem(DEFAULT_DATE_CREATE.toString())));
    }
}
