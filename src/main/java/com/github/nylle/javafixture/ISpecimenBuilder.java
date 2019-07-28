package com.github.nylle.javafixture;

import java.util.function.Consumer;
import java.util.stream.Stream;

public interface ISpecimenBuilder<T> {
    T create();

    Stream<T> createMany();

    Stream<T> createMany(int size);

    ISpecimenBuilder<T> with(Consumer<T> function);

    ISpecimenBuilder<T> with(String fieldName, Object value);

    ISpecimenBuilder<T> without(String fieldName);
}

