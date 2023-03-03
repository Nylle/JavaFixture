package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.PseudoRandom;
import com.github.nylle.javafixture.SpecimenType;

import java.io.File;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.UUID;

public class SpecialSpecimen<T> implements ISpecimen<T> {
    private static final int MAXIMUM_BITLENGTH_FOR_RANDOM_BIGINT = 1024;
    private final SpecimenType<T> type;
    private final Context context;


    public SpecialSpecimen(SpecimenType<T> type, Context context) {
        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (!type.isSpecialType()) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        this.type = type;
        this.context = context;
    }

    @Override
    public T create(CustomizationContext customizationContext, Annotation[] annotations) {
        if (type.asClass().equals(File.class)) {
            return (T) new File(UUID.randomUUID().toString());
        }
        if (type.asClass().equals(BigInteger.class)) {
            return (T) createBigInteger();
        }
        if (type.asClass().equals(BigDecimal.class)) {
            return (T) createBigDecimal();
        }
        try {
            return (T) new URI("https://localhost/" + UUID.randomUUID());
        } catch (URISyntaxException e) {
            return null;
        }
    }

    private BigInteger createBigInteger() {
        var rnd = new Random();
        var result = new BigInteger(rnd.nextInt(MAXIMUM_BITLENGTH_FOR_RANDOM_BIGINT), new Random());
        if (!context.getConfiguration().usePositiveNumbersOnly()) {
            if (rnd.nextBoolean()) { // negate randomly
                return result.negate();
            }
        }
        return result;
    }

    private BigDecimal createBigDecimal() {
        var bd = new BigDecimal(new PseudoRandom().nextLong(new Random().nextBoolean()));
        if (context.getConfiguration().usePositiveNumbersOnly()) {
            return bd.abs();
        }
        return bd;
    }
}
