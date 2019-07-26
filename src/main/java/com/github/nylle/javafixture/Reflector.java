package com.github.nylle.javafixture;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;


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
            field.set(instance, getDefaultValueForPrimitiveOrNull(field.getType()));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Cannot remove field '%s' on '%s'. Inner exception: %s", fieldName, typeReference.getName(), e.toString()), e);
        }
    }

    public static boolean isBoxedOrString(Class<?> type) {
        return type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class || type == String.class;
    }

    public static <T> boolean isCollection(final Class<T> type) {
        return Collection.class.isAssignableFrom(type);
    }

    public static <T> boolean isMap(final Class<T> type) {
        return Map.class.isAssignableFrom(type);
    }

    public static boolean isParameterizedType(final Type type) {
        return type != null && type instanceof ParameterizedType && ((ParameterizedType) type).getActualTypeArguments().length > 0;
    }

    public static Class<?> getGenericType(final Type type, final int index) {
        return (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[index];
    }

    public static boolean isStatic(final Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    public static void setField(final Field field, final Object instance, final Object value) {
        try {
            field.setAccessible(true);
            field.set(instance, value);
        } catch (SecurityException e) {
            throw new SpecimenException("Unable to access field " + field.getName() + " on object of type " + instance.getClass().getName(), e);
        } catch (IllegalAccessException e) {
            throw new SpecimenException("Unable to set field " + field.getName() + " on object of type " + instance.getClass().getName(), e);
        }

    }

    private static <T> T getDefaultValueForPrimitiveOrNull(Class<T> type) {
        return (T) Array.get(Array.newInstance(type, 1), 0);
    }
}
