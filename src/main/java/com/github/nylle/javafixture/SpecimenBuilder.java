package com.github.nylle.javafixture;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class SpecimenBuilder<T> {
    private static final int DEFAULT_COLLECTION_SIZE = 3;
    private final Class<T> typeReference;
    private final Reflector<T> reflector;
    private List<Consumer<T>> functions;
    private List<String> ignoredFields;
    private Map<String, Object> customFields;
    private Randomizer randomizer = new Randomizer();

    public SpecimenBuilder(Class<T> typeReference) {
        this.typeReference = typeReference;
        reflector = new Reflector<>(typeReference);
        functions = new LinkedList<>();
        ignoredFields = new LinkedList<>();
        customFields = new HashMap<>();
    }

    public T create() {
        T instance = randomizer.random(typeReference);
        customize(instance);
        return instance;
    }

    public Stream<T> createMany() {
        return IntStream.range(0, DEFAULT_COLLECTION_SIZE).boxed().map(x -> create());
    }

    public Stream<T> createMany(int size) {
        return IntStream.range(0, size).boxed().map(x -> create());
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
        ignoredFields.forEach(field -> reflector.unsetField(instance, field));
        functions.forEach(f -> f.accept(instance));
        return instance;
    }
}

