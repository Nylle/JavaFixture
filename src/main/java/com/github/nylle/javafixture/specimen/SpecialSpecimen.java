package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.PseudoRandom;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.io.File;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;

public class SpecialSpecimen<T> implements ISpecimen<T> {
    private final static int MAXIMUM_BITLENGTH_FOR_RANDOM_BIGINT = 1024;
    private final static Map<Class<?>, Function<Context, ?>> creators = Map.of(
            File.class, x -> new File(UUID.randomUUID().toString()),
            BigInteger.class, bigInteger(),
            BigDecimal.class, bigDecimal(),
            URI.class, uri());

    private final SpecimenType<T> type;
    private final Context context;

    public SpecialSpecimen(SpecimenType<T> type, Context context) {
        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (!supportsType(type)) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        this.type = type;
        this.context = context;
    }

    public static <T> boolean supportsType(SpecimenType<T> type) {
        return creators.containsKey(type.asClass());
    }

    public static IMeta meta() {
        return new IMeta() {
            @Override
            public <T> boolean supports(SpecimenType<T> type) {
                return supportsType(type);
            }

            @Override
            public <T> ISpecimen<T> create(SpecimenType<T> type, Context context, SpecimenFactory specimenFactory) {
                return new SpecialSpecimen<>(type, context);
            }
        };
    }

    @Override
    public T create(CustomizationContext customizationContext, Annotation[] annotations) {
        return (T) creators.get(type.asClass()).apply(context);
    }

    private static Function<Context, BigInteger> bigInteger() {
        return context -> {
            var rnd = new Random();
            var result = new BigInteger(rnd.nextInt(MAXIMUM_BITLENGTH_FOR_RANDOM_BIGINT), new Random());
            if (context.getConfiguration().usePositiveNumbersOnly()) {
                return result;
            }
            return rnd.nextBoolean() ? result.negate() : result; // negate randomly
        };
    }

    private static Function<Context, BigDecimal> bigDecimal() {
        return context -> {
            var result = new BigDecimal(new PseudoRandom().nextLong(new Random().nextBoolean()));
            if (context.getConfiguration().usePositiveNumbersOnly()) {
                return result.abs();
            }
            return result;
        };
    }

    private static Function<Context, URI> uri() {
        return x -> {
            try {
                return new URI("https://localhost/" + UUID.randomUUID());
            } catch (URISyntaxException e) {
                return null;
            }
        };
    }
}
