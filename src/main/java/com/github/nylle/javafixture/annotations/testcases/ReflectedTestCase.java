package com.github.nylle.javafixture.annotations.testcases;

import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.instantiation.Primitive;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class ReflectedTestCase {

    private static final Map<Class<?>, ?> defaults = Map.of(
            Class.class, Object.class,
            String.class, "",
            Boolean.class, false,
            Character.class, Character.MIN_VALUE,
            Byte.class, (byte) 0,
            Short.class, (short) 0,
            Integer.class, 0,
            Long.class, 0L,
            Float.class, 0.0f,
            Double.class, 0.0d);

    private final Map<Class<?>, List<?>> matrix = new HashMap<>();

    public ReflectedTestCase(TestCase testCase) {
        Arrays.stream(TestCase.class.getDeclaredMethods())
                .sorted(Comparator.comparing(method -> method.getName()))
                .forEachOrdered(m -> matrix.compute(m.getReturnType(), (k, v) -> addTo(v, invoke(m, testCase))));
    }

    public <T> T getTestCaseValueFor(Class<T> type, int i) {
        return getTestCaseValueFor(SpecimenType.fromClass(type), i);
    }

    public <T> T getTestCaseValueFor(Type type, int i) {
        return getTestCaseValueFor(SpecimenType.fromClass(type), i);
    }

    @SuppressWarnings("unchecked")
    private <T> T getTestCaseValueFor(SpecimenType<T> type, int i) {
        validate(type, i);
        if (type.isEnum()) {
            return (T) Enum.valueOf((Class<Enum>) type.asClass(), (String) matrix.get(Primitive.fromBoxed(String.class)).get(i));
        }
        if (type.isCollection()) {
            var value = (String) matrix.get(Primitive.fromBoxed(String.class)).get(i);
            var items = value.isBlank() ? Stream.of() : Arrays.stream(value.split(","))
                    .map(x -> x.strip())
                    .map(x -> Primitive.parse(x, type.getGenericTypeArgument(0).asClass()));
            return type.asClass().isAssignableFrom(Set.class)
                    ? (T) items.collect(Collectors.toSet())
                    : (T) items.collect(Collectors.toList());
        }
        return (T) matrix.get(Primitive.fromBoxed(type.asClass())).get(i);
    }

    private <T> void validate(SpecimenType<T> type, int i) {
        var nonDefaultValues = defaults.keySet().stream()
                .map(key -> matrix.get(Primitive.fromBoxed(key)).get(i))
                .filter(value -> !defaults.get(value.getClass()).equals(value))
                .collect(toList());

        if (isInvalid(type, nonDefaultValues)) {
            throw new TestCaseException(String.format("Invalid customisation found for argument at position %d, wanted: %s, found: %s",
                    i + 1,
                    type.getName(),
                    nonDefaultValues.stream().map(x -> x.getClass().getName()).collect(joining(", "))));
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T invoke(Method method, TestCase testCase) {
        try {
            return (T) method.invoke(testCase);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new TestCaseException(String.format("Unable to read test-case value for '%s'", method.getName()), e);
        }
    }

    private <T> List<T> addTo(List<T> list, T value) {
        if (list == null) {
            list = new ArrayList<>();
        }

        list.add(value);
        return list;
    }

    private static <T> boolean isInvalid(SpecimenType<T> type, List<?> nonDefaultValues) {
        if (nonDefaultValues.isEmpty()) {
            return false;
        }
        if (nonDefaultValues.size() > 1) {
            return true;
        }
        if (type.isEnum()) {
            return false;
        }
        if (type.isCollection()) {
            return !(type.getGenericTypeArguments().length != 1
                     || Primitive.fromBoxed(type.asClass()) != Primitive.fromBoxed(nonDefaultValues.get(0).getClass())
                     || type.getGenericTypeArgument(0).asClass().isAssignableFrom(String.class));
        }
        return Primitive.fromBoxed(type.asClass()) != Primitive.fromBoxed(nonDefaultValues.get(0).getClass());
    }
}
