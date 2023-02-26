package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.PseudoRandom;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.specimen.constraints.StringConstraints;

import java.lang.annotation.Annotation;
import javax.persistence.Column;
import javax.validation.constraints.Size;

public class PrimitiveSpecimen<T> implements ISpecimen<T> {

    private final SpecimenType<T> type;
    private final PseudoRandom pseudoRandom;
    private final Configuration configuration;
    private final Context context;

    public PrimitiveSpecimen(final SpecimenType<T> type, final Context context) {

        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (!type.isPrimitive() && !type.isBoxed() && type.asClass() != String.class) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        this.type = type;
        this.pseudoRandom = new PseudoRandom();
        this.configuration = context.getConfiguration();
        this.context = context;
    }

    @Override
    public T create(final CustomizationContext customizationContext, Annotation[] annotations) {
        if (type.asClass().equals(String.class)) {
            return context.preDefined(type, (T) pseudoRandom.nextString(detectStringConstraints(annotations)));
        }

        if (type.asClass().equals(Boolean.class) || type.asClass().equals(boolean.class)) {
            return context.preDefined(type, (T) pseudoRandom.nextBool());
        }

        if (type.asClass().equals(Character.class) || type.asClass().equals(char.class)) {
            return context.preDefined(type, (T) pseudoRandom.nextChar());
        }

        if (type.asClass().equals(Byte.class) || type.asClass().equals(byte.class)) {
            return context.preDefined(type, (T) pseudoRandom.nextByte());
        }

        if (type.asClass().equals(Short.class) || type.asClass().equals(short.class)) {
            return context.preDefined(type, (T) pseudoRandom.nextShort(configuration.usePositiveNumbersOnly()));
        }

        if (type.asClass().equals(Integer.class) || type.asClass().equals(int.class)) {
            return context.preDefined(type, (T) pseudoRandom.nextInt(configuration.usePositiveNumbersOnly()));
        }

        if (type.asClass().equals(Long.class) || type.asClass().equals(long.class)) {
            return context.preDefined(type, (T) pseudoRandom.nextLong(configuration.usePositiveNumbersOnly()));
        }

        if (type.asClass().equals(Float.class) || type.asClass().equals(float.class)) {
            return context.preDefined(type, (T) pseudoRandom.nextFloat(configuration.usePositiveNumbersOnly()));
        }

        if (type.asClass().equals(Double.class) || type.asClass().equals(double.class)) {
            return context.preDefined(type, (T) pseudoRandom.nextDouble(configuration.usePositiveNumbersOnly()));
        }

        throw new SpecimenException("Unsupported type: " + type);
    }

    private StringConstraints detectStringConstraints(Annotation[] annotations) {
        for (var annotation : annotations) {
            if(Size.class.isAssignableFrom(annotation.annotationType())) {
                return new StringConstraints(((Size)annotation).min(), ((Size)annotation).max());
            }

            if(Column.class.isAssignableFrom(annotation.annotationType())) {
                return new StringConstraints(0, ((Column) annotation).length());
            }

            if (jakarta.validation.constraints.Size.class.isAssignableFrom(annotation.annotationType())) {
                return new StringConstraints(((jakarta.validation.constraints.Size)annotation).min(), ((jakarta.validation.constraints.Size)annotation).max());
            }

            if (jakarta.persistence.Column.class.isAssignableFrom(annotation.annotationType())) {
                return new StringConstraints(0, ((jakarta.persistence.Column) annotation).length());
            }
        }
        return new StringConstraints(0, Integer.MAX_VALUE);
    }
}

