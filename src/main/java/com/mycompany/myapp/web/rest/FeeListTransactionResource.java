package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.FeeListTransaction;
import com.mycompany.myapp.repository.FeeListTransactionRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.FeeListTransaction}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FeeListTransactionResource {

    private final Logger log = LoggerFactory.getLogger(FeeListTransactionResource.class);

    private static final String ENTITY_NAME = "sobeAquiJHipsterFeeListTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeeListTransactionRepository feeListTransactionRepository;

    public FeeListTransactionResource(FeeListTransactionRepository feeListTransactionRepository) {
        this.feeListTransactionRepository = feeListTransactionRepository;
    }

    /**
     * {@code POST  /fee-list-transactions} : Create a new feeListTransaction.
     *
     * @param feeListTransaction the feeListTransaction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feeListTransaction, or with status {@code 400 (Bad Request)} if the feeListTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fee-list-transactions")
    public ResponseEntity<FeeListTransaction> createFeeListTransaction(@RequestBody FeeListTransaction feeListTransaction)
        throws URISyntaxException {
        log.debug("REST request to save FeeListTransaction : {}", feeListTransaction);
        if (feeListTransaction.getId() != null) {
            throw new BadRequestAlertException("A new feeListTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FeeListTransaction result = feeListTransactionRepository.save(feeListTransaction);
        return ResponseEntity
            .created(new URI("/api/fee-list-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fee-list-transactions/:id} : Updates an existing feeListTransaction.
     *
     * @param id the id of the feeListTransaction to save.
     * @param feeListTransaction the feeListTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feeListTransaction,
     * or with status {@code 400 (Bad Request)} if the feeListTransaction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feeListTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fee-list-transactions/{id}")
    public ResponseEntity<FeeListTransaction> updateFeeListTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeeListTransaction feeListTransaction
    ) throws URISyntaxException {
        log.debug("REST request to update FeeListTransaction : {}, {}", id, feeListTransaction);
        if (feeListTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feeListTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feeListTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FeeListTransaction result = feeListTransactionRepository.save(feeListTransaction);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, feeListTransaction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fee-list-transactions/:id} : Partial updates given fields of an existing feeListTransaction, field will ignore if it is null
     *
     * @param id the id of the feeListTransaction to save.
     * @param feeListTransaction the feeListTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feeListTransaction,
     * or with status {@code 400 (Bad Request)} if the feeListTransaction is not valid,
     * or with status {@code 404 (Not Found)} if the feeListTransaction is not found,
     * or with status {@code 500 (Internal Server Error)} if the feeListTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fee-list-transactions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeeListTransaction> partialUpdateFeeListTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeeListTransaction feeListTransaction
    ) throws URISyntaxException {
        log.debug("REST request to partial update FeeListTransaction partially : {}, {}", id, feeListTransaction);
        if (feeListTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feeListTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feeListTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeeListTransaction> result = feeListTransactionRepository
            .findById(feeListTransaction.getId())
            .map(existingFeeListTransaction -> {
                if (feeListTransaction.getCreationDate() != null) {
                    existingFeeListTransaction.setCreationDate(feeListTransaction.getCreationDate());
                }
                if (feeListTransaction.getDeletionDate() != null) {
                    existingFeeListTransaction.setDeletionDate(feeListTransaction.getDeletionDate());
                }

                return existingFeeListTransaction;
            })
            .map(feeListTransactionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, feeListTransaction.getId().toString())
        );
    }

    /**
     * {@code GET  /fee-list-transactions} : get all the feeListTransactions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feeListTransactions in body.
     */
    @GetMapping("/fee-list-transactions")
    public List<FeeListTransaction> getAllFeeListTransactions() {
        log.debug("REST request to get all FeeListTransactions");
        return feeListTransactionRepository.findAll();
    }

    /**
     * {@code GET  /fee-list-transactions/:id} : get the "id" feeListTransaction.
     *
     * @param id the id of the feeListTransaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feeListTransaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fee-list-transactions/{id}")
    public ResponseEntity<FeeListTransaction> getFeeListTransaction(@PathVariable Long id) {
        log.debug("REST request to get FeeListTransaction : {}", id);
        Optional<FeeListTransaction> feeListTransaction = feeListTransactionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(feeListTransaction);
    }

    /**
     * {@code DELETE  /fee-list-transactions/:id} : delete the "id" feeListTransaction.
     *
     * @param id the id of the feeListTransaction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fee-list-transactions/{id}")
    public ResponseEntity<Void> deleteFeeListTransaction(@PathVariable Long id) {
        log.debug("REST request to delete FeeListTransaction : {}", id);
        feeListTransactionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
