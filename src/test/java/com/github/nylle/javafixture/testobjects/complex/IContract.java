package com.github.nylle.javafixture.testobjects.complex;

import java.util.Set;


public interface IContract {
  Set<ContractPosition> getContractPositions();

  void addContractPosition(ContractPosition contractPosition);
}
