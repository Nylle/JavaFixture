package com.github.nylle.javafixture;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Deque;
import java.util.List;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TransferQueue;


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

    public static <T> boolean isCollection(final Class<T> collectionInterface) {
        return List.class.isAssignableFrom(collectionInterface) || NavigableSet.class.isAssignableFrom(collectionInterface)
                || SortedSet.class.isAssignableFrom(collectionInterface) || Set.class.isAssignableFrom(collectionInterface)
                || BlockingDeque.class.isAssignableFrom(collectionInterface) || Deque.class.isAssignableFrom(collectionInterface)
                || TransferQueue.class.isAssignableFrom(collectionInterface) || BlockingQueue.class.isAssignableFrom(collectionInterface)
                || Queue.class.isAssignableFrom(collectionInterface);
    }

    private static <T> T getDefaultValueForPrimitiveOrNull(Class<T> type) {
        return (T) Array.get(Array.newInstance(type, 1), 0);
    }

}
