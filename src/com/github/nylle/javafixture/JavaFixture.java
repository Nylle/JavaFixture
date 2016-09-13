package com.github.nylle.javafixture;

public class JavaFixture {
  public <T> SpecimenBuilder<T> create(Class<T> testDtoClass) {
    return new SpecimenBuilder<>(testDtoClass);
  }
}
