package com.github.nylle.javafixture.generic;

import com.github.nylle.javafixture.ReflectionHelper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class FixtureType<T> extends TypeCapture<T> {

    private final Type type;

    protected FixtureType() {
        this.type = capture();
    }

    private FixtureType(final Type type) {
        this.type = type;
    }

    public static <T> FixtureType<T> fromClass(final Type typeReference) {
        return new FixtureType<>(typeReference);
    }

    public static <T> FixtureType<T> fromRawType(final Class<?> rawType, final Type[] actualTypeArguments) {
        return new FixtureType<>(TypeCapture.create(rawType, actualTypeArguments));
    }

    public Class<T> asClass() {
        return (Class<T>) ReflectionHelper.castToClass(type);
    }

    public ParameterizedType asParameterizedType() {
        if (isParameterized()) {
            return (ParameterizedType) type;
        }
        throw new FixtureTypeException(format("%s is not a ParameterizedType", type));
    }

    public Type[] getGenericTypeArguments() {
        if (isParameterized()) {
            return ((ParameterizedType) type).getActualTypeArguments();
        }
        throw new FixtureTypeException(format("%s is not a ParameterizedType", type));
    }

    public Type getGenericTypeArgument(final int index) {
        return getGenericTypeArguments()[index];
    }

    public String getTypeParameterName(final int index) {
        return getTypeParameterNames()[index];
    }

    public String[] getTypeParameterNames() {
        if (isParameterized()) {
            return stream(asClass().getTypeParameters()).map(x -> x.getName()).toArray(size -> new String[size]);
        }
        throw new FixtureTypeException(format("%s is not a ParameterizedType", type));
    }

    public Class<?> getComponentType() {
        if(isArray()) {
            return asClass().getComponentType();
        }
        throw new FixtureTypeException(format("%s is not an array", type));
    }

    public T[] getEnumConstants() {
        if(isEnum()) {
            return asClass().getEnumConstants();
        }
        throw new FixtureTypeException(format("%s is not an enum", type));
    }

    public boolean isParameterized() {
        return ReflectionHelper.isParameterizedType(type);
    }

    public boolean isCollection() {
        return ReflectionHelper.isCollection(this.asClass());
    }

    public boolean isMap() {
        return ReflectionHelper.isMap(this.asClass());
    }

    public boolean isTimeType() {
        return ReflectionHelper.isTimeType(this.asClass());
    }

    public boolean isPrimitive() {
        return this.asClass().isPrimitive();
    }

    public boolean isBoxed() {
        return this.asClass() == Double.class || this.asClass() == Float.class || this.asClass() == Long.class
                || this.asClass() == Integer.class || this.asClass() == Short.class || this.asClass() == Character.class
                || this.asClass() == Byte.class || this.asClass() == Boolean.class;
    }

    public boolean isEnum() {
        return this.asClass().isEnum();
    }

    public boolean isArray() {
        return this.asClass().isArray();
    }

    public boolean isInterface() {
        return this.asClass().isInterface();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final FixtureType that = (FixtureType) o;

        if(isParameterized() != that.isParameterized()) {
            return false;
        }

        if(isParameterized() && that.isParameterized()) {

            if(getGenericTypeArguments().length != that.getGenericTypeArguments().length) {
                return false;
            }

            if(!IntStream.range(0, getGenericTypeArguments().length).boxed().allMatch(i -> Objects.equals(getGenericTypeArgument(i), that.getGenericTypeArgument(i)))) {
                return false;
            }

        }

        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return isParameterized() ? Objects.hash(type, stream(getGenericTypeArguments()).map(x -> x.getTypeName()).collect(toList())) : Objects.hash(type);
    }

}
