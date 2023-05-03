package com.github.nylle.javafixture.annotations.testcases;

import com.github.nylle.javafixture.SpecimenException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class ReflectedTestCase {

    private static final Map<Class<?>, Class<?>> primitiveWrapperMap = Map.of(
            Boolean.class, Boolean.TYPE,
            Character.class, Character.TYPE,
            Byte.class, Byte.TYPE,
            Short.class, Short.TYPE,
            Integer.class, Integer.TYPE,
            Long.class, Long.TYPE,
            Float.class, Float.TYPE,
            Double.class, Double.TYPE
    );

    private Map<Class<?>, List<?>> matrix = new HashMap<>();

    public ReflectedTestCase(TestCase testCase) {
        stream(TestCase.class.getDeclaredMethods())
                .sorted(Comparator.comparing(Method::getName))
                .forEachOrdered(m -> matrix.compute(m.getReturnType(), (k, v) -> addTo(v, invoke(m, testCase))));
        validate(matrix);
    }

    @SuppressWarnings("unchecked")
    public <T> T getTestCaseValueFor(Class<T> type, int i) {
        return (T) matrix.get(primitiveWrapperMap.getOrDefault(type, type)).get(i);
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

    private static void validate(Map<Class<?>, List<?>> matrix) {
        var defaults = Map.of(
                Class.class, Object.class,
                String.class, "",
                Boolean.class, false,
                Character.class, Character.MIN_VALUE,
                Byte.class, (byte)0,
                Short.class, (short)0,
                Integer.class, 0,
                Long.class, 0L,
                Float.class, 0.0f,
                Double.class, 0.0d
        );

        var duplicateIndices = Stream.iterate(0, i -> i + 1)
                .limit(6)
                .collect(toMap(index -> index, index -> defaults.keySet().stream()
                        .map(key -> matrix.get(primitiveWrapperMap.getOrDefault(key, key)).get(index))
                        .collect(toList())))
                .entrySet().stream()
                .filter(indexToValuePair -> indexToValuePair.getValue().stream()
                        .filter(value -> !defaults.get(value.getClass()).equals(value))
                        .count() > 1)
                .map(indexToValuePair -> indexToValuePair.getKey())
                .collect(toList());

        if(!duplicateIndices.isEmpty()) {
            throw new SpecimenException("Duplicate customisation for test-method arguments at position " + duplicateIndices.stream().map(x -> String.valueOf(x+1)).collect(joining(", ")));
        }
    }
}
