package com.github.nylle.javafixture.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

import static java.lang.String.format;

public class OpenParameterizedType implements ParameterizedType {
    private final Type[] actualTypeArguments;
    private final Class<?> rawType;

    public OpenParameterizedType(final Class<?> rawType, final Type[] actualTypeArguments) {
        if (rawType == null) {
            throw new IllegalArgumentException("rawType: null");
        }

        if (actualTypeArguments == null) {
            throw new IllegalArgumentException("actualTypeArguments: null");
        }

        if (rawType.getTypeParameters().length != actualTypeArguments.length) {
            throw new IllegalArgumentException(format("actualTypeArguments do not match rawType type parameters (%d): %d",
                    rawType.getTypeParameters().length,
                    actualTypeArguments.length));
        }

        this.actualTypeArguments = actualTypeArguments;
        this.rawType = rawType;
    }

    public Type[] getActualTypeArguments() {
        return actualTypeArguments.clone();
    }

    public Class<?> getRawType() {
        return rawType;
    }

    public Type getOwnerType() {
        return rawType.getDeclaringClass();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) {
            return false;
        }

        ParameterizedType that = (ParameterizedType) o;

        if (this == that) {
            return true;
        }

        return Objects.equals(rawType.getDeclaringClass(), that.getOwnerType())
                && Objects.equals(rawType, that.getRawType())
                && Arrays.equals(actualTypeArguments, that.getActualTypeArguments());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(actualTypeArguments) ^ Objects.hashCode(rawType.getDeclaringClass()) ^ Objects.hashCode(rawType);
    }
}
