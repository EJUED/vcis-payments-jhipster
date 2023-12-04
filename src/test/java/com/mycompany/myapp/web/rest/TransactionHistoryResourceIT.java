package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TransactionHistory;
import com.mycompany.myapp.repository.TransactionHistoryRepository;
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
 * Integration tests for the {@link TransactionHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionHistoryResourceIT {

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/transaction-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionHistoryMockMvc;

    private TransactionHistory transactionHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionHistory createEntity(EntityManager em) {
        TransactionHistory transactionHistory = new TransactionHistory()
            .user(DEFAULT_USER)
            .description(DEFAULT_DESCRIPTION)
            .creationDate(DEFAULT_CREATION_DATE);
        return transactionHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionHistory createUpdatedEntity(EntityManager em) {
        TransactionHistory transactionHistory = new TransactionHistory()
            .user(UPDATED_USER)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE);
        return transactionHistory;
    }

    @BeforeEach
    public void initTest() {
        transactionHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createTransactionHistory() throws Exception {
        int databaseSizeBeforeCreate = transactionHistoryRepository.findAll().size();
        // Create the TransactionHistory
        restTransactionHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionHistory))
            )
            .andExpect(status().isCreated());

        // Validate the TransactionHistory in the database
        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionHistory testTransactionHistory = transactionHistoryList.get(transactionHistoryList.size() - 1);
        assertThat(testTransactionHistory.getUser()).isEqualTo(DEFAULT_USER);
        assertThat(testTransactionHistory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTransactionHistory.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    void createTransactionHistoryWithExistingId() throws Exception {
        // Create the TransactionHistory with an existing ID
        transactionHistory.setId(1L);

        int databaseSizeBeforeCreate = transactionHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionHistory in the database
        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTransactionHistories() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList
        restTransactionHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getTransactionHistory() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get the transactionHistory
        restTransactionHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionHistory.getId().intValue()))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTransactionHistory() throws Exception {
        // Get the transactionHistory
        restTransactionHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransactionHistory() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        int databaseSizeBeforeUpdate = transactionHistoryRepository.findAll().size();

        // Update the transactionHistory
        TransactionHistory updatedTransactionHistory = transactionHistoryRepository.findById(transactionHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransactionHistory are not directly saved in db
        em.detach(updatedTransactionHistory);
        updatedTransactionHistory.user(UPDATED_USER).description(UPDATED_DESCRIPTION).creationDate(UPDATED_CREATION_DATE);

        restTransactionHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTransactionHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTransactionHistory))
            )
            .andExpect(status().isOk());

        // Validate the TransactionHistory in the database
        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeUpdate);
        TransactionHistory testTransactionHistory = transactionHistoryList.get(transactionHistoryList.size() - 1);
        assertThat(testTransactionHistory.getUser()).isEqualTo(UPDATED_USER);
        assertThat(testTransactionHistory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTransactionHistory.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTransactionHistory() throws Exception {
        int databaseSizeBeforeUpdate = transactionHistoryRepository.findAll().size();
        transactionHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionHistory in the database
        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionHistory() throws Exception {
        int databaseSizeBeforeUpdate = transactionHistoryRepository.findAll().size();
        transactionHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionHistory in the database
        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionHistory() throws Exception {
        int databaseSizeBeforeUpdate = transactionHistoryRepository.findAll().size();
        transactionHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionHistoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionHistory in the database
        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionHistoryWithPatch() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        int databaseSizeBeforeUpdate = transactionHistoryRepository.findAll().size();

        // Update the transactionHistory using partial update
        TransactionHistory partialUpdatedTransactionHistory = new TransactionHistory();
        partialUpdatedTransactionHistory.setId(transactionHistory.getId());

        partialUpdatedTransactionHistory.user(UPDATED_USER).creationDate(UPDATED_CREATION_DATE);

        restTransactionHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionHistory))
            )
            .andExpect(status().isOk());

        // Validate the TransactionHistory in the database
        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeUpdate);
        TransactionHistory testTransactionHistory = transactionHistoryList.get(transactionHistoryList.size() - 1);
        assertThat(testTransactionHistory.getUser()).isEqualTo(UPDATED_USER);
        assertThat(testTransactionHistory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTransactionHistory.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTransactionHistoryWithPatch() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        int databaseSizeBeforeUpdate = transactionHistoryRepository.findAll().size();

        // Update the transactionHistory using partial update
        TransactionHistory partialUpdatedTransactionHistory = new TransactionHistory();
        partialUpdatedTransactionHistory.setId(transactionHistory.getId());

        partialUpdatedTransactionHistory.user(UPDATED_USER).description(UPDATED_DESCRIPTION).creationDate(UPDATED_CREATION_DATE);

        restTransactionHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionHistory))
            )
            .andExpect(status().isOk());

        // Validate the TransactionHistory in the database
        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeUpdate);
        TransactionHistory testTransactionHistory = transactionHistoryList.get(transactionHistoryList.size() - 1);
        assertThat(testTransactionHistory.getUser()).isEqualTo(UPDATED_USER);
        assertThat(testTransactionHistory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTransactionHistory.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTransactionHistory() throws Exception {
        int databaseSizeBeforeUpdate = transactionHistoryRepository.findAll().size();
        transactionHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionHistory in the database
        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionHistory() throws Exception {
        int databaseSizeBeforeUpdate = transactionHistoryRepository.findAll().size();
        transactionHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionHistory in the database
        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionHistory() throws Exception {
        int databaseSizeBeforeUpdate = transactionHistoryRepository.findAll().size();
        transactionHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionHistory in the database
        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransactionHistory() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        int databaseSizeBeforeDelete = transactionHistoryRepository.findAll().size();

        // Delete the transactionHistory
        restTransactionHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
