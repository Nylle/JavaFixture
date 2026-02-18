package com.github.nylle.javafixture.annotations.testcases;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;
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
            Double.class, 0.0d
    );

    private final Map<Class<?>, List<?>> matrix = new HashMap<>();

    public ReflectedTestCase(TestCase testCase) {
        stream(TestCase.class.getDeclaredMethods())
                .sorted(Comparator.comparing(method -> method.getName()))
                .forEachOrdered(m -> matrix.compute(m.getReturnType(), (k, v) -> addTo(v, invoke(m, testCase))));
    }

    @SuppressWarnings("unchecked")
    public <T> T getTestCaseValueFor(Class<T> type, int i) {
        validate(type, i);
        if (type.isEnum()) {
            return (T) Enum.valueOf((Class<Enum>) type, (String) matrix.get(asPrimitive(String.class)).get(i));
        }
        return (T) matrix.get(asPrimitive(type)).get(i);
    }

    private <T> void validate(Class<T> type, int i) {
        var nonDefaultValues = defaults.keySet().stream()
                .map(key -> matrix.get(asPrimitive(key)).get(i))
                .filter(value -> !defaults.get(value.getClass()).equals(value))
                .collect(toList());

        if (isInvalid(type, nonDefaultValues)) {
            throw new TestCaseException(String.format("Duplicate customisation found for argument at position %d, wanted: %s, found: %s",
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

    private static <T> boolean isInvalid(Class<T> type, List<?> nonDefaultValues) {
        if (nonDefaultValues.size() < 1) {
            return false;
        }
        if (nonDefaultValues.size() > 1) {
            return true;
        }
        if (type.isEnum()) {
            return false;
        }
        return asPrimitive(type) != asPrimitive(nonDefaultValues.get(0).getClass());
    }

    private static Class<?> asPrimitive(Class<?> type) {
        return Map.<Class<?>, Class<?>>of(
                        Boolean.class, Boolean.TYPE,
                        Character.class, Character.TYPE,
                        Byte.class, Byte.TYPE,
                        Short.class, Short.TYPE,
                        Integer.class, Integer.TYPE,
                        Long.class, Long.TYPE,
                        Float.class, Float.TYPE,
                        Double.class, Double.TYPE)
                .getOrDefault(type, type);
    }
}
