package com.github.nylle.javafixture;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class JavaFixture {
    public <T> T create(Class<T> typeReference) {
        SpecimenBuilder<T> specimenBuilder = new SpecimenBuilder<>(typeReference);
        return specimenBuilder.create();
    }

    public <T> Stream<T> createMany(Class<T> typeReference) {
        SpecimenBuilder<T> specimenBuilder = new SpecimenBuilder<>(typeReference);
        return specimenBuilder.createMany();
    }

    public <T> SpecimenBuilder<T> build(Class<T> typeReference) {
        return new SpecimenBuilder<>(typeReference);
    }

    public <T> void addManyTo(Collection<T> result, Class<T> typeReference) {
        SpecimenBuilder<T> specimenBuilder = new SpecimenBuilder<>(typeReference);
        result.addAll(specimenBuilder.createMany().collect(Collectors.toList()));
    }
}
