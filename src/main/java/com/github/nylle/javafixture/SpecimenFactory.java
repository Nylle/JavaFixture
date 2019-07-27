package com.github.nylle.javafixture;

import java.lang.reflect.Type;

public interface SpecimenFactory {
    <T> Specimen<T> build(Class<T> type);

    <T> Specimen<T> build(Class<T> type, Type genericType);
}
