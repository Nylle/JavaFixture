package com.github.nylle.javafixture;

import java.util.function.Consumer;
import java.util.stream.Stream;

public interface ISpecimenBuilder<T> {
    T create();

    Stream<T> createMany();

    Stream<T> createMany(int size);

    SpecimenBuilder<T> with(Consumer<T> function);

    SpecimenBuilder<T> with(String fieldName, Object value);

    SpecimenBuilder<T> without(String fieldName);
}

