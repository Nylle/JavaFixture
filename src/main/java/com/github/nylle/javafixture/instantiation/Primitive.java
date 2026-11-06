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
            Double.TYPE, 0.0d);

    private static final Map<Class<?>, Class<?>> types = Map.of(
            Boolean.class, Boolean.TYPE,
            Character.class, Character.TYPE,
            Byte.class, Byte.TYPE,
            Short.class, Short.TYPE,
            Integer.class, Integer.TYPE,
            Long.class, Long.TYPE,
            Float.class, Float.TYPE,
            Double.class, Double.TYPE);

    private Primitive() { }

    public static Object defaultValue(Class<?> type) {
        return primitiveDefaults.get(type);
    }

    public static Class<?> fromBoxed(Class<?> type) {
        return types.getOrDefault(type, type);
    }

    public static <T> T parse(String s, Class<T> type) {
        if (type == String.class) {
            return type.cast(String.valueOf(s));
        }
        var unboxed = fromBoxed(type);
        if (unboxed == Boolean.TYPE) {
            return (T) Boolean.valueOf(s);
        }
        if (unboxed == Character.TYPE) {
            if (s.length() != 1) {
                throw new IllegalArgumentException("Expected a single character: " + s);
            }
            return (T) Character.valueOf(s.charAt(0));
        }
        if (unboxed == Byte.TYPE) {
            return (T) Byte.valueOf(s);
        }
        if (unboxed == Short.TYPE) {
            return (T) Short.valueOf(s);
        }
        if (unboxed == Integer.TYPE) {
            return (T) Integer.valueOf(s);
        }
        if (unboxed == Long.TYPE) {
            return (T) Long.valueOf(s);
        }
        if (unboxed == Float.TYPE) {
            return (T) Float.valueOf(s);
        }
        if (unboxed == Double.TYPE) {
            return (T) Double.valueOf(s);
        }

        throw new IllegalArgumentException("Unsupported type: " + type);
    }
}
