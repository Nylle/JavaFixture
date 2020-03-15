package com.github.nylle.javafixture;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Fixture {

    private final Configuration configuration;

    public Fixture() {
        this(new Configuration());
    }

    public Fixture(Configuration configuration) {
        this.configuration = configuration;
    }

    public static Fixture fixture() {
        return new Fixture();
    }

    public <T> T create(final Class<T> type) {
        return new SpecimenBuilder<T>(SpecimenType.fromClass(type), configuration).create();
    }

    public <T> T create(final SpecimenType<T> type) {
        return new SpecimenBuilder<T>(type, configuration).create();
    }

    public <T> Stream<T> createMany(final Class<T> type) {
        return new SpecimenBuilder<T>(SpecimenType.fromClass(type), configuration).createMany();
    }

    public <T> Stream<T> createMany(final SpecimenType<T> type) {
        return new SpecimenBuilder<T>(type, configuration).createMany();
    }

    public <T> ISpecimenBuilder<T> build(final Class<T> type) {
        return new SpecimenBuilder<T>(SpecimenType.fromClass(type), configuration);
    }

    public <T> ISpecimenBuilder<T> build(final SpecimenType<T> type) {
        return new SpecimenBuilder<>(type, configuration);
    }

    public <T> void addManyTo(Collection<T> result, final Class<T> type) {
        result.addAll(new SpecimenBuilder<T>(SpecimenType.fromClass(type), configuration).createMany().collect(Collectors.toList()));
    }

    public <T> void addManyTo(Collection<T> result, final SpecimenType<T> type) {
        result.addAll(new SpecimenBuilder<T>(type, configuration).createMany().collect(Collectors.toList()));
    }
}
