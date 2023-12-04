package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.FeeList;
import com.mycompany.myapp.domain.enumeration.HistoryOriginEnum;
import com.mycompany.myapp.domain.enumeration.HistoryTypeEnum;
import com.mycompany.myapp.repository.FeeListRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link FeeListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeeListResourceIT {

    private static final String DEFAULT_DOCUMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_REGISTRATION = "AAAAAAAAAA";
    private static final String UPDATED_STATE_REGISTRATION = "BBBBBBBBBB";

    private static final HistoryOriginEnum DEFAULT_ORIGIN = HistoryOriginEnum.POD;
    private static final HistoryOriginEnum UPDATED_ORIGIN = HistoryOriginEnum.Vendas;

    private static final HistoryTypeEnum DEFAULT_TYPE = HistoryTypeEnum.TaxaDeBaixaDeVolumeDeIsencao;
    private static final HistoryTypeEnum UPDATED_TYPE = HistoryTypeEnum.PrecoDaTecnologiaNaMoega;

    private static final String DEFAULT_ERP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ERP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CROP = "AAAAAAAAAA";
    private static final String UPDATED_CROP = "BBBBBBBBBB";

    private static final String DEFAULT_REVENUE_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_REVENUE_MODEL = "BBBBBBBBBB";

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;

    private static final Integer DEFAULT_OPERATIONAL_YEAR = 1;
    private static final Integer UPDATED_OPERATIONAL_YEAR = 2;

    private static final String DEFAULT_PERCENTAGE = "AAAAAAAAAA";
    private static final String UPDATED_PERCENTAGE = "BBBBBBBBBB";

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    private static final String DEFAULT_CREATION_DATE = "AAAAAAAAAA";
    private static final String UPDATED_CREATION_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATE_DATE = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_VENDOR = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR = "BBBBBBBBBB";

    private static final String DEFAULT_PO_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PO_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fee-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FeeListRepository feeListRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeeListMockMvc;

    private FeeList feeList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeeList createEntity(EntityManager em) {
        FeeList feeList = new FeeList()
            .documentType(DEFAULT_DOCUMENT_TYPE)
            .documentNumber(DEFAULT_DOCUMENT_NUMBER)
            .stateRegistration(DEFAULT_STATE_REGISTRATION)
            .origin(DEFAULT_ORIGIN)
            .type(DEFAULT_TYPE)
            .erpCode(DEFAULT_ERP_CODE)
            .crop(DEFAULT_CROP)
            .revenueModel(DEFAULT_REVENUE_MODEL)
            .month(DEFAULT_MONTH)
            .operationalYear(DEFAULT_OPERATIONAL_YEAR)
            .percentage(DEFAULT_PERCENTAGE)
            .value(DEFAULT_VALUE)
            .creationDate(DEFAULT_CREATION_DATE)
            .updateDate(DEFAULT_UPDATE_DATE)
            .status(DEFAULT_STATUS)
            .vendor(DEFAULT_VENDOR)
            .poNumber(DEFAULT_PO_NUMBER);
        return feeList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeeList createUpdatedEntity(EntityManager em) {
        FeeList feeList = new FeeList()
            .documentType(UPDATED_DOCUMENT_TYPE)
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .stateRegistration(UPDATED_STATE_REGISTRATION)
            .origin(UPDATED_ORIGIN)
            .type(UPDATED_TYPE)
            .erpCode(UPDATED_ERP_CODE)
            .crop(UPDATED_CROP)
            .revenueModel(UPDATED_REVENUE_MODEL)
            .month(UPDATED_MONTH)
            .operationalYear(UPDATED_OPERATIONAL_YEAR)
            .percentage(UPDATED_PERCENTAGE)
            .value(UPDATED_VALUE)
            .creationDate(UPDATED_CREATION_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .status(UPDATED_STATUS)
            .vendor(UPDATED_VENDOR)
            .poNumber(UPDATED_PO_NUMBER);
        return feeList;
    }

    @BeforeEach
    public void initTest() {
        feeList = createEntity(em);
    }

    @Test
    @Transactional
    void createFeeList() throws Exception {
        int databaseSizeBeforeCreate = feeListRepository.findAll().size();
        // Create the FeeList
        restFeeListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feeList)))
            .andExpect(status().isCreated());

        // Validate the FeeList in the database
        List<FeeList> feeListList = feeListRepository.findAll();
        assertThat(feeListList).hasSize(databaseSizeBeforeCreate + 1);
        FeeList testFeeList = feeListList.get(feeListList.size() - 1);
        assertThat(testFeeList.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
        assertThat(testFeeList.getDocumentNumber()).isEqualTo(DEFAULT_DOCUMENT_NUMBER);
        assertThat(testFeeList.getStateRegistration()).isEqualTo(DEFAULT_STATE_REGISTRATION);
        assertThat(testFeeList.getOrigin()).isEqualTo(DEFAULT_ORIGIN);
        assertThat(testFeeList.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFeeList.getErpCode()).isEqualTo(DEFAULT_ERP_CODE);
        assertThat(testFeeList.getCrop()).isEqualTo(DEFAULT_CROP);
        assertThat(testFeeList.getRevenueModel()).isEqualTo(DEFAULT_REVENUE_MODEL);
        assertThat(testFeeList.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testFeeList.getOperationalYear()).isEqualTo(DEFAULT_OPERATIONAL_YEAR);
        assertThat(testFeeList.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
        assertThat(testFeeList.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testFeeList.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testFeeList.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testFeeList.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFeeList.getVendor()).isEqualTo(DEFAULT_VENDOR);
        assertThat(testFeeList.getPoNumber()).isEqualTo(DEFAULT_PO_NUMBER);
    }

    @Test
    @Transactional
    void createFeeListWithExistingId() throws Exception {
        // Create the FeeList with an existing ID
        feeList.setId(1L);

        int databaseSizeBeforeCreate = feeListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeeListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feeList)))
            .andExpect(status().isBadRequest());

        // Validate the FeeList in the database
        List<FeeList> feeListList = feeListRepository.findAll();
        assertThat(feeListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFeeLists() throws Exception {
        // Initialize the database
        feeListRepository.saveAndFlush(feeList);

        // Get all the feeListList
        restFeeListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feeList.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE)))
            .andExpect(jsonPath("$.[*].documentNumber").value(hasItem(DEFAULT_DOCUMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].stateRegistration").value(hasItem(DEFAULT_STATE_REGISTRATION)))
            .andExpect(jsonPath("$.[*].origin").value(hasItem(DEFAULT_ORIGIN.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].erpCode").value(hasItem(DEFAULT_ERP_CODE)))
            .andExpect(jsonPath("$.[*].crop").value(hasItem(DEFAULT_CROP)))
            .andExpect(jsonPath("$.[*].revenueModel").value(hasItem(DEFAULT_REVENUE_MODEL)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].operationalYear").value(hasItem(DEFAULT_OPERATIONAL_YEAR)))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR)))
            .andExpect(jsonPath("$.[*].poNumber").value(hasItem(DEFAULT_PO_NUMBER)));
    }

    @Test
    @Transactional
    void getFeeList() throws Exception {
        // Initialize the database
        feeListRepository.saveAndFlush(feeList);

        // Get the feeList
        restFeeListMockMvc
            .perform(get(ENTITY_API_URL_ID, feeList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feeList.getId().intValue()))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE))
            .andExpect(jsonPath("$.documentNumber").value(DEFAULT_DOCUMENT_NUMBER))
            .andExpect(jsonPath("$.stateRegistration").value(DEFAULT_STATE_REGISTRATION))
            .andExpect(jsonPath("$.origin").value(DEFAULT_ORIGIN.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.erpCode").value(DEFAULT_ERP_CODE))
            .andExpect(jsonPath("$.crop").value(DEFAULT_CROP))
            .andExpect(jsonPath("$.revenueModel").value(DEFAULT_REVENUE_MODEL))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH))
            .andExpect(jsonPath("$.operationalYear").value(DEFAULT_OPERATIONAL_YEAR))
            .andExpect(jsonPath("$.percentage").value(DEFAULT_PERCENTAGE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.vendor").value(DEFAULT_VENDOR))
            .andExpect(jsonPath("$.poNumber").value(DEFAULT_PO_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingFeeList() throws Exception {
        // Get the feeList
        restFeeListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeeList() throws Exception {
        // Initialize the database
        feeListRepository.saveAndFlush(feeList);

        int databaseSizeBeforeUpdate = feeListRepository.findAll().size();

        // Update the feeList
        FeeList updatedFeeList = feeListRepository.findById(feeList.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeeList are not directly saved in db
        em.detach(updatedFeeList);
        updatedFeeList
            .documentType(UPDATED_DOCUMENT_TYPE)
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .stateRegistration(UPDATED_STATE_REGISTRATION)
            .origin(UPDATED_ORIGIN)
            .type(UPDATED_TYPE)
            .erpCode(UPDATED_ERP_CODE)
            .crop(UPDATED_CROP)
            .revenueModel(UPDATED_REVENUE_MODEL)
            .month(UPDATED_MONTH)
            .operationalYear(UPDATED_OPERATIONAL_YEAR)
            .percentage(UPDATED_PERCENTAGE)
            .value(UPDATED_VALUE)
            .creationDate(UPDATED_CREATION_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .status(UPDATED_STATUS)
            .vendor(UPDATED_VENDOR)
            .poNumber(UPDATED_PO_NUMBER);

        restFeeListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFeeList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFeeList))
            )
            .andExpect(status().isOk());

        // Validate the FeeList in the database
        List<FeeList> feeListList = feeListRepository.findAll();
        assertThat(feeListList).hasSize(databaseSizeBeforeUpdate);
        FeeList testFeeList = feeListList.get(feeListList.size() - 1);
        assertThat(testFeeList.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testFeeList.getDocumentNumber()).isEqualTo(UPDATED_DOCUMENT_NUMBER);
        assertThat(testFeeList.getStateRegistration()).isEqualTo(UPDATED_STATE_REGISTRATION);
        assertThat(testFeeList.getOrigin()).isEqualTo(UPDATED_ORIGIN);
        assertThat(testFeeList.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFeeList.getErpCode()).isEqualTo(UPDATED_ERP_CODE);
        assertThat(testFeeList.getCrop()).isEqualTo(UPDATED_CROP);
        assertThat(testFeeList.getRevenueModel()).isEqualTo(UPDATED_REVENUE_MODEL);
        assertThat(testFeeList.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testFeeList.getOperationalYear()).isEqualTo(UPDATED_OPERATIONAL_YEAR);
        assertThat(testFeeList.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
        assertThat(testFeeList.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testFeeList.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFeeList.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testFeeList.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFeeList.getVendor()).isEqualTo(UPDATED_VENDOR);
        assertThat(testFeeList.getPoNumber()).isEqualTo(UPDATED_PO_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingFeeList() throws Exception {
        int databaseSizeBeforeUpdate = feeListRepository.findAll().size();
        feeList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeeListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feeList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feeList))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeeList in the database
        List<FeeList> feeListList = feeListRepository.findAll();
        assertThat(feeListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeeList() throws Exception {
        int databaseSizeBeforeUpdate = feeListRepository.findAll().size();
        feeList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feeList))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeeList in the database
        List<FeeList> feeListList = feeListRepository.findAll();
        assertThat(feeListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeeList() throws Exception {
        int databaseSizeBeforeUpdate = feeListRepository.findAll().size();
        feeList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feeList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeeList in the database
        List<FeeList> feeListList = feeListRepository.findAll();
        assertThat(feeListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeeListWithPatch() throws Exception {
        // Initialize the database
        feeListRepository.saveAndFlush(feeList);

        int databaseSizeBeforeUpdate = feeListRepository.findAll().size();

        // Update the feeList using partial update
        FeeList partialUpdatedFeeList = new FeeList();
        partialUpdatedFeeList.setId(feeList.getId());

        partialUpdatedFeeList.documentNumber(UPDATED_DOCUMENT_NUMBER).origin(UPDATED_ORIGIN).crop(UPDATED_CROP).month(UPDATED_MONTH);

        restFeeListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeeList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeeList))
            )
            .andExpect(status().isOk());

        // Validate the FeeList in the database
        List<FeeList> feeListList = feeListRepository.findAll();
        assertThat(feeListList).hasSize(databaseSizeBeforeUpdate);
        FeeList testFeeList = feeListList.get(feeListList.size() - 1);
        assertThat(testFeeList.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
        assertThat(testFeeList.getDocumentNumber()).isEqualTo(UPDATED_DOCUMENT_NUMBER);
        assertThat(testFeeList.getStateRegistration()).isEqualTo(DEFAULT_STATE_REGISTRATION);
        assertThat(testFeeList.getOrigin()).isEqualTo(UPDATED_ORIGIN);
        assertThat(testFeeList.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFeeList.getErpCode()).isEqualTo(DEFAULT_ERP_CODE);
        assertThat(testFeeList.getCrop()).isEqualTo(UPDATED_CROP);
        assertThat(testFeeList.getRevenueModel()).isEqualTo(DEFAULT_REVENUE_MODEL);
        assertThat(testFeeList.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testFeeList.getOperationalYear()).isEqualTo(DEFAULT_OPERATIONAL_YEAR);
        assertThat(testFeeList.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
        assertThat(testFeeList.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testFeeList.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testFeeList.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testFeeList.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFeeList.getVendor()).isEqualTo(DEFAULT_VENDOR);
        assertThat(testFeeList.getPoNumber()).isEqualTo(DEFAULT_PO_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateFeeListWithPatch() throws Exception {
        // Initialize the database
        feeListRepository.saveAndFlush(feeList);

        int databaseSizeBeforeUpdate = feeListRepository.findAll().size();

        // Update the feeList using partial update
        FeeList partialUpdatedFeeList = new FeeList();
        partialUpdatedFeeList.setId(feeList.getId());

        partialUpdatedFeeList
            .documentType(UPDATED_DOCUMENT_TYPE)
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .stateRegistration(UPDATED_STATE_REGISTRATION)
            .origin(UPDATED_ORIGIN)
            .type(UPDATED_TYPE)
            .erpCode(UPDATED_ERP_CODE)
            .crop(UPDATED_CROP)
            .revenueModel(UPDATED_REVENUE_MODEL)
            .month(UPDATED_MONTH)
            .operationalYear(UPDATED_OPERATIONAL_YEAR)
            .percentage(UPDATED_PERCENTAGE)
            .value(UPDATED_VALUE)
            .creationDate(UPDATED_CREATION_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .status(UPDATED_STATUS)
            .vendor(UPDATED_VENDOR)
            .poNumber(UPDATED_PO_NUMBER);

        restFeeListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeeList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeeList))
            )
            .andExpect(status().isOk());

        // Validate the FeeList in the database
        List<FeeList> feeListList = feeListRepository.findAll();
        assertThat(feeListList).hasSize(databaseSizeBeforeUpdate);
        FeeList testFeeList = feeListList.get(feeListList.size() - 1);
        assertThat(testFeeList.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testFeeList.getDocumentNumber()).isEqualTo(UPDATED_DOCUMENT_NUMBER);
        assertThat(testFeeList.getStateRegistration()).isEqualTo(UPDATED_STATE_REGISTRATION);
        assertThat(testFeeList.getOrigin()).isEqualTo(UPDATED_ORIGIN);
        assertThat(testFeeList.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFeeList.getErpCode()).isEqualTo(UPDATED_ERP_CODE);
        assertThat(testFeeList.getCrop()).isEqualTo(UPDATED_CROP);
        assertThat(testFeeList.getRevenueModel()).isEqualTo(UPDATED_REVENUE_MODEL);
        assertThat(testFeeList.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testFeeList.getOperationalYear()).isEqualTo(UPDATED_OPERATIONAL_YEAR);
        assertThat(testFeeList.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
        assertThat(testFeeList.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testFeeList.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFeeList.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testFeeList.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFeeList.getVendor()).isEqualTo(UPDATED_VENDOR);
        assertThat(testFeeList.getPoNumber()).isEqualTo(UPDATED_PO_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingFeeList() throws Exception {
        int databaseSizeBeforeUpdate = feeListRepository.findAll().size();
        feeList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeeListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feeList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feeList))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeeList in the database
        List<FeeList> feeListList = feeListRepository.findAll();
        assertThat(feeListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeeList() throws Exception {
        int databaseSizeBeforeUpdate = feeListRepository.findAll().size();
        feeList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feeList))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeeList in the database
        List<FeeList> feeListList = feeListRepository.findAll();
        assertThat(feeListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeeList() throws Exception {
        int databaseSizeBeforeUpdate = feeListRepository.findAll().size();
        feeList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeListMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(feeList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeeList in the database
        List<FeeList> feeListList = feeListRepository.findAll();
        assertThat(feeListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeeList() throws Exception {
        // Initialize the database
        feeListRepository.saveAndFlush(feeList);

        int databaseSizeBeforeDelete = feeListRepository.findAll().size();

        // Delete the feeList
        restFeeListMockMvc
            .perform(delete(ENTITY_API_URL_ID, feeList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FeeList> feeListList = feeListRepository.findAll();
        assertThat(feeListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
