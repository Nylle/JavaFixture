package com.github.nylle.javafixture;

import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.function.Consumer;


public class SpecimenBuilder<T> {
  private Class<T> typeReference;
  private T instance;

  public SpecimenBuilder(Class<?> typeReference) {
    this.typeReference = (Class<T>) typeReference;
    this.instance = (T) EnhancedRandom.random(typeReference);
  }

  public T build() {
    return instance;
  }

  public SpecimenBuilder<T> with(Consumer<T> f) {
    f.accept(instance);
    return this;
  }
}
