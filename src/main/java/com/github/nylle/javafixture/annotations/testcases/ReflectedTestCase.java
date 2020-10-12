package com.github.nylle.javafixture.annotations.testcases;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;

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

}
