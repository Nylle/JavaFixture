package com.github.nylle.javafixture;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
            field.set(instance, Array.get(Array.newInstance(field.getType(), 1), 0));
        } catch (SecurityException e) {
            throw new SpecimenException(format("Unable to access field %s on object of type %s", fieldName, instance.getClass().getName()), e);
        } catch (IllegalAccessException e) {
            throw new SpecimenException(format("Unable to set field %s on object of type %s", fieldName, instance.getClass().getName()), e);
        } catch (NoSuchFieldException e) {
            throw new SpecimenException(format("Unknown field %s on object of type %s", fieldName, instance.getClass().getName()), e);
        }
    }

    public static boolean isStatic(final Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

}
