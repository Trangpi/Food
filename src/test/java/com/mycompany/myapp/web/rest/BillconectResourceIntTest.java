package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.FoodApp;

import com.mycompany.myapp.domain.Billconect;
import com.mycompany.myapp.repository.BillconectRepository;
import com.mycompany.myapp.repository.search.BillconectSearchRepository;

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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BillconectResource REST controller.
 *
 * @see BillconectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodApp.class)
public class BillconectResourceIntTest {

    private static final Integer DEFAULT_SOLUONG = 1;
    private static final Integer UPDATED_SOLUONG = 2;

    @Inject
    private BillconectRepository billconectRepository;

    @Inject
    private BillconectSearchRepository billconectSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBillconectMockMvc;

    private Billconect billconect;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BillconectResource billconectResource = new BillconectResource();
        ReflectionTestUtils.setField(billconectResource, "billconectSearchRepository", billconectSearchRepository);
        ReflectionTestUtils.setField(billconectResource, "billconectRepository", billconectRepository);
        this.restBillconectMockMvc = MockMvcBuilders.standaloneSetup(billconectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Billconect createEntity(EntityManager em) {
        Billconect billconect = new Billconect()
                .soluong(DEFAULT_SOLUONG);
        return billconect;
    }

    @Before
    public void initTest() {
        billconectSearchRepository.deleteAll();
        billconect = createEntity(em);
    }

    @Test
    @Transactional
    public void createBillconect() throws Exception {
        int databaseSizeBeforeCreate = billconectRepository.findAll().size();

        // Create the Billconect

        restBillconectMockMvc.perform(post("/api/billconects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billconect)))
                .andExpect(status().isCreated());

        // Validate the Billconect in the database
        List<Billconect> billconects = billconectRepository.findAll();
        assertThat(billconects).hasSize(databaseSizeBeforeCreate + 1);
        Billconect testBillconect = billconects.get(billconects.size() - 1);
        assertThat(testBillconect.getSoluong()).isEqualTo(DEFAULT_SOLUONG);

        // Validate the Billconect in ElasticSearch
        Billconect billconectEs = billconectSearchRepository.findOne(testBillconect.getId());
        assertThat(billconectEs).isEqualToComparingFieldByField(testBillconect);
    }

    @Test
    @Transactional
    public void checkSoluongIsRequired() throws Exception {
        int databaseSizeBeforeTest = billconectRepository.findAll().size();
        // set the field null
        billconect.setSoluong(null);

        // Create the Billconect, which fails.

        restBillconectMockMvc.perform(post("/api/billconects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billconect)))
                .andExpect(status().isBadRequest());

        List<Billconect> billconects = billconectRepository.findAll();
        assertThat(billconects).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBillconects() throws Exception {
        // Initialize the database
        billconectRepository.saveAndFlush(billconect);

        // Get all the billconects
        restBillconectMockMvc.perform(get("/api/billconects?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(billconect.getId().intValue())))
                .andExpect(jsonPath("$.[*].soluong").value(hasItem(DEFAULT_SOLUONG)));
    }

    @Test
    @Transactional
    public void getBillconect() throws Exception {
        // Initialize the database
        billconectRepository.saveAndFlush(billconect);

        // Get the billconect
        restBillconectMockMvc.perform(get("/api/billconects/{id}", billconect.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(billconect.getId().intValue()))
            .andExpect(jsonPath("$.soluong").value(DEFAULT_SOLUONG));
    }

    @Test
    @Transactional
    public void getNonExistingBillconect() throws Exception {
        // Get the billconect
        restBillconectMockMvc.perform(get("/api/billconects/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBillconect() throws Exception {
        // Initialize the database
        billconectRepository.saveAndFlush(billconect);
        billconectSearchRepository.save(billconect);
        int databaseSizeBeforeUpdate = billconectRepository.findAll().size();

        // Update the billconect
        Billconect updatedBillconect = billconectRepository.findOne(billconect.getId());
        updatedBillconect
                .soluong(UPDATED_SOLUONG);

        restBillconectMockMvc.perform(put("/api/billconects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBillconect)))
                .andExpect(status().isOk());

        // Validate the Billconect in the database
        List<Billconect> billconects = billconectRepository.findAll();
        assertThat(billconects).hasSize(databaseSizeBeforeUpdate);
        Billconect testBillconect = billconects.get(billconects.size() - 1);
        assertThat(testBillconect.getSoluong()).isEqualTo(UPDATED_SOLUONG);

        // Validate the Billconect in ElasticSearch
        Billconect billconectEs = billconectSearchRepository.findOne(testBillconect.getId());
        assertThat(billconectEs).isEqualToComparingFieldByField(testBillconect);
    }

    @Test
    @Transactional
    public void deleteBillconect() throws Exception {
        // Initialize the database
        billconectRepository.saveAndFlush(billconect);
        billconectSearchRepository.save(billconect);
        int databaseSizeBeforeDelete = billconectRepository.findAll().size();

        // Get the billconect
        restBillconectMockMvc.perform(delete("/api/billconects/{id}", billconect.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean billconectExistsInEs = billconectSearchRepository.exists(billconect.getId());
        assertThat(billconectExistsInEs).isFalse();

        // Validate the database is empty
        List<Billconect> billconects = billconectRepository.findAll();
        assertThat(billconects).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBillconect() throws Exception {
        // Initialize the database
        billconectRepository.saveAndFlush(billconect);
        billconectSearchRepository.save(billconect);

        // Search the billconect
        restBillconectMockMvc.perform(get("/api/_search/billconects?query=id:" + billconect.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billconect.getId().intValue())))
            .andExpect(jsonPath("$.[*].soluong").value(hasItem(DEFAULT_SOLUONG)));
    }
}
