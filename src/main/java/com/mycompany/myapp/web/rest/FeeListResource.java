package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.FeeList;
import com.mycompany.myapp.repository.FeeListRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.FeeList}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FeeListResource {

    private final Logger log = LoggerFactory.getLogger(FeeListResource.class);

    private static final String ENTITY_NAME = "sobeAquiJHipsterFeeList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeeListRepository feeListRepository;

    public FeeListResource(FeeListRepository feeListRepository) {
        this.feeListRepository = feeListRepository;
    }

    /**
     * {@code POST  /fee-lists} : Create a new feeList.
     *
     * @param feeList the feeList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feeList, or with status {@code 400 (Bad Request)} if the feeList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fee-lists")
    public ResponseEntity<FeeList> createFeeList(@RequestBody FeeList feeList) throws URISyntaxException {
        log.debug("REST request to save FeeList : {}", feeList);
        if (feeList.getId() != null) {
            throw new BadRequestAlertException("A new feeList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FeeList result = feeListRepository.save(feeList);
        return ResponseEntity
            .created(new URI("/api/fee-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fee-lists/:id} : Updates an existing feeList.
     *
     * @param id the id of the feeList to save.
     * @param feeList the feeList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feeList,
     * or with status {@code 400 (Bad Request)} if the feeList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feeList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fee-lists/{id}")
    public ResponseEntity<FeeList> updateFeeList(@PathVariable(value = "id", required = false) final Long id, @RequestBody FeeList feeList)
        throws URISyntaxException {
        log.debug("REST request to update FeeList : {}, {}", id, feeList);
        if (feeList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feeList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feeListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FeeList result = feeListRepository.save(feeList);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, feeList.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fee-lists/:id} : Partial updates given fields of an existing feeList, field will ignore if it is null
     *
     * @param id the id of the feeList to save.
     * @param feeList the feeList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feeList,
     * or with status {@code 400 (Bad Request)} if the feeList is not valid,
     * or with status {@code 404 (Not Found)} if the feeList is not found,
     * or with status {@code 500 (Internal Server Error)} if the feeList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fee-lists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeeList> partialUpdateFeeList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeeList feeList
    ) throws URISyntaxException {
        log.debug("REST request to partial update FeeList partially : {}, {}", id, feeList);
        if (feeList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feeList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feeListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeeList> result = feeListRepository
            .findById(feeList.getId())
            .map(existingFeeList -> {
                if (feeList.getDocumentType() != null) {
                    existingFeeList.setDocumentType(feeList.getDocumentType());
                }
                if (feeList.getDocumentNumber() != null) {
                    existingFeeList.setDocumentNumber(feeList.getDocumentNumber());
                }
                if (feeList.getStateRegistration() != null) {
                    existingFeeList.setStateRegistration(feeList.getStateRegistration());
                }
                if (feeList.getOrigin() != null) {
                    existingFeeList.setOrigin(feeList.getOrigin());
                }
                if (feeList.getType() != null) {
                    existingFeeList.setType(feeList.getType());
                }
                if (feeList.getErpCode() != null) {
                    existingFeeList.setErpCode(feeList.getErpCode());
                }
                if (feeList.getCrop() != null) {
                    existingFeeList.setCrop(feeList.getCrop());
                }
                if (feeList.getRevenueModel() != null) {
                    existingFeeList.setRevenueModel(feeList.getRevenueModel());
                }
                if (feeList.getMonth() != null) {
                    existingFeeList.setMonth(feeList.getMonth());
                }
                if (feeList.getOperationalYear() != null) {
                    existingFeeList.setOperationalYear(feeList.getOperationalYear());
                }
                if (feeList.getPercentage() != null) {
                    existingFeeList.setPercentage(feeList.getPercentage());
                }
                if (feeList.getValue() != null) {
                    existingFeeList.setValue(feeList.getValue());
                }
                if (feeList.getCreationDate() != null) {
                    existingFeeList.setCreationDate(feeList.getCreationDate());
                }
                if (feeList.getUpdateDate() != null) {
                    existingFeeList.setUpdateDate(feeList.getUpdateDate());
                }
                if (feeList.getStatus() != null) {
                    existingFeeList.setStatus(feeList.getStatus());
                }
                if (feeList.getVendor() != null) {
                    existingFeeList.setVendor(feeList.getVendor());
                }
                if (feeList.getPoNumber() != null) {
                    existingFeeList.setPoNumber(feeList.getPoNumber());
                }

                return existingFeeList;
            })
            .map(feeListRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, feeList.getId().toString())
        );
    }

    /**
     * {@code GET  /fee-lists} : get all the feeLists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feeLists in body.
     */
    @GetMapping("/fee-lists")
    public List<FeeList> getAllFeeLists() {
        log.debug("REST request to get all FeeLists");
        return feeListRepository.findAll();
    }

    /**
     * {@code GET  /fee-lists/:id} : get the "id" feeList.
     *
     * @param id the id of the feeList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feeList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fee-lists/{id}")
    public ResponseEntity<FeeList> getFeeList(@PathVariable Long id) {
        log.debug("REST request to get FeeList : {}", id);
        Optional<FeeList> feeList = feeListRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(feeList);
    }

    /**
     * {@code DELETE  /fee-lists/:id} : delete the "id" feeList.
     *
     * @param id the id of the feeList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fee-lists/{id}")
    public ResponseEntity<Void> deleteFeeList(@PathVariable Long id) {
        log.debug("REST request to delete FeeList : {}", id);
        feeListRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
