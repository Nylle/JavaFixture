package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.annotation.Annotation;
import java.util.Random;

public class EnumSpecimen<T> implements ISpecimen<T> {

    private final SpecimenType<T> type;
    private final Random random;

    public EnumSpecimen(final SpecimenType<T> type ) {

        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (!type.isEnum()) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.random = new Random();
    }

    @Override
    public T create(CustomizationContext customizationContext, Annotation[] annotations) {
        return type.getEnumConstants()[random.nextInt(type.getEnumConstants().length)];
    }
}
