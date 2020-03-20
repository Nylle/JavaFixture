package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.PseudoRandom;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenType;

import static com.github.nylle.javafixture.CustomizationContext.noContext;

public class PrimitiveSpecimen<T> implements ISpecimen<T> {

    private final SpecimenType<T> type;
    private final PseudoRandom pseudoRandom;
    private final Configuration configuration;

    public PrimitiveSpecimen(final SpecimenType<T> type, final Context context) {

        if(type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (!type.isPrimitive() && !type.isBoxed() && type.asClass() != String.class) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.pseudoRandom = new PseudoRandom();
        this.configuration = context.getConfiguration();
    }

    @Override
    public T create() {
        return create(noContext());
    }

    @Override
    public T create(final CustomizationContext customizationContext) {
        if (type.asClass().equals(String.class)) {
            return (T) pseudoRandom.nextString();
        }

        if (type.asClass().equals(Boolean.class) || type.asClass().equals(boolean.class)) {
            return (T) pseudoRandom.nextBool();
        }

        if (type.asClass().equals(Character.class) || type.asClass().equals(char.class)) {
            return (T) pseudoRandom.nextChar();
        }

        if (type.asClass().equals(Byte.class) || type.asClass().equals(byte.class)) {
            return (T) pseudoRandom.nextByte();
        }

        if (type.asClass().equals(Short.class) || type.asClass().equals(short.class)) {
            return (T) pseudoRandom.nextShort(configuration.usePositiveNumbersOnly());
        }

        if (type.asClass().equals(Integer.class) || type.asClass().equals(int.class)) {
            return (T) pseudoRandom.nextInt(configuration.usePositiveNumbersOnly());
        }

        if (type.asClass().equals(Long.class) || type.asClass().equals(long.class)) {
            return (T) pseudoRandom.nextLong(configuration.usePositiveNumbersOnly());
        }

        if (type.asClass().equals(Float.class) || type.asClass().equals(float.class)) {
            return (T) pseudoRandom.nextFloat(configuration.usePositiveNumbersOnly());
        }

        if (type.asClass().equals(Double.class) || type.asClass().equals(double.class)) {
            return (T) pseudoRandom.nextDouble(configuration.usePositiveNumbersOnly());
        }

        throw new SpecimenException("Unsupported type: "+ type);
    }
}

