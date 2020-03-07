package com.github.nylle.javafixture;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SpecimenBuilder<T> implements ISpecimenBuilder<T> {
    private final List<Consumer<T>> functions = new LinkedList<>();
    private final List<String> ignoredFields = new LinkedList<>();
    private final Map<String, Object> customFields = new HashMap<>();

    private final Class<T> typeReference;
    private final Configuration configuration;


    public SpecimenBuilder(final Class<T> typeReference, final Configuration configuration) {
        this.typeReference = typeReference;
        this.configuration = configuration;
    }

    @Override
    public T create() {
        return customize(new SpecimenFactory(new Context(configuration)).build(typeReference).create(new CustomizationContext(ignoredFields, customFields)));
    }

    @Override
    public Stream<T> createMany() {
        return createMany(configuration.getStreamSize());
    }

    @Override
    public Stream<T> createMany(int size) {
        return IntStream.range(0, size).boxed().map(x -> customize(new SpecimenFactory(new Context(configuration)).build(typeReference).create(new CustomizationContext(ignoredFields, customFields))));
    }

    @Override
    public ISpecimenBuilder<T> with(Consumer<T> function) {
        functions.add(function);
        return this;
    }

    @Override
    public ISpecimenBuilder<T> with(String fieldName, Object value) {
        customFields.put(fieldName, value);
        return this;
    }

    @Override
    public ISpecimenBuilder<T> without(String fieldName) {
        ignoredFields.add(fieldName);
        return this;
    }

    protected T customize(T instance) {
        functions.forEach(f -> f.accept(instance));
        return instance;
    }

}

