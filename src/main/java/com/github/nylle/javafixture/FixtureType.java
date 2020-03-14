package com.github.nylle.javafixture;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class FixtureType<T> extends TypeCapture<T> {

    private final Type type;

    private FixtureType(final Type type) {
        this.type = type;
    }

    protected FixtureType() {
        this.type = capture();
    }

    public static <T> FixtureType<T> fromClass(final Type typeReference) {
        return new FixtureType<>(typeReference);
    }

    public static <T> FixtureType<T> fromRawType(final Class<?> rawType, final Type[] actualTypeArguments) {
        return new FixtureType<>(TypeCapture.create(rawType, actualTypeArguments));
    }

    public Class<T> asClass() {
        return (Class<T>) castToClass(type);
    }

    public ParameterizedType asParameterizedType() {
        if (isParameterized()) {
            return (ParameterizedType) type;
        }
        throw new FixtureTypeException(format("%s is not a ParameterizedType", type));
    }

    public T asInstance() {
        try {
            return asClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return (T) ((ObjectInstantiator) ((Objenesis) new ObjenesisStd()).getInstantiatorOf(asClass())).newInstance();
        }
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
        return isParameterized(type);
    }

    public boolean isCollection() {
        return Collection.class.isAssignableFrom(this.asClass());
    }

    public boolean isMap() {
        return Map.class.isAssignableFrom(this.asClass());
    }

    public boolean isTimeType() {
        if (Temporal.class.isAssignableFrom(this.asClass())) {
            return true;
        }
        if (TemporalAdjuster.class.isAssignableFrom(this.asClass())) {
            return true;
        }
        if (TemporalAmount.class.isAssignableFrom(this.asClass())) {
            return true;
        }
        if (this.asClass().equals(ZoneId.class)) {
            return true;
        }
        return false;
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

    public static Class<?> castToClass(Type type) {
        if (type instanceof WildcardType) {
            return Object.class;
        }

        if (isParameterized(type)) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        }

        return (Class<?>) type;
    }

    public static boolean isParameterized(final Type type) {
        return type instanceof ParameterizedType && ((ParameterizedType) type).getActualTypeArguments().length > 0;
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
