package com.github.nylle.javafixture;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class JavaFixture {

  private JavaFixture() {
  }

  public static  <T> T create(Class<T> typeReference) {
    SpecimenBuilder<T> specimenBuilder = new SpecimenBuilder<>(typeReference);
    return specimenBuilder.create();
  }

  public static  <T> Stream<T> createMany(Class<T> typeReference) {
    SpecimenBuilder<T> specimenBuilder = new SpecimenBuilder<>(typeReference);
    return specimenBuilder.createMany();
  }

  public static <T> SpecimenBuilder<T> build(Class<T> typeReference) {
    return new SpecimenBuilder<>(typeReference);
  }

  public static <T> void addManyTo(Collection<T> result, Class<T> typeReference) {
    SpecimenBuilder<T> specimenBuilder = new SpecimenBuilder<>(typeReference);
    result.addAll(specimenBuilder.createMany().collect(Collectors.toList()));
  }
}
