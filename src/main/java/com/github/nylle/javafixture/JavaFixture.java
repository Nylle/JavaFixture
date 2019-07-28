package com.github.nylle.javafixture;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class JavaFixture {

    private final SpecimenBuilderProvider provider;

    public JavaFixture() {
        this(new Configuration());
    }

    public JavaFixture(Configuration configuration) {
        this.provider = new SpecimenBuilderProvider(configuration);
    }

    public <T> T create(Class<T> typeReference) {
        var specimenBuilder = provider.newBuilder(typeReference);
        return specimenBuilder.create();
    }

    public <T> Stream<T> createMany(Class<T> typeReference) {
        var specimenBuilder = provider.newBuilder(typeReference);
        return specimenBuilder.createMany();
    }

    public <T> ISpecimenBuilder<T> build(Class<T> typeReference) {
        return provider.newBuilder(typeReference);
    }

    public <T> void addManyTo(Collection<T> result, Class<T> typeReference) {
        var specimenBuilder = provider.newBuilder(typeReference);
        result.addAll(specimenBuilder.createMany().collect(Collectors.toList()));
    }
}
