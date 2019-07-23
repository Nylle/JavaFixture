package com.github.nylle.javafixture;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ObjectRandomizer {
    public static <T> T random(final Class<T> type) {
        try {
            final T result = type.getDeclaredConstructor().newInstance();
            for (Field field : type.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    field.set(result, Randomizer.random(field.getType()));
                } catch (SecurityException e) {
                    throw new RandomizerException("Unable to access field " + field.getName() + " on object of type " + type.getName(), e);
                } catch (IllegalAccessException e) {
                    throw new RandomizerException("Unable to set field " + field.getName() + " on object of type " + type.getName(), e);
                }
            }
            return result;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RandomizerException("Unable to create object of type " + type.getName(), e);
        }
    }
}
