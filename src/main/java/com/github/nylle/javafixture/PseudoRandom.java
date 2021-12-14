package com.github.nylle.javafixture;

import com.github.nylle.javafixture.specimen.constraints.StringConstraints;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PseudoRandom {

    private final Random random = new Random();
    private static final char[] letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();

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

    public String nextString(StringConstraints constraints) {
        validate(constraints.getMin(), constraints.getMax());
        return Stream.generate(() -> "" + letters[random.nextInt(letters.length)])
                .limit(constraints.getMin() + Math.min(128, random.nextInt(constraints.getMax() - constraints.getMin())))
                .collect(Collectors.joining());
    }

    private void validate(int min, int max) {
        if (min < 0) {
            throw new SpecimenException("minimum must be non-negative");
        }
        if (max < min) {
            throw new SpecimenException("maximum must be greater than or equal minimum");
        }
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
