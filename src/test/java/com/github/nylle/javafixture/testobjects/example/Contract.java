package com.github.nylle.javafixture.testobjects.example;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;


public class Contract implements IContract {
    private Long id;
    private String companyWideId;
    private long contractNumber;
    private AccountManager[] accountManagers;
    private int lastAssignedNumber = 0;
    private Set<ContractPosition> contractPositions = new TreeSet<>();
    private ContractPosition baseContractPosition;
    private LocalDateTime creationDate;
    private boolean isConsistencyChecked;
    private ContractCategory category;

    public Contract(Long id, long contractNumber) {
        this.id = id;
        this.contractNumber = contractNumber;
        this.companyWideId = "009." + UUID.randomUUID().toString();
        this.creationDate = LocalDateTime.now();
        this.isConsistencyChecked = false;
        this.category = ContractCategory.STANDARD;
    }

    protected Contract() {
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

    public long getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(long contractNumber) {
        this.contractNumber = contractNumber;
    }

    public AccountManager[] getAccountManagers() {
        return accountManagers;
    }

    public void setAccountManagers(AccountManager[] accountManagers) {
        this.accountManagers = accountManagers;
    }

    public int getLastAssignedNumber() {
        return lastAssignedNumber;
    }

    public void setLastAssignedNumber(int lastAssignedNumber) {
        this.lastAssignedNumber = lastAssignedNumber;
    }

    @Override
    public Set<ContractPosition> getContractPositions() {
        return contractPositions;
    }

    public void setContractPositions(Set<ContractPosition> contractPositions) {
        this.contractPositions = contractPositions;
    }

    public ContractPosition getBaseContractPosition() {
        return baseContractPosition;
    }

    public void setBaseContractPosition(ContractPosition baseContractPosition) {
        this.baseContractPosition = baseContractPosition;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isConsistencyChecked() {
        return isConsistencyChecked;
    }

    public void setConsistencyChecked(boolean consistencyChecked) {
        isConsistencyChecked = consistencyChecked;
    }

    public ContractCategory getCategory() {
        return category;
    }

    public void setCategory(ContractCategory category) {
        this.category = category;
    }

    @Override
    public void addContractPosition(ContractPosition contractPosition) {
        contractPositions.add(contractPosition);
    }
}
