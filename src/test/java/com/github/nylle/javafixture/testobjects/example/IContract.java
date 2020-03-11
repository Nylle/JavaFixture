package com.github.nylle.javafixture.testobjects.example;

import java.util.Set;


public interface IContract {
    Set<ContractPosition> getContractPositions();

    void addContractPosition(ContractPosition contractPosition);
}
