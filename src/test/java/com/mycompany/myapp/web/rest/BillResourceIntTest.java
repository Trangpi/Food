package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.FoodApp;

import com.mycompany.myapp.domain.Bill;
import com.mycompany.myapp.repository.BillRepository;
import com.mycompany.myapp.repository.search.BillSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BillResource REST controller.
 *
 * @see BillResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodApp.class)
public class BillResourceIntTest {

    private static final Integer DEFAULT_SHIP = 1;
    private static final Integer UPDATED_SHIP = 2;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private BillRepository billRepository;

    @Inject
    private BillSearchRepository billSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBillMockMvc;

    private Bill bill;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BillResource billResource = new BillResource();
        ReflectionTestUtils.setField(billResource, "billSearchRepository", billSearchRepository);
        ReflectionTestUtils.setField(billResource, "billRepository", billRepository);
        this.restBillMockMvc = MockMvcBuilders.standaloneSetup(billResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bill createEntity(EntityManager em) {
        Bill bill = new Bill()
                .ship(DEFAULT_SHIP)
                .date(DEFAULT_DATE);
        return bill;
    }

    @Before
    public void initTest() {
        billSearchRepository.deleteAll();
        bill = createEntity(em);
    }

    @Test
    @Transactional
    public void createBill() throws Exception {
        int databaseSizeBeforeCreate = billRepository.findAll().size();

        // Create the Bill

        restBillMockMvc.perform(post("/api/bills")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bill)))
                .andExpect(status().isCreated());

        // Validate the Bill in the database
        List<Bill> bills = billRepository.findAll();
        assertThat(bills).hasSize(databaseSizeBeforeCreate + 1);
        Bill testBill = bills.get(bills.size() - 1);
        assertThat(testBill.getShip()).isEqualTo(DEFAULT_SHIP);
        assertThat(testBill.getDate()).isEqualTo(DEFAULT_DATE);

        // Validate the Bill in ElasticSearch
        Bill billEs = billSearchRepository.findOne(testBill.getId());
        assertThat(billEs).isEqualToComparingFieldByField(testBill);
    }

    @Test
    @Transactional
    public void checkShipIsRequired() throws Exception {
        int databaseSizeBeforeTest = billRepository.findAll().size();
        // set the field null
        bill.setShip(null);

        // Create the Bill, which fails.

        restBillMockMvc.perform(post("/api/bills")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bill)))
                .andExpect(status().isBadRequest());

        List<Bill> bills = billRepository.findAll();
        assertThat(bills).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = billRepository.findAll().size();
        // set the field null
        bill.setDate(null);

        // Create the Bill, which fails.

        restBillMockMvc.perform(post("/api/bills")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bill)))
                .andExpect(status().isBadRequest());

        List<Bill> bills = billRepository.findAll();
        assertThat(bills).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBills() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the bills
        restBillMockMvc.perform(get("/api/bills?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bill.getId().intValue())))
                .andExpect(jsonPath("$.[*].ship").value(hasItem(DEFAULT_SHIP)))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getBill() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get the bill
        restBillMockMvc.perform(get("/api/bills/{id}", bill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bill.getId().intValue()))
            .andExpect(jsonPath("$.ship").value(DEFAULT_SHIP))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBill() throws Exception {
        // Get the bill
        restBillMockMvc.perform(get("/api/bills/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBill() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);
        billSearchRepository.save(bill);
        int databaseSizeBeforeUpdate = billRepository.findAll().size();

        // Update the bill
        Bill updatedBill = billRepository.findOne(bill.getId());
        updatedBill
                .ship(UPDATED_SHIP)
                .date(UPDATED_DATE);

        restBillMockMvc.perform(put("/api/bills")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBill)))
                .andExpect(status().isOk());

        // Validate the Bill in the database
        List<Bill> bills = billRepository.findAll();
        assertThat(bills).hasSize(databaseSizeBeforeUpdate);
        Bill testBill = bills.get(bills.size() - 1);
        assertThat(testBill.getShip()).isEqualTo(UPDATED_SHIP);
        assertThat(testBill.getDate()).isEqualTo(UPDATED_DATE);

        // Validate the Bill in ElasticSearch
        Bill billEs = billSearchRepository.findOne(testBill.getId());
        assertThat(billEs).isEqualToComparingFieldByField(testBill);
    }

    @Test
    @Transactional
    public void deleteBill() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);
        billSearchRepository.save(bill);
        int databaseSizeBeforeDelete = billRepository.findAll().size();

        // Get the bill
        restBillMockMvc.perform(delete("/api/bills/{id}", bill.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean billExistsInEs = billSearchRepository.exists(bill.getId());
        assertThat(billExistsInEs).isFalse();

        // Validate the database is empty
        List<Bill> bills = billRepository.findAll();
        assertThat(bills).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBill() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);
        billSearchRepository.save(bill);

        // Search the bill
        restBillMockMvc.perform(get("/api/_search/bills?query=id:" + bill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bill.getId().intValue())))
            .andExpect(jsonPath("$.[*].ship").value(hasItem(DEFAULT_SHIP)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
}
