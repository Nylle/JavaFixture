package com.github.nylle.javafixture;

import java.lang.reflect.Type;
import java.util.Objects;

public class SpecimenType {

    private final Type type;
    private final Type genericType;

    public SpecimenType(final Type type, final Type genericType) {
        this.type = type;
        this.genericType = genericType;
    }

    public Type getType() {
        return type;
    }

    public Type getGenericType() {
        return genericType;
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
        return Objects.equals(type, that.type) && Objects.equals(genericType, that.genericType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, genericType);
    }
}
