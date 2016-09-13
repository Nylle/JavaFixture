package com.github.nylle.javafixture.testobjects.complex;

import org.joda.time.LocalDateTime;


public class AccountManager {
  private Long id;
  private Long accountManagerNumber;
  private String description;
  private LocalDateTime creationDate;
  private String createdByUser;
  private LocalDateTime modificationDate;
  private String modifiedByUser;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getAccountManagerNumber() {
    return accountManagerNumber;
  }

  public void setAccountManagerNumber(Long accountManagerNumber) {
    this.accountManagerNumber = accountManagerNumber;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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
}
