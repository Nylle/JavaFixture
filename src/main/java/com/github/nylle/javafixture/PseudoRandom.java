package com.github.nylle.javafixture;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

public class PseudoRandom {

    private final Random random = new Random();

    public Short nextShort(boolean positiveOnly) {
        return positiveOnly
                ? (short) random.nextInt(1 << 15)
                : (short) random.nextInt(1 << 16);
    }

    public Integer nextInt(boolean positiveOnly) {
        return positiveOnly
                ? (int) (new Random().nextFloat() * (Integer.MAX_VALUE))
                : random.nextInt();
    }

    public Long nextLong(boolean positiveOnly) {
        return positiveOnly
                ? (long) (Math.random() * (Long.MAX_VALUE))
                : random.nextLong();
    }

    public Float nextFloat(boolean positiveOnly) {
        return positiveOnly
                ? new Random().nextFloat() * (Float.MAX_VALUE)
                : random.nextFloat();
    }

    public Double nextDouble(boolean positiveOnly) {
        return positiveOnly
                ? new Random().nextDouble() * (Double.MAX_VALUE)
                : random.nextDouble();
    }

    public String nextString() {
        return UUID.randomUUID().toString();
    }

    public Boolean nextBool() {
        return random.nextBoolean();
    }

    public Character nextChar() {
        return UUID.randomUUID().toString().charAt(0);
    }

    public Byte nextByte() {
        return UUID.randomUUID().toString().getBytes(Charset.defaultCharset())[0];
    }
}
