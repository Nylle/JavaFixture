package com.github.nylle.javafixture;

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
            throw new RuntimeException(
                    "Cannot populate field '" + fieldName + "' on '" + typeReference.getName() + "'. Inner exception: " +
                            e.toString(),
                    e);
        }
    }
}
