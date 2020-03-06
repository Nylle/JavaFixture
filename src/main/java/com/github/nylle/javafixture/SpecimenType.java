package com.github.nylle.javafixture;

import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;

public class SpecimenType {

    private final Class<?> type;
    private final List<Class<?>> genericTypes;

    private SpecimenType(final Class<?> type, final Class<?>... genericTypes) {
        this.type = type;
        this.genericTypes = asList(genericTypes);
    }

    public static SpecimenType forObject(final Class<?> type) {
        return new SpecimenType(type);
    }

    public static SpecimenType forCollection(final Class<?> type, final Class<?> T) {
        return new SpecimenType(type, T);
    }

    public static SpecimenType forMap(final Class<?> type, final Class<?> K, final Class<?> V) {
        return new SpecimenType(type, K, V);
    }

    public static SpecimenType forGeneric(final Class<?> type, final Class<?>... genericTypes) {
        return new SpecimenType(type, genericTypes);
    }

    public Class<?> getType() {
        return type;
    }

    public List<Class<?>> getGenericTypes() {
        return genericTypes;
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

        if(genericTypes.size() != that.genericTypes.size()) {
            return false;
        }

        //TODO: Do we care about the order? Maybe genericTypes could be a Set?
        boolean allGenericTypesAreEqual = true;
        for(int i = 0; i < genericTypes.size(); i++) {
            allGenericTypesAreEqual &= Objects.equals(genericTypes.get(i), that.genericTypes.get(i));
        }

        return Objects.equals(type, that.type) && allGenericTypesAreEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, genericTypes);
    }
}
