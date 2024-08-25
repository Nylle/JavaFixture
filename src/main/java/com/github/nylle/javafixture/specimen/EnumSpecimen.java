package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.annotation.Annotation;
import java.util.Random;

public class EnumSpecimen<T> implements ISpecimen<T> {

    private final SpecimenType<T> type;
    private final Random random;

    public EnumSpecimen(SpecimenType<T> type) {

        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (!supportsType(type)) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.random = new Random();
    }

    public static <T> boolean supportsType(SpecimenType<T> type) {
        return type.isEnum();
    }

    @Override
    public T create(CustomizationContext customizationContext, Annotation[] annotations) {
        return type.getEnumConstants()[random.nextInt(type.getEnumConstants().length)];
    }

    public static class Spec implements ISpec {

        @Override
        public <T> boolean supports(SpecimenType<T> type) {
            return supportsType(type);
        }

        @Override
        public <T> ISpecimen<T> create(SpecimenType<T> type, Context context, SpecimenFactory specimenFactory) {
            return new EnumSpecimen<>(type);
        }
    }
}
