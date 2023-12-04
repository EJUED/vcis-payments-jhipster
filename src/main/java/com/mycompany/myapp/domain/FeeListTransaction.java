package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A FeeListTransaction.
 */
@Entity
@Table(name = "fee_list_transaction")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeeListTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "deletion_date")
    private LocalDate deletionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "feeListTransactions" }, allowSetters = true)
    private FeeList feeList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "feeListTransactions", "invoices", "transactionHistories" }, allowSetters = true)
    private Transaction transaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FeeListTransaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public FeeListTransaction creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getDeletionDate() {
        return this.deletionDate;
    }

    public FeeListTransaction deletionDate(LocalDate deletionDate) {
        this.setDeletionDate(deletionDate);
        return this;
    }

    public void setDeletionDate(LocalDate deletionDate) {
        this.deletionDate = deletionDate;
    }

    public FeeList getFeeList() {
        return this.feeList;
    }

    public void setFeeList(FeeList feeList) {
        this.feeList = feeList;
    }

    public FeeListTransaction feeList(FeeList feeList) {
        this.setFeeList(feeList);
        return this;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public FeeListTransaction transaction(Transaction transaction) {
        this.setTransaction(transaction);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeeListTransaction)) {
            return false;
        }
        return id != null && id.equals(((FeeListTransaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeeListTransaction{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", deletionDate='" + getDeletionDate() + "'" +
            "}";
    }
}
