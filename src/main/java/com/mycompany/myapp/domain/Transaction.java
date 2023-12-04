package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.TransactionsStatus;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "payment_forecast")
    private Double paymentForecast;

    @Column(name = "our_number")
    private String ourNumber;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private TransactionsStatus transactionStatus;

    @Column(name = "observation")
    private String observation;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "deletion_date")
    private LocalDate deletionDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transaction")
    @JsonIgnoreProperties(value = { "feeList", "transaction" }, allowSetters = true)
    private Set<FeeListTransaction> feeListTransactions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transaction")
    @JsonIgnoreProperties(value = { "transaction" }, allowSetters = true)
    private Set<Invoice> invoices = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transaction")
    @JsonIgnoreProperties(value = { "transaction" }, allowSetters = true)
    private Set<TransactionHistory> transactionHistories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPaymentForecast() {
        return this.paymentForecast;
    }

    public Transaction paymentForecast(Double paymentForecast) {
        this.setPaymentForecast(paymentForecast);
        return this;
    }

    public void setPaymentForecast(Double paymentForecast) {
        this.paymentForecast = paymentForecast;
    }

    public String getOurNumber() {
        return this.ourNumber;
    }

    public Transaction ourNumber(String ourNumber) {
        this.setOurNumber(ourNumber);
        return this;
    }

    public void setOurNumber(String ourNumber) {
        this.ourNumber = ourNumber;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public Transaction dueDate(LocalDate dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public TransactionsStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public Transaction transactionStatus(TransactionsStatus transactionStatus) {
        this.setTransactionStatus(transactionStatus);
        return this;
    }

    public void setTransactionStatus(TransactionsStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getObservation() {
        return this.observation;
    }

    public Transaction observation(String observation) {
        this.setObservation(observation);
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public Transaction creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getDeletionDate() {
        return this.deletionDate;
    }

    public Transaction deletionDate(LocalDate deletionDate) {
        this.setDeletionDate(deletionDate);
        return this;
    }

    public void setDeletionDate(LocalDate deletionDate) {
        this.deletionDate = deletionDate;
    }

    public Set<FeeListTransaction> getFeeListTransactions() {
        return this.feeListTransactions;
    }

    public void setFeeListTransactions(Set<FeeListTransaction> feeListTransactions) {
        if (this.feeListTransactions != null) {
            this.feeListTransactions.forEach(i -> i.setTransaction(null));
        }
        if (feeListTransactions != null) {
            feeListTransactions.forEach(i -> i.setTransaction(this));
        }
        this.feeListTransactions = feeListTransactions;
    }

    public Transaction feeListTransactions(Set<FeeListTransaction> feeListTransactions) {
        this.setFeeListTransactions(feeListTransactions);
        return this;
    }

    public Transaction addFeeListTransaction(FeeListTransaction feeListTransaction) {
        this.feeListTransactions.add(feeListTransaction);
        feeListTransaction.setTransaction(this);
        return this;
    }

    public Transaction removeFeeListTransaction(FeeListTransaction feeListTransaction) {
        this.feeListTransactions.remove(feeListTransaction);
        feeListTransaction.setTransaction(null);
        return this;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        if (this.invoices != null) {
            this.invoices.forEach(i -> i.setTransaction(null));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.setTransaction(this));
        }
        this.invoices = invoices;
    }

    public Transaction invoices(Set<Invoice> invoices) {
        this.setInvoices(invoices);
        return this;
    }

    public Transaction addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setTransaction(this);
        return this;
    }

    public Transaction removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setTransaction(null);
        return this;
    }

    public Set<TransactionHistory> getTransactionHistories() {
        return this.transactionHistories;
    }

    public void setTransactionHistories(Set<TransactionHistory> transactionHistories) {
        if (this.transactionHistories != null) {
            this.transactionHistories.forEach(i -> i.setTransaction(null));
        }
        if (transactionHistories != null) {
            transactionHistories.forEach(i -> i.setTransaction(this));
        }
        this.transactionHistories = transactionHistories;
    }

    public Transaction transactionHistories(Set<TransactionHistory> transactionHistories) {
        this.setTransactionHistories(transactionHistories);
        return this;
    }

    public Transaction addTransactionHistory(TransactionHistory transactionHistory) {
        this.transactionHistories.add(transactionHistory);
        transactionHistory.setTransaction(this);
        return this;
    }

    public Transaction removeTransactionHistory(TransactionHistory transactionHistory) {
        this.transactionHistories.remove(transactionHistory);
        transactionHistory.setTransaction(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return id != null && id.equals(((Transaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", paymentForecast=" + getPaymentForecast() +
            ", ourNumber='" + getOurNumber() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", transactionStatus='" + getTransactionStatus() + "'" +
            ", observation='" + getObservation() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", deletionDate='" + getDeletionDate() + "'" +
            "}";
    }
}
