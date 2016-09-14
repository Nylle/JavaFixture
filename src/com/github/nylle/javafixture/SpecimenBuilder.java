package com.github.nylle.javafixture;

import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.function.Consumer;
import java.util.stream.Stream;


public class SpecimenBuilder<T> {
  private final Class<T> typeReference;
  private final T instance;
  private final Reflector<T> reflector;

  public SpecimenBuilder(Class<?> typeReference) {
    this.typeReference = (Class<T>) typeReference;
    this.instance = (T) EnhancedRandom.random(typeReference);
    this.reflector = new Reflector<T>(this.typeReference, this.instance);
  }

  public T create() {
    return instance;
  }

  public Stream<T> createMany(int size) {
    return EnhancedRandom.randomStreamOf(size, typeReference);
  }

  public SpecimenBuilder<T> with(Consumer<T> f) {
    f.accept(instance);
    return this;
  }

  public SpecimenBuilder<T> with(String fieldName, Object value) {
    reflector.setField(fieldName, value);
    return this;
  }

  public SpecimenBuilder<T> without(String fieldName) {
    reflector.setField(fieldName, null);
    return this;
  }
}
