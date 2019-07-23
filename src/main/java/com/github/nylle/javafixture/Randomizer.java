package com.github.nylle.javafixture;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Randomizer {

    private static final int MAX_COLLECTION_SIZE = 10;

    public static <T> T random(final Class<T> type) {
        final Random random = new Random();

        if (type.equals(String.class)) {
            return (T) UUID.randomUUID().toString();
        }

        if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return (T) Boolean.valueOf(random.nextBoolean());
        }

        if (type.equals(Byte.class) || type.equals(byte.class)) {
            return (T) Byte.valueOf(UUID.randomUUID().toString().getBytes(Charset.defaultCharset())[0]);
        }

        if (type.equals(Short.class) || type.equals(short.class)) {
            return (T) Short.valueOf((short)random.nextInt(Short.MAX_VALUE + 1));
        }

        if (type.equals(Integer.class) || type.equals(int.class)) {
            return (T) Integer.valueOf(random.nextInt());
        }

        if (type.equals(Long.class) || type.equals(long.class)) {
            return (T) Long.valueOf(random.nextLong());
        }

        if (type.equals(Float.class) || type.equals(float.class)) {
            return (T) Float.valueOf(random.nextFloat());
        }

        if (type.equals(Double.class) || type.equals(double.class)) {
            return (T) Double.valueOf(random.nextDouble());
        }

        try {
            final T result = type.getDeclaredConstructor().newInstance();
            for (Field field : type.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    field.set(result, random(field.getType()));
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

    public static <T> Stream<T> randomStreamOf(final Class<T> type) {
        return IntStream.range(0, randomLength()).boxed().map(x -> random(type));
    }

    public static <T> Stream<T> randomStreamOf(final int length, final Class<T> type) {
        return IntStream.range(0, length).boxed().map(x -> random(type));
    }

    private static int randomLength() {
        return 1 + new Random().nextInt(MAX_COLLECTION_SIZE);
    }
}