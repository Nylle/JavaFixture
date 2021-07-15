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

    private static final Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<>();

    private Map<Class<?>, List<?>> matrix = new HashMap<>();

    public ReflectedTestCase(TestCase testCase) {

        primitiveWrapperMap.put(Boolean.class, Boolean.TYPE);
        primitiveWrapperMap.put(Character.class, Character.TYPE);
        primitiveWrapperMap.put(Byte.class, Byte.TYPE);
        primitiveWrapperMap.put(Short.class, Short.TYPE);
        primitiveWrapperMap.put(Integer.class, Integer.TYPE);
        primitiveWrapperMap.put(Long.class, Long.TYPE);
        primitiveWrapperMap.put(Float.class, Float.TYPE);
        primitiveWrapperMap.put(Double.class, Double.TYPE);

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
