package com.github.nylle.javafixture.instantiation;

import java.util.Map;

public class Primitive {
    private static final Map<Class<?>, Object> primitiveDefaults = Map.of(
            Boolean.TYPE, false,
            Character.TYPE, '\0',
            Byte.TYPE, (byte) 0,
            Short.TYPE, 0,
            Integer.TYPE, 0,
            Long.TYPE, 0L,
            Float.TYPE, 0.0f,
            Double.TYPE, 0.0d
    );

    private Primitive() {
    }

    public static Object defaultValue(Class<?> type) {
        return primitiveDefaults.get(type);
    }
}
