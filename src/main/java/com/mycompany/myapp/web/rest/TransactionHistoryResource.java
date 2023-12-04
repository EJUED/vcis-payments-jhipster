package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TransactionHistory;
import com.mycompany.myapp.repository.TransactionHistoryRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TransactionHistory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TransactionHistoryResource {

    private final Logger log = LoggerFactory.getLogger(TransactionHistoryResource.class);

    private static final String ENTITY_NAME = "sobeAquiJHipsterTransactionHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionHistoryRepository transactionHistoryRepository;

    public TransactionHistoryResource(TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    /**
     * {@code POST  /transaction-histories} : Create a new transactionHistory.
     *
     * @param transactionHistory the transactionHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionHistory, or with status {@code 400 (Bad Request)} if the transactionHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transaction-histories")
    public ResponseEntity<TransactionHistory> createTransactionHistory(@RequestBody TransactionHistory transactionHistory)
        throws URISyntaxException {
        log.debug("REST request to save TransactionHistory : {}", transactionHistory);
        if (transactionHistory.getId() != null) {
            throw new BadRequestAlertException("A new transactionHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionHistory result = transactionHistoryRepository.save(transactionHistory);
        return ResponseEntity
            .created(new URI("/api/transaction-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transaction-histories/:id} : Updates an existing transactionHistory.
     *
     * @param id the id of the transactionHistory to save.
     * @param transactionHistory the transactionHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionHistory,
     * or with status {@code 400 (Bad Request)} if the transactionHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transaction-histories/{id}")
    public ResponseEntity<TransactionHistory> updateTransactionHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransactionHistory transactionHistory
    ) throws URISyntaxException {
        log.debug("REST request to update TransactionHistory : {}, {}", id, transactionHistory);
        if (transactionHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransactionHistory result = transactionHistoryRepository.save(transactionHistory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transactionHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transaction-histories/:id} : Partial updates given fields of an existing transactionHistory, field will ignore if it is null
     *
     * @param id the id of the transactionHistory to save.
     * @param transactionHistory the transactionHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionHistory,
     * or with status {@code 400 (Bad Request)} if the transactionHistory is not valid,
     * or with status {@code 404 (Not Found)} if the transactionHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transaction-histories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionHistory> partialUpdateTransactionHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransactionHistory transactionHistory
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransactionHistory partially : {}, {}", id, transactionHistory);
        if (transactionHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionHistory> result = transactionHistoryRepository
            .findById(transactionHistory.getId())
            .map(existingTransactionHistory -> {
                if (transactionHistory.getUser() != null) {
                    existingTransactionHistory.setUser(transactionHistory.getUser());
                }
                if (transactionHistory.getDescription() != null) {
                    existingTransactionHistory.setDescription(transactionHistory.getDescription());
                }
                if (transactionHistory.getCreationDate() != null) {
                    existingTransactionHistory.setCreationDate(transactionHistory.getCreationDate());
                }

                return existingTransactionHistory;
            })
            .map(transactionHistoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transactionHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /transaction-histories} : get all the transactionHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionHistories in body.
     */
    @GetMapping("/transaction-histories")
    public List<TransactionHistory> getAllTransactionHistories() {
        log.debug("REST request to get all TransactionHistories");
        return transactionHistoryRepository.findAll();
    }

    /**
     * {@code GET  /transaction-histories/:id} : get the "id" transactionHistory.
     *
     * @param id the id of the transactionHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaction-histories/{id}")
    public ResponseEntity<TransactionHistory> getTransactionHistory(@PathVariable Long id) {
        log.debug("REST request to get TransactionHistory : {}", id);
        Optional<TransactionHistory> transactionHistory = transactionHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(transactionHistory);
    }

    /**
     * {@code DELETE  /transaction-histories/:id} : delete the "id" transactionHistory.
     *
     * @param id the id of the transactionHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transaction-histories/{id}")
    public ResponseEntity<Void> deleteTransactionHistory(@PathVariable Long id) {
        log.debug("REST request to delete TransactionHistory : {}", id);
        transactionHistoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
