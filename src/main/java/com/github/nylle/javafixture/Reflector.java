package com.github.nylle.javafixture;

import java.lang.reflect.Array;
import java.lang.reflect.Field;


public class Reflector<T> {
    private final Class<T> typeReference;

    public Reflector(Class<T> typeReference) {
        this.typeReference = typeReference;
    }

    public void setField(T instance, String fieldName, Object value) {
        try {
            Field field = typeReference.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Cannot populate field '%s' on '%s'. Inner exception: %s", fieldName, typeReference.getName(), e.toString()), e);
        }
    }

    public void unsetField(T instance, String fieldName) {
        try {
            Field field = typeReference.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, getDefaultValue(field.getType()));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Cannot remove field '%s' on '%s'. Inner exception: %s", fieldName, typeReference.getName(), e.toString()), e);
        }
    }

    public static boolean isPrimitiveOrPrimitiveWrapperOrString(Class<?> type) {
        return (type.isPrimitive() && type != void.class) ||
                type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class || type == String.class;
    }

    private static <T> T getDefaultValue(Class<T> type) {
        return (T) Array.get(Array.newInstance(type, 1), 0);
    }

}
