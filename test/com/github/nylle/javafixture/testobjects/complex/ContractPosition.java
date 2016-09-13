package com.github.nylle.javafixture.testobjects.complex;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.ReadablePeriod;


public class ContractPosition {
  private Long id;
  private String companyWideId;
  private int quantity = 1;
  private LocalDate startDate;
  private Period remainingPeriod;
  private boolean billingIsPeriodic;
  private Contract contract;

  public ContractPosition(int quantity, final LocalDate startDate,
                          final ReadablePeriod remainingPeriod,
                          final boolean billingIsPeriodic) {
    this.quantity = quantity;
    this.startDate = startDate;
    this.remainingPeriod = remainingPeriod.toPeriod();
    this.billingIsPeriodic = billingIsPeriodic;
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

  public Contract getContract() {
    return contract;
  }

  public void setContract(Contract contract) {
    this.contract = contract;
  }

}
