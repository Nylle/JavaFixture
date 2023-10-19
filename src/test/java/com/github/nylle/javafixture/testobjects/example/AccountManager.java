package com.github.nylle.javafixture.testobjects.example;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountManager {
    private Long id;
    private String description;
    private LocalDateTime creationDate;
    private BigDecimal salary;
    private AccountManager[] otherAccountManagers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getSalary(){
        return salary;
    }
    public void setSalary(BigDecimal salary){
        this.salary = salary;
    }

    public AccountManager[] getOtherAccountManagers() {
        return otherAccountManagers;
    }

    public void setOtherAccountManagers(AccountManager[] otherAccountManagers) {
        this.otherAccountManagers = otherAccountManagers;
    }
}
