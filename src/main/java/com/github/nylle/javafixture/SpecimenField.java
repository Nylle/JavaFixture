package com.github.nylle.javafixture;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import static java.lang.String.format;

public class SpecimenField {

    private final Field field;

    public SpecimenField(Field field) {
        this.field = field;
    }

    public void set(Object instance, Object value) {
        try {
            field.setAccessible(true);
            field.set(instance, value);
        } catch (SecurityException e) {
            throw new SpecimenException(format("Unable to access field %s on object of type %s", field.getName(), instance.getClass().getName()), e);
        } catch (IllegalAccessException e) {
            throw new SpecimenException(format("Unable to set field %s on object of type %s", field.getName(), instance.getClass().getName()), e);
        }
    }

    public String getName() {
        return field.getName();
    }

    public Type getGenericType() {
        return field.getGenericType();
    }

    public Class<?> getType() {
        return field.getType();
    }
}
