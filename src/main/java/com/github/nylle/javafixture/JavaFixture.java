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

    public static JavaFixture fixture() {
        return new JavaFixture();
    }

    public <T> T create(final Class<T> typeReference) {
        return new SpecimenBuilder<>(typeReference, configuration).create();
    }

    public <T> Stream<T> createMany(final Class<T> typeReference) {
        return new SpecimenBuilder<>(typeReference, configuration).createMany();
    }

    public <T> ISpecimenBuilder<T> build(final Class<T> typeReference) {
        return new SpecimenBuilder<>(typeReference, configuration);
    }

    public <T> void addManyTo(Collection<T> result, final Class<T> typeReference) {
        result.addAll(new SpecimenBuilder<>(typeReference, configuration).createMany().collect(Collectors.toList()));
    }
}
