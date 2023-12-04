package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.HistoryOriginEnum;
import com.mycompany.myapp.domain.enumeration.HistoryTypeEnum;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A FeeList.
 */
@Entity
@Table(name = "fee_list")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeeList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "state_registration")
    private String stateRegistration;

    @Enumerated(EnumType.STRING)
    @Column(name = "origin")
    private HistoryOriginEnum origin;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private HistoryTypeEnum type;

    @Column(name = "erp_code")
    private String erpCode;

    @Column(name = "crop")
    private String crop;

    @Column(name = "revenue_model")
    private String revenueModel;

    @Column(name = "month")
    private Integer month;

    @Column(name = "operational_year")
    private Integer operationalYear;

    @Column(name = "percentage")
    private String percentage;

    @Column(name = "value")
    private Double value;

    @Column(name = "creation_date")
    private String creationDate;

    @Column(name = "update_date")
    private String updateDate;

    @Column(name = "status")
    private String status;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "po_number")
    private String poNumber;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "feeList")
    @JsonIgnoreProperties(value = { "feeList", "transaction" }, allowSetters = true)
    private Set<FeeListTransaction> feeListTransactions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FeeList id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentType() {
        return this.documentType;
    }

    public FeeList documentType(String documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return this.documentNumber;
    }

    public FeeList documentNumber(String documentNumber) {
        this.setDocumentNumber(documentNumber);
        return this;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getStateRegistration() {
        return this.stateRegistration;
    }

    public FeeList stateRegistration(String stateRegistration) {
        this.setStateRegistration(stateRegistration);
        return this;
    }

    public void setStateRegistration(String stateRegistration) {
        this.stateRegistration = stateRegistration;
    }

    public HistoryOriginEnum getOrigin() {
        return this.origin;
    }

    public FeeList origin(HistoryOriginEnum origin) {
        this.setOrigin(origin);
        return this;
    }

    public void setOrigin(HistoryOriginEnum origin) {
        this.origin = origin;
    }

    public HistoryTypeEnum getType() {
        return this.type;
    }

    public FeeList type(HistoryTypeEnum type) {
        this.setType(type);
        return this;
    }

    public void setType(HistoryTypeEnum type) {
        this.type = type;
    }

    public String getErpCode() {
        return this.erpCode;
    }

    public FeeList erpCode(String erpCode) {
        this.setErpCode(erpCode);
        return this;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    public String getCrop() {
        return this.crop;
    }

    public FeeList crop(String crop) {
        this.setCrop(crop);
        return this;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getRevenueModel() {
        return this.revenueModel;
    }

    public FeeList revenueModel(String revenueModel) {
        this.setRevenueModel(revenueModel);
        return this;
    }

    public void setRevenueModel(String revenueModel) {
        this.revenueModel = revenueModel;
    }

    public Integer getMonth() {
        return this.month;
    }

    public FeeList month(Integer month) {
        this.setMonth(month);
        return this;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getOperationalYear() {
        return this.operationalYear;
    }

    public FeeList operationalYear(Integer operationalYear) {
        this.setOperationalYear(operationalYear);
        return this;
    }

    public void setOperationalYear(Integer operationalYear) {
        this.operationalYear = operationalYear;
    }

    public String getPercentage() {
        return this.percentage;
    }

    public FeeList percentage(String percentage) {
        this.setPercentage(percentage);
        return this;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public Double getValue() {
        return this.value;
    }

    public FeeList value(Double value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public FeeList creationDate(String creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUpdateDate() {
        return this.updateDate;
    }

    public FeeList updateDate(String updateDate) {
        this.setUpdateDate(updateDate);
        return this;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getStatus() {
        return this.status;
    }

    public FeeList status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVendor() {
        return this.vendor;
    }

    public FeeList vendor(String vendor) {
        this.setVendor(vendor);
        return this;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getPoNumber() {
        return this.poNumber;
    }

    public FeeList poNumber(String poNumber) {
        this.setPoNumber(poNumber);
        return this;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public Set<FeeListTransaction> getFeeListTransactions() {
        return this.feeListTransactions;
    }

    public void setFeeListTransactions(Set<FeeListTransaction> feeListTransactions) {
        if (this.feeListTransactions != null) {
            this.feeListTransactions.forEach(i -> i.setFeeList(null));
        }
        if (feeListTransactions != null) {
            feeListTransactions.forEach(i -> i.setFeeList(this));
        }
        this.feeListTransactions = feeListTransactions;
    }

    public FeeList feeListTransactions(Set<FeeListTransaction> feeListTransactions) {
        this.setFeeListTransactions(feeListTransactions);
        return this;
    }

    public FeeList addFeeListTransaction(FeeListTransaction feeListTransaction) {
        this.feeListTransactions.add(feeListTransaction);
        feeListTransaction.setFeeList(this);
        return this;
    }

    public FeeList removeFeeListTransaction(FeeListTransaction feeListTransaction) {
        this.feeListTransactions.remove(feeListTransaction);
        feeListTransaction.setFeeList(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeeList)) {
            return false;
        }
        return id != null && id.equals(((FeeList) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeeList{" +
            "id=" + getId() +
            ", documentType='" + getDocumentType() + "'" +
            ", documentNumber='" + getDocumentNumber() + "'" +
            ", stateRegistration='" + getStateRegistration() + "'" +
            ", origin='" + getOrigin() + "'" +
            ", type='" + getType() + "'" +
            ", erpCode='" + getErpCode() + "'" +
            ", crop='" + getCrop() + "'" +
            ", revenueModel='" + getRevenueModel() + "'" +
            ", month=" + getMonth() +
            ", operationalYear=" + getOperationalYear() +
            ", percentage='" + getPercentage() + "'" +
            ", value=" + getValue() +
            ", creationDate='" + getCreationDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", vendor='" + getVendor() + "'" +
            ", poNumber='" + getPoNumber() + "'" +
            "}";
    }
}
