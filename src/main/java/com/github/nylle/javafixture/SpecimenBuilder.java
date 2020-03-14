package com.github.nylle.javafixture;

import com.github.nylle.javafixture.generic.FixtureType;

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

    private final FixtureType<T> type;
    private final Configuration configuration;

    public SpecimenBuilder(final FixtureType<T> type, final Configuration configuration) {
        this.type = type;
        this.configuration = configuration;
    }

    @Override
    public T create() {
        return customize(new SpecimenFactory(new Context(configuration)).build(type).create(new CustomizationContext(ignoredFields, customFields)));
    }

    @Override
    public Stream<T> createMany() {
        return createMany(configuration.getStreamSize());
    }

    @Override
    public Stream<T> createMany(final int size) {
        return IntStream.range(0, size).boxed().map(x -> customize(create()));
    }

    @Override
    public ISpecimenBuilder<T> with(final Consumer<T> function) {
        functions.add(function);
        return this;
    }

    @Override
    public ISpecimenBuilder<T> with(final String fieldName, Object value) {
        customFields.put(fieldName, value);
        return this;
    }

    @Override
    public ISpecimenBuilder<T> without(final String fieldName) {
        ignoredFields.add(fieldName);
        return this;
    }

    private T customize(T instance) {
        functions.forEach(f -> f.accept(instance));
        return instance;
    }

}

