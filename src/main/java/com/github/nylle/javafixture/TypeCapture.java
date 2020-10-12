package com.github.nylle.javafixture;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

import static java.lang.String.format;

abstract class TypeCapture<T> {
    static ParameterizedType create(final Class<?> rawType, final Type[] actualTypeArguments) {
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

        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return actualTypeArguments;
            }

            @Override
            public Type getRawType() {
                return rawType;
            }

            @Override
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
        };
    }

    final Type capture() {
        Type superclass = getClass().getGenericSuperclass();

        if (!(superclass instanceof ParameterizedType)) {
            throw new IllegalArgumentException(format("%s doesn't seem to have been instantiated with generic type arguments", superclass));
        }

        return ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }
}
