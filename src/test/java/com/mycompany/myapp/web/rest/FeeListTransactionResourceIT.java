package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.FeeListTransaction;
import com.mycompany.myapp.repository.FeeListTransactionRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FeeListTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeeListTransactionResourceIT {

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DELETION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DELETION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/fee-list-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FeeListTransactionRepository feeListTransactionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeeListTransactionMockMvc;

    private FeeListTransaction feeListTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeeListTransaction createEntity(EntityManager em) {
        FeeListTransaction feeListTransaction = new FeeListTransaction()
            .creationDate(DEFAULT_CREATION_DATE)
            .deletionDate(DEFAULT_DELETION_DATE);
        return feeListTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeeListTransaction createUpdatedEntity(EntityManager em) {
        FeeListTransaction feeListTransaction = new FeeListTransaction()
            .creationDate(UPDATED_CREATION_DATE)
            .deletionDate(UPDATED_DELETION_DATE);
        return feeListTransaction;
    }

    @BeforeEach
    public void initTest() {
        feeListTransaction = createEntity(em);
    }

    @Test
    @Transactional
    void createFeeListTransaction() throws Exception {
        int databaseSizeBeforeCreate = feeListTransactionRepository.findAll().size();
        // Create the FeeListTransaction
        restFeeListTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feeListTransaction))
            )
            .andExpect(status().isCreated());

        // Validate the FeeListTransaction in the database
        List<FeeListTransaction> feeListTransactionList = feeListTransactionRepository.findAll();
        assertThat(feeListTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        FeeListTransaction testFeeListTransaction = feeListTransactionList.get(feeListTransactionList.size() - 1);
        assertThat(testFeeListTransaction.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testFeeListTransaction.getDeletionDate()).isEqualTo(DEFAULT_DELETION_DATE);
    }

    @Test
    @Transactional
    void createFeeListTransactionWithExistingId() throws Exception {
        // Create the FeeListTransaction with an existing ID
        feeListTransaction.setId(1L);

        int databaseSizeBeforeCreate = feeListTransactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeeListTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feeListTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeeListTransaction in the database
        List<FeeListTransaction> feeListTransactionList = feeListTransactionRepository.findAll();
        assertThat(feeListTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFeeListTransactions() throws Exception {
        // Initialize the database
        feeListTransactionRepository.saveAndFlush(feeListTransaction);

        // Get all the feeListTransactionList
        restFeeListTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feeListTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].deletionDate").value(hasItem(DEFAULT_DELETION_DATE.toString())));
    }

    @Test
    @Transactional
    void getFeeListTransaction() throws Exception {
        // Initialize the database
        feeListTransactionRepository.saveAndFlush(feeListTransaction);

        // Get the feeListTransaction
        restFeeListTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, feeListTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feeListTransaction.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.deletionDate").value(DEFAULT_DELETION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFeeListTransaction() throws Exception {
        // Get the feeListTransaction
        restFeeListTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeeListTransaction() throws Exception {
        // Initialize the database
        feeListTransactionRepository.saveAndFlush(feeListTransaction);

        int databaseSizeBeforeUpdate = feeListTransactionRepository.findAll().size();

        // Update the feeListTransaction
        FeeListTransaction updatedFeeListTransaction = feeListTransactionRepository.findById(feeListTransaction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeeListTransaction are not directly saved in db
        em.detach(updatedFeeListTransaction);
        updatedFeeListTransaction.creationDate(UPDATED_CREATION_DATE).deletionDate(UPDATED_DELETION_DATE);

        restFeeListTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFeeListTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFeeListTransaction))
            )
            .andExpect(status().isOk());

        // Validate the FeeListTransaction in the database
        List<FeeListTransaction> feeListTransactionList = feeListTransactionRepository.findAll();
        assertThat(feeListTransactionList).hasSize(databaseSizeBeforeUpdate);
        FeeListTransaction testFeeListTransaction = feeListTransactionList.get(feeListTransactionList.size() - 1);
        assertThat(testFeeListTransaction.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFeeListTransaction.getDeletionDate()).isEqualTo(UPDATED_DELETION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingFeeListTransaction() throws Exception {
        int databaseSizeBeforeUpdate = feeListTransactionRepository.findAll().size();
        feeListTransaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeeListTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feeListTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feeListTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeeListTransaction in the database
        List<FeeListTransaction> feeListTransactionList = feeListTransactionRepository.findAll();
        assertThat(feeListTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeeListTransaction() throws Exception {
        int databaseSizeBeforeUpdate = feeListTransactionRepository.findAll().size();
        feeListTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeListTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feeListTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeeListTransaction in the database
        List<FeeListTransaction> feeListTransactionList = feeListTransactionRepository.findAll();
        assertThat(feeListTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeeListTransaction() throws Exception {
        int databaseSizeBeforeUpdate = feeListTransactionRepository.findAll().size();
        feeListTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeListTransactionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feeListTransaction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeeListTransaction in the database
        List<FeeListTransaction> feeListTransactionList = feeListTransactionRepository.findAll();
        assertThat(feeListTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeeListTransactionWithPatch() throws Exception {
        // Initialize the database
        feeListTransactionRepository.saveAndFlush(feeListTransaction);

        int databaseSizeBeforeUpdate = feeListTransactionRepository.findAll().size();

        // Update the feeListTransaction using partial update
        FeeListTransaction partialUpdatedFeeListTransaction = new FeeListTransaction();
        partialUpdatedFeeListTransaction.setId(feeListTransaction.getId());

        partialUpdatedFeeListTransaction.creationDate(UPDATED_CREATION_DATE);

        restFeeListTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeeListTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeeListTransaction))
            )
            .andExpect(status().isOk());

        // Validate the FeeListTransaction in the database
        List<FeeListTransaction> feeListTransactionList = feeListTransactionRepository.findAll();
        assertThat(feeListTransactionList).hasSize(databaseSizeBeforeUpdate);
        FeeListTransaction testFeeListTransaction = feeListTransactionList.get(feeListTransactionList.size() - 1);
        assertThat(testFeeListTransaction.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFeeListTransaction.getDeletionDate()).isEqualTo(DEFAULT_DELETION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateFeeListTransactionWithPatch() throws Exception {
        // Initialize the database
        feeListTransactionRepository.saveAndFlush(feeListTransaction);

        int databaseSizeBeforeUpdate = feeListTransactionRepository.findAll().size();

        // Update the feeListTransaction using partial update
        FeeListTransaction partialUpdatedFeeListTransaction = new FeeListTransaction();
        partialUpdatedFeeListTransaction.setId(feeListTransaction.getId());

        partialUpdatedFeeListTransaction.creationDate(UPDATED_CREATION_DATE).deletionDate(UPDATED_DELETION_DATE);

        restFeeListTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeeListTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeeListTransaction))
            )
            .andExpect(status().isOk());

        // Validate the FeeListTransaction in the database
        List<FeeListTransaction> feeListTransactionList = feeListTransactionRepository.findAll();
        assertThat(feeListTransactionList).hasSize(databaseSizeBeforeUpdate);
        FeeListTransaction testFeeListTransaction = feeListTransactionList.get(feeListTransactionList.size() - 1);
        assertThat(testFeeListTransaction.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFeeListTransaction.getDeletionDate()).isEqualTo(UPDATED_DELETION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingFeeListTransaction() throws Exception {
        int databaseSizeBeforeUpdate = feeListTransactionRepository.findAll().size();
        feeListTransaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeeListTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feeListTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feeListTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeeListTransaction in the database
        List<FeeListTransaction> feeListTransactionList = feeListTransactionRepository.findAll();
        assertThat(feeListTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeeListTransaction() throws Exception {
        int databaseSizeBeforeUpdate = feeListTransactionRepository.findAll().size();
        feeListTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeListTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feeListTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeeListTransaction in the database
        List<FeeListTransaction> feeListTransactionList = feeListTransactionRepository.findAll();
        assertThat(feeListTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeeListTransaction() throws Exception {
        int databaseSizeBeforeUpdate = feeListTransactionRepository.findAll().size();
        feeListTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeListTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feeListTransaction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeeListTransaction in the database
        List<FeeListTransaction> feeListTransactionList = feeListTransactionRepository.findAll();
        assertThat(feeListTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeeListTransaction() throws Exception {
        // Initialize the database
        feeListTransactionRepository.saveAndFlush(feeListTransaction);

        int databaseSizeBeforeDelete = feeListTransactionRepository.findAll().size();

        // Delete the feeListTransaction
        restFeeListTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, feeListTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FeeListTransaction> feeListTransactionList = feeListTransactionRepository.findAll();
        assertThat(feeListTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
