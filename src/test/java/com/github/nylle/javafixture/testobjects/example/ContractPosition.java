package com.github.nylle.javafixture.testobjects.example;


import java.io.File;
import java.time.LocalDate;
import java.time.Period;

public class ContractPosition {
    private Long id;
    private String companyWideId;
    private int quantity = 1;
    private LocalDate startDate;
    private File file;
    private Period remainingPeriod;
    private boolean billingIsPeriodic;
    private Contract contract;

    public ContractPosition(int quantity, final LocalDate startDate,
            File file, final Period remainingPeriod,
            final boolean billingIsPeriodic) {
        this.quantity = quantity;
        this.startDate = startDate;
        this.file = file;
        this.remainingPeriod = remainingPeriod;
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

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
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
