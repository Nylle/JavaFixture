package com.github.nylle.javafixture;

public class JavaFixture {
  public <T> T create(Class<T> typeReference) {
    SpecimenBuilder<T> specimenBuilder = new SpecimenBuilder<>(typeReference);
    return specimenBuilder.create();
  }

  public <T> SpecimenBuilder<T> build(Class<T> typeReference) {
    return new SpecimenBuilder<>(typeReference);
  }
}
