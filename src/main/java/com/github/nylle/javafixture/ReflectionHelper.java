package com.github.nylle.javafixture;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;


public class ReflectionHelper {
    public static void setField(final Field field, final Object instance, final Object value) {
        try {
            field.setAccessible(true);
            field.set(instance, value);
        } catch (SecurityException e) {
            throw new SpecimenException(format("Unable to access field %s on object of type %s", field.getName(), instance.getClass().getName()), e);
        } catch (IllegalAccessException e) {
            throw new SpecimenException(format("Unable to set field %s on object of type %s", field.getName(), instance.getClass().getName()), e);
        }

    }

    public static void setField(final String fieldName, final Object instance, final Object value) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            setField(field, instance, value);
        } catch (SecurityException e) {
            throw new SpecimenException(format("Unable to access field %s on object of type %s", fieldName, instance.getClass().getName()), e);
        } catch (NoSuchFieldException e) {
            throw new SpecimenException(format("Unknown field %s on object of type %s", fieldName, instance.getClass().getName()), e);
        }
    }

    public static void unsetField(final String fieldName, final Object instance) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, getDefaultValueForPrimitiveOrNull(field.getType()));
        } catch (SecurityException e) {
            throw new SpecimenException(format("Unable to access field %s on object of type %s", fieldName, instance.getClass().getName()), e);
        } catch (IllegalAccessException e) {
            throw new SpecimenException(format("Unable to set field %s on object of type %s", fieldName, instance.getClass().getName()), e);
        } catch (NoSuchFieldException e) {
            throw new SpecimenException(format("Unknown field %s on object of type %s", fieldName, instance.getClass().getName()), e);
        }
    }

    public static boolean isBoxedOrString(Class<?> type) {
        return type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class || type == String.class;
    }

    public static boolean isCollection(final Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }

    public static boolean isMap(final Class<?> type) {
        return Map.class.isAssignableFrom(type);
    }

    public static boolean isParameterizedType(final Type type) {
        return type != null && type instanceof ParameterizedType && ((ParameterizedType) type).getActualTypeArguments().length > 0;
    }

    public static Class<?> getGenericTypeClass(final Type type, final int index) {
        return (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[index];
    }

    public static Type getGenericType(final Type type, final int index) {
        return ((ParameterizedType) type).getActualTypeArguments()[index];
    }

    public static boolean isStatic(final Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    public static <T> boolean isNull(Field field, T instance) {
        try {
            return field.get(instance) == null;
        } catch (IllegalAccessException e) {
            return true;
        }
    }

    public static boolean isTimeType(Class<?> type) {
        if (Temporal.class.isAssignableFrom(type)) {
            return true;
        }
        if (TemporalAdjuster.class.isAssignableFrom(type)) {
            return true;
        }
        if (TemporalAmount.class.isAssignableFrom(type)) {
            return true;
        }
        if (type.equals(ZoneId.class)) {
            return true;
        }
        return false;
    }

    private static <T> T getDefaultValueForPrimitiveOrNull(Class<T> type) {
        return (T) Array.get(Array.newInstance(type, 1), 0);
    }

    static Optional<Class> getRawType(Type type, int index) {
        if (isParameterizedType(type)) {
            var actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[index];
            if (isParameterizedType(actualTypeArgument)) {
                return Optional.of((Class) ((ParameterizedType) actualTypeArgument).getRawType());
            }
        }
        return Optional.empty();
    }
}
