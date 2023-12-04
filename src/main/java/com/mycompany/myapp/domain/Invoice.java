package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.InvoicesStatusEnum;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "s_3_url")
    private String s3Url;

    @Column(name = "value")
    private Double value;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_status")
    private InvoicesStatusEnum invoiceStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "feeListTransactions", "invoices", "transactionHistories" }, allowSetters = true)
    private Transaction transaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Invoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String gets3Url() {
        return this.s3Url;
    }

    public Invoice s3Url(String s3Url) {
        this.sets3Url(s3Url);
        return this;
    }

    public void sets3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public Double getValue() {
        return this.value;
    }

    public Invoice value(Double value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public Invoice creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public InvoicesStatusEnum getInvoiceStatus() {
        return this.invoiceStatus;
    }

    public Invoice invoiceStatus(InvoicesStatusEnum invoiceStatus) {
        this.setInvoiceStatus(invoiceStatus);
        return this;
    }

    public void setInvoiceStatus(InvoicesStatusEnum invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Invoice transaction(Transaction transaction) {
        this.setTransaction(transaction);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", s3Url='" + gets3Url() + "'" +
            ", value=" + getValue() +
            ", creationDate='" + getCreationDate() + "'" +
            ", invoiceStatus='" + getInvoiceStatus() + "'" +
            "}";
    }
}
