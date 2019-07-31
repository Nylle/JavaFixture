package com.github.nylle.javafixture;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class JavaFixture {

    private final Configuration configuration;

    public JavaFixture() {
        this(new Configuration());
    }

    public JavaFixture(Configuration configuration) {
        this.configuration = configuration;
    }

    public <T> T create(Class<T> typeReference) {
        var specimenBuilder = new SpecimenBuilder<>(typeReference, configuration);
        return specimenBuilder.create();
    }

    public <T> Stream<T> createMany(Class<T> typeReference) {
        var specimenBuilder = new SpecimenBuilder<>(typeReference, configuration);
        return specimenBuilder.createMany();
    }

    public <T> ISpecimenBuilder<T> build(Class<T> typeReference) {
        return new SpecimenBuilder<>(typeReference, configuration);
    }

    public <T> void addManyTo(Collection<T> result, Class<T> typeReference) {
        var specimenBuilder = new SpecimenBuilder<>(typeReference, configuration);
        result.addAll(specimenBuilder.createMany().collect(Collectors.toList()));
    }
}
