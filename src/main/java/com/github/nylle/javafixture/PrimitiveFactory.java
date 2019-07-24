package com.github.nylle.javafixture;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

public class PrimitiveFactory {

    public <T> T create(final Class<T> type, final Random random) {
        if (type.equals(String.class)) {
            return (T) UUID.randomUUID().toString();
        }

        if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return (T) Boolean.valueOf(random.nextBoolean());
        }

        if (type.equals(Character.class) || type.equals(char.class)) {
            return (T) Character.valueOf(UUID.randomUUID().toString().charAt(0));
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

        throw new RandomizerException("Unsupported type: "+ type);

    }
}

