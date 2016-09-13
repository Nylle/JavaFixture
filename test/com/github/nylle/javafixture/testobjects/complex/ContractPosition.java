package com.github.nylle.javafixture.testobjects.complex;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.ReadablePeriod;
import java.math.BigDecimal;


public class ContractPosition {
  private Long id;
  private String companyWideId;
  private String externalReferenceCwid;
  private String priceListItemCwid;
  private BigDecimal priceAmount;
  private int quantity = 1;
  private LocalDate startDate;
  private LocalDate commencementDate;
  private LocalDate regularEndDate;
  private LocalDate endDate;
  private int contingentSize;
  private int contingentTolerance;
  private int consecutiveNumber;
  private Period remainingPeriod;
  private boolean billingIsPeriodic;
  private Period billingPeriod;
  private String invoiceText;
  private String forwardTrackingCode;
  private Contract contract;
  private LocalDateTime creationDate;
  private String createdByUser;
  private LocalDateTime modificationDate;
  private String modifiedByUser;
  private String payPalBillingAgreementId;
  private String payPalTransactionId;
  private Period furtherPeriod;
  Period cancellationPeriod;
  private String dependsOn;
  private String predecessor;
  private LocalDate cancellationReceptionDate;
  private String offerCwid;
  private String offerItemCwid;

  public ContractPosition(final String priceListItemCwid,
                          final BigDecimal priceAmount,
                          int quantity, final LocalDate startDate,
                          final LocalDate endDate,
                          final ReadablePeriod remainingPeriod,
                          final boolean billingIsPeriodic,
                          final String createdByUser,
                          final ReadablePeriod furtherPeriod,
                          final ReadablePeriod cancellationPeriod) {
    this.priceListItemCwid = priceListItemCwid;
    this.quantity = quantity;
    this.startDate = startDate;
    this.createdByUser = createdByUser;
    this.remainingPeriod = remainingPeriod.toPeriod();
    this.billingIsPeriodic = billingIsPeriodic;
    this.endDate = endDate;
    this.furtherPeriod = furtherPeriod.toPeriod();
    this.cancellationPeriod = cancellationPeriod.toPeriod();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCompanyWideId() {
    return companyWideId;
  }

  public void setCompanyWideId(String companyWideId) {
    this.companyWideId = companyWideId;
  }

  public String getExternalReferenceCwid() {
    return externalReferenceCwid;
  }

  public void setExternalReferenceCwid(String externalReferenceCwid) {
    this.externalReferenceCwid = externalReferenceCwid;
  }

  public String getPriceListItemCwid() {
    return priceListItemCwid;
  }

  public void setPriceListItemCwid(String priceListItemCwid) {
    this.priceListItemCwid = priceListItemCwid;
  }

  public BigDecimal getPriceAmount() {
    return priceAmount;
  }

  public void setPriceAmount(BigDecimal priceAmount) {
    this.priceAmount = priceAmount;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getCommencementDate() {
    return commencementDate;
  }

  public void setCommencementDate(LocalDate commencementDate) {
    this.commencementDate = commencementDate;
  }

  public LocalDate getRegularEndDate() {
    return regularEndDate;
  }

  public void setRegularEndDate(LocalDate regularEndDate) {
    this.regularEndDate = regularEndDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public int getContingentSize() {
    return contingentSize;
  }

  public void setContingentSize(int contingentSize) {
    this.contingentSize = contingentSize;
  }

  public int getContingentTolerance() {
    return contingentTolerance;
  }

  public void setContingentTolerance(int contingentTolerance) {
    this.contingentTolerance = contingentTolerance;
  }

  public int getConsecutiveNumber() {
    return consecutiveNumber;
  }

  public void setConsecutiveNumber(int consecutiveNumber) {
    this.consecutiveNumber = consecutiveNumber;
  }

  public Period getRemainingPeriod() {
    return remainingPeriod;
  }

  public void setRemainingPeriod(Period remainingPeriod) {
    this.remainingPeriod = remainingPeriod;
  }

  public boolean isBillingIsPeriodic() {
    return billingIsPeriodic;
  }

  public void setBillingIsPeriodic(boolean billingIsPeriodic) {
    this.billingIsPeriodic = billingIsPeriodic;
  }

  public Period getBillingPeriod() {
    return billingPeriod;
  }

  public void setBillingPeriod(Period billingPeriod) {
    this.billingPeriod = billingPeriod;
  }

  public String getInvoiceText() {
    return invoiceText;
  }

  public void setInvoiceText(String invoiceText) {
    this.invoiceText = invoiceText;
  }

  public String getForwardTrackingCode() {
    return forwardTrackingCode;
  }

  public void setForwardTrackingCode(String forwardTrackingCode) {
    this.forwardTrackingCode = forwardTrackingCode;
  }

  public Contract getContract() {
    return contract;
  }

  public void setContract(Contract contract) {
    this.contract = contract;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public String getCreatedByUser() {
    return createdByUser;
  }

  public void setCreatedByUser(String createdByUser) {
    this.createdByUser = createdByUser;
  }

  public LocalDateTime getModificationDate() {
    return modificationDate;
  }

  public void setModificationDate(LocalDateTime modificationDate) {
    this.modificationDate = modificationDate;
  }

  public String getModifiedByUser() {
    return modifiedByUser;
  }

  public void setModifiedByUser(String modifiedByUser) {
    this.modifiedByUser = modifiedByUser;
  }

  public String getPayPalBillingAgreementId() {
    return payPalBillingAgreementId;
  }

  public void setPayPalBillingAgreementId(String payPalBillingAgreementId) {
    this.payPalBillingAgreementId = payPalBillingAgreementId;
  }

  public String getPayPalTransactionId() {
    return payPalTransactionId;
  }

  public void setPayPalTransactionId(String payPalTransactionId) {
    this.payPalTransactionId = payPalTransactionId;
  }

  public Period getFurtherPeriod() {
    return furtherPeriod;
  }

  public void setFurtherPeriod(Period furtherPeriod) {
    this.furtherPeriod = furtherPeriod;
  }

  public Period getCancellationPeriod() {
    return cancellationPeriod;
  }

  public void setCancellationPeriod(Period cancellationPeriod) {
    this.cancellationPeriod = cancellationPeriod;
  }

  public String getDependsOn() {
    return dependsOn;
  }

  public void setDependsOn(String dependsOn) {
    this.dependsOn = dependsOn;
  }

  public String getPredecessor() {
    return predecessor;
  }

  public void setPredecessor(String predecessor) {
    this.predecessor = predecessor;
  }

  public LocalDate getCancellationReceptionDate() {
    return cancellationReceptionDate;
  }

  public void setCancellationReceptionDate(LocalDate cancellationReceptionDate) {
    this.cancellationReceptionDate = cancellationReceptionDate;
  }

  public String getOfferCwid() {
    return offerCwid;
  }

  public void setOfferCwid(String offerCwid) {
    this.offerCwid = offerCwid;
  }

  public String getOfferItemCwid() {
    return offerItemCwid;
  }

  public void setOfferItemCwid(String offerItemCwid) {
    this.offerItemCwid = offerItemCwid;
  }
}
