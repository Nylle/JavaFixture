package com.github.nylle.javafixture.testobjects.complex;

import org.joda.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;


public class Contract {
  private Long id;
  private String companyWideId;
  private long contractNumber;
  private String customerCwid;
  private String invoiceText;
  private String externalReference;
  private AccountManager accountManager;
  private int lastAssignedNumber = 0;
  private String shoppingCartCwid;
  private Set<ContractPosition> contractPositions = new TreeSet<>();
  private ContractPosition baseContractPosition;
  private LocalDateTime creationDate;
  private String createdByUser;
  private LocalDateTime modificationDate;
  private String modifiedByUser;
  private boolean isConsistencyChecked;
  private boolean schemaChecked;
  private Long version;
  private Long lockingVersion;
  private ContractCategory category;
  private String parentReference;

  public Contract(Long id, long contractNumber, String customerCwid, String createdByUser,
                  String shoppingCartCwid) {
    this.id = id;
    this.customerCwid = customerCwid;
    this.createdByUser = createdByUser;
    this.contractNumber = contractNumber;
    this.shoppingCartCwid = shoppingCartCwid;
    this.companyWideId = "009." + UUID.randomUUID().toString();
    this.creationDate = new LocalDateTime();
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

  public String getCustomerCwid() {
    return customerCwid;
  }

  public void setCustomerCwid(String customerCwid) {
    this.customerCwid = customerCwid;
  }

  public String getInvoiceText() {
    return invoiceText;
  }

  public void setInvoiceText(String invoiceText) {
    this.invoiceText = invoiceText;
  }

  public String getExternalReference() {
    return externalReference;
  }

  public void setExternalReference(String externalReference) {
    this.externalReference = externalReference;
  }

  public AccountManager getAccountManager() {
    return accountManager;
  }

  public void setAccountManager(AccountManager accountManager) {
    this.accountManager = accountManager;
  }

  public int getLastAssignedNumber() {
    return lastAssignedNumber;
  }

  public void setLastAssignedNumber(int lastAssignedNumber) {
    this.lastAssignedNumber = lastAssignedNumber;
  }

  public String getShoppingCartCwid() {
    return shoppingCartCwid;
  }

  public void setShoppingCartCwid(String shoppingCartCwid) {
    this.shoppingCartCwid = shoppingCartCwid;
  }

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

  public boolean isConsistencyChecked() {
    return isConsistencyChecked;
  }

  public void setConsistencyChecked(boolean consistencyChecked) {
    isConsistencyChecked = consistencyChecked;
  }

  public boolean isSchemaChecked() {
    return schemaChecked;
  }

  public void setSchemaChecked(boolean schemaChecked) {
    this.schemaChecked = schemaChecked;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Long getLockingVersion() {
    return lockingVersion;
  }

  public void setLockingVersion(Long lockingVersion) {
    this.lockingVersion = lockingVersion;
  }

  public ContractCategory getCategory() {
    return category;
  }

  public void setCategory(ContractCategory category) {
    this.category = category;
  }

  public String getParentReference() {
    return parentReference;
  }

  public void setParentReference(String parentReference) {
    this.parentReference = parentReference;
  }
}
