package com.github.nylle.javafixture;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SpecimenBuilder<T> extends AbstractSpecimenBuilder<T> implements ISpecimenBuilder<T> {
    private final Class<T> typeReference;
    private final Configuration configuration;


    public SpecimenBuilder(final Class<T> typeReference, final Configuration configuration) {
        this.typeReference = typeReference;
        this.configuration = configuration;
    }

    @Override
    public T create() {
        return customize(new SpecimenFactory(new Context(configuration)).build(typeReference).create());
    }

    @Override
    public Stream<T> createMany() {
        return IntStream.range(0, configuration.getStreamSize()).boxed().map(x -> customize(new SpecimenFactory(new Context(configuration)).build(typeReference).create()));
    }

    @Override
    public Stream<T> createMany(int size) {
        return IntStream.range(0, size).boxed().map(x -> customize(new SpecimenFactory(new Context(configuration)).build(typeReference).create()));
    }
}

