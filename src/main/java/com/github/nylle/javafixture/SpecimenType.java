package com.github.nylle.javafixture;

import java.util.Objects;

public class SpecimenType {

    private final Class<?> type;
    private final Class<?> genericType1;
    private final Class<?> genericType2;

    private SpecimenType() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    private SpecimenType(final Class<?> type, final Class<?> genericType1, final Class<?> genericType2) {
        this.type = type;
        this.genericType1 = genericType1;
        this.genericType2 = genericType2;
    }

    public static SpecimenType forObject(final Class<?> type) {
        return new SpecimenType(type, null, null);
    }

    public static SpecimenType forCollection(final Class<?> type, final Class<?> T) {
        return new SpecimenType(type, T, null);
    }

    public static SpecimenType forMap(final Class<?> type, final Class<?> K, final Class<?> V) {
        return new SpecimenType(type, K, V);
    }

    public Class<?> getType() {
        return type;
    }

    public Class<?> getGenericType() {
        return genericType1;
    }

    public Class<?> getKeyType() {
        return genericType1;
    }

    public Class<?> getValueType() {
        return genericType2;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final SpecimenType that = (SpecimenType) o;
        return Objects.equals(type, that.type)
                && Objects.equals(genericType1, that.genericType1)
                && Objects.equals(genericType2, that.genericType2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, genericType1);
    }
}
