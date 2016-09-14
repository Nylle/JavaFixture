package com.github.nylle.javafixture;

import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;


public class SpecimenBuilder<T> {
  private static int DEFAULT_COLLECTION_SIZE = 3;
  private final Class<T> typeReference;
  private final Reflector<T> reflector;
  private List<Consumer<T>> functions;
  private List<String> ignoredFields;
  private Map<String, Object> customFields;

  public SpecimenBuilder(Class<?> typeReference) {
    this.typeReference = (Class<T>) typeReference;
    reflector = new Reflector<T>(this.typeReference);
    functions = new LinkedList<>();
    ignoredFields = new LinkedList<>();
    customFields = new HashMap<>();
  }

  public T create() {
    T instance = EnhancedRandom.random(typeReference);
    customize(instance);
    return instance;
  }

  public Stream<T> createMany() {
    return EnhancedRandom.randomStreamOf(DEFAULT_COLLECTION_SIZE, typeReference).map(x -> customize(x));
  }

  public Stream<T> createMany(int size) {
    return EnhancedRandom.randomStreamOf(size, typeReference).map(x -> customize(x));
  }

  public SpecimenBuilder<T> with(Consumer<T> function) {
    functions.add(function);
    return this;
  }

  public SpecimenBuilder<T> with(String fieldName, Object value) {
    customFields.put(fieldName, value);
    return this;
  }

  public SpecimenBuilder<T> without(String fieldName) {
    ignoredFields.add(fieldName);
    return this;
  }

  private T customize(T instance) {
    customFields.entrySet().forEach(kvp -> reflector.setField(instance, kvp.getKey(), kvp.getValue()));
    ignoredFields.forEach(x -> reflector.setField(instance, x, null));
    functions.forEach(x -> x.accept(instance));
    return instance;
  }
}
