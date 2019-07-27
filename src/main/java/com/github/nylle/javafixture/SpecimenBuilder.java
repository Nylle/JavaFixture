package com.github.nylle.javafixture;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SpecimenBuilder<T> {
    private final Class<T> typeReference;
    private final Configuration configuration;

    private final List<Consumer<T>> functions = new LinkedList<>();
    private final List<String> ignoredFields = new LinkedList<>();
    private final Map<String, Object> customFields = new HashMap<>();

    public SpecimenBuilder(final Class<T> typeReference, final Configuration configuration) {
        this.typeReference = typeReference;
        this.configuration = configuration;
    }

    public T create() {
        return customize(new SpecimenFactory(new Context(configuration)).build(typeReference).create());
    }

    public Stream<T> createMany() {
        return IntStream.range(0, configuration.getStreamSize()).boxed().map(x -> customize(new SpecimenFactory(new Context(configuration)).build(typeReference).create()));
    }

    public Stream<T> createMany(int size) {
        return IntStream.range(0, size).boxed().map(x -> customize(new SpecimenFactory(new Context(configuration)).build(typeReference).create()));
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
        customFields.forEach((fieldName, value) -> ReflectionHelper.setField(fieldName, instance, value));
        ignoredFields.forEach(field -> ReflectionHelper.unsetField(field, instance));
        functions.forEach(f -> f.accept(instance));
        return instance;
    }
}

