package com.github.nylle.javafixture;

import com.github.nylle.javafixture.instantiation.BuilderInstantiator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class SpecimenType<T> extends TypeCapture<T> {

    private final Type type;

    private SpecimenType(final Type type) {
        this.type = type;
    }

    protected SpecimenType() {
        this.type = capture();
    }

    public static <T> SpecimenType<T> fromClass(final Type typeReference) {
        return new SpecimenType<>(typeReference);
    }

    public static <T> SpecimenType<T> fromRawType(final Class<?> rawType, final Type[] actualTypeArguments) {
        return new SpecimenType<>(TypeCapture.create(rawType, actualTypeArguments));
    }

    public static Class<?> castToClass(Type type) {
        if (type instanceof WildcardType) {
            return (Class<?>) Stream.of(((WildcardType) type).getUpperBounds())
                    .findFirst()
                    .orElse(Stream.of(((WildcardType) type).getLowerBounds())
                            .findFirst()
                            .orElse(Object.class));
        }

        if (isParameterized(type)) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        }

        return (Class<?>) type;
    }

    public static boolean isParameterized(final Type type) {
        return type instanceof ParameterizedType && ((ParameterizedType) type).getActualTypeArguments().length > 0;
    }

    public Class<T> asClass() {
        return (Class<T>) castToClass(type);
    }

    public ParameterizedType asParameterizedType() {
        if (!isParameterized()) {
            throw new SpecimenTypeException(format("%s is not a ParameterizedType", type));
        }
        return (ParameterizedType) type;
    }

    public String[] getTypeParameterNames() {
        if (!isParameterized()) {
            throw new SpecimenTypeException(format("%s is not a ParameterizedType", type));
        }
        return Stream.of(asClass().getTypeParameters()).map(x -> x.getName()).toArray(size -> new String[size]);
    }

    public String getTypeParameterName(final int index) {
        return getTypeParameterNames()[index];
    }

    public Type[] getGenericTypeArguments() {
        if (!isParameterized()) {
            throw new SpecimenTypeException(format("%s is not a ParameterizedType", type));
        }
        return ((ParameterizedType) type).getActualTypeArguments();
    }

    public <A> SpecimenType<A> getGenericTypeArgument(int index) {
        return SpecimenType.fromClass(getGenericTypeArguments()[index]);
    }

    public <A> Map<String, A> getTypeParameterNamesAndTypes(Function<SpecimenType<?>, A> f) {
        if (!isParameterized()) {
            return Map.of();
        }
        return IntStream.range(0, this.getGenericTypeArguments().length)
                .boxed()
                .collect(toMap(
                        i -> this.getTypeParameterName(i),
                        i -> f.apply(this.getGenericTypeArgument(i))));
    }

    public Class<?> getComponentType() {
        if (isArray()) {
            return asClass().getComponentType();
        }

        throw new SpecimenTypeException(format("%s is not an array", type));
    }

    public T[] getEnumConstants() {
        if (isEnum()) {
            return asClass().getEnumConstants();
        }

        throw new SpecimenTypeException(format("%s is not an enum", type));
    }

    public String getName() {
        if (isParameterized()) {
            return asParameterizedType().getTypeName();
        }

        return asClass().getName();
    }

    public boolean isParameterized() {
        return isParameterized(type);
    }

    public boolean isCollection() {
        if(!isInterface() && !isAbstract()) {
            return Collection.class.isAssignableFrom(asClass());
        }

        return Stream.of(Collection.class,
                        List.class,
                        NavigableSet.class,
                        SortedSet.class,
                        Set.class,
                        EnumSet.class,
                        Deque.class,
                        BlockingDeque.class,
                        Queue.class,
                        BlockingQueue.class,
                        TransferQueue.class,
                        ArrayList.class,
                        HashSet.class,
                        TreeSet.class,
                        ArrayDeque.class,
                        LinkedBlockingDeque.class,
                        LinkedList.class,
                        LinkedBlockingQueue.class,
                        LinkedTransferQueue.class)
                .anyMatch(x -> asClass().isAssignableFrom(x));
    }

    public boolean isMap() {
        return Map.class.isAssignableFrom(asClass());
    }

    public boolean isTimeType() {
        if (Temporal.class.isAssignableFrom(asClass())) {
            return true;
        }

        if (TemporalAdjuster.class.isAssignableFrom(asClass())) {
            return true;
        }

        if (TemporalAmount.class.isAssignableFrom(asClass())) {
            return true;
        }

        if (asClass().equals(ZoneId.class)) {
            return true;
        }

        if (asClass().equals(java.util.Date.class)) {
            return true;
        }

        if (asClass().equals(java.sql.Date.class)) {
            return true;
        }

        return false;
    }

    public boolean isSpecialType() {
        if (asClass().equals(java.math.BigInteger.class)) {
            return true;
        }
        if (asClass().equals(java.math.BigDecimal.class)) {
            return true;
        }
        if (asClass().equals(java.io.File.class)) {
            return true;
        }
        if (asClass().equals(java.net.URI.class)) {
            return true;
        }
        return false;
    }

    public boolean isPrimitive() {
        return asClass().isPrimitive();
    }

    public boolean isBoxed() {
        return asClass() == Double.class || asClass() == Float.class || asClass() == Long.class
                || asClass() == Integer.class || asClass() == Short.class || asClass() == Character.class
                || asClass() == Byte.class || asClass() == Boolean.class;
    }

    public boolean isEnum() {
        return asClass().isEnum();
    }

    public boolean isArray() {
        return asClass().isArray();
    }

    public boolean isInterface() {
        return asClass().isInterface();
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(asClass().getModifiers());
    }

    public List<Constructor<T>> getDeclaredConstructors() {
        return Stream.of(this.asClass().getDeclaredConstructors()).map(x -> (Constructor<T>) x).collect(toList());
    }

    public List<Method> getFactoryMethods() {
        return Stream.of(this.asClass().getDeclaredMethods())
                .filter(x -> Modifier.isStatic(x.getModifiers()))
                .filter(x -> x.getReturnType().equals(this.asClass()))
                .collect(Collectors.toList());
    }

    public List<BuilderInstantiator<T>> findBuilders() {
        return Stream.of(asClass().getDeclaredMethods())
                .filter(m -> Modifier.isStatic(m.getModifiers()))
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .filter(m -> !m.getReturnType().equals(asClass()))
                .map(m -> BuilderInstantiator.create(m, this))
                .filter(b -> b != null)
                .collect(toList());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof SpecimenType<?>) {
            SpecimenType<?> that = (SpecimenType<?>) o;
            return this.type.equals(that.type);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
