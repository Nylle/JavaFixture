package com.github.nylle.javafixture;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface ISpecimenBuilder<T> {
    T create();

    Optional<T> createOptional();

    Stream<T> createMany();

    Stream<T> createMany(int size);

    ISpecimenBuilder<T> with(Consumer<T> function);

    ISpecimenBuilder<T> with(String fieldName, Object value);

    <U> ISpecimenBuilder<T> with(Class<U> type, U value);

    <U> ISpecimenBuilder<T> with(SpecimenType<U> type, U value);

    ISpecimenBuilder<T> without(String fieldName);

    <U> ISpecimenBuilder<T> without(Class<U> fieldName);
}

