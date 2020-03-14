package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenType;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

import static com.github.nylle.javafixture.CustomizationContext.noContext;

public class PrimitiveSpecimen<T> implements ISpecimen<T> {

    private final SpecimenType<T> type;
    private final Random random;

    public PrimitiveSpecimen(final SpecimenType<T> type) {

        if(type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (!type.isPrimitive() && !type.isBoxed() && type.asClass() != String.class) {
            throw new IllegalArgumentException("type: " + type.asClass().getName());
        }

        this.type = type;
        this.random = new Random();
    }

    @Override
    public T create() {
        return create(noContext());
    }

    @Override
    public T create(final CustomizationContext customizationContext) {
        if (type.asClass().equals(String.class)) {
            return (T) UUID.randomUUID().toString();
        }

        if (type.asClass().equals(Boolean.class) || type.asClass().equals(boolean.class)) {
            return (T) Boolean.valueOf(random.nextBoolean());
        }

        if (type.asClass().equals(Character.class) || type.asClass().equals(char.class)) {
            return (T) Character.valueOf(UUID.randomUUID().toString().charAt(0));
        }

        if (type.asClass().equals(Byte.class) || type.asClass().equals(byte.class)) {
            return (T) Byte.valueOf(UUID.randomUUID().toString().getBytes(Charset.defaultCharset())[0]);
        }

        if (type.asClass().equals(Short.class) || type.asClass().equals(short.class)) {
            return (T) Short.valueOf((short)random.nextInt(Short.MAX_VALUE + 1));
        }

        if (type.asClass().equals(Integer.class) || type.asClass().equals(int.class)) {
            return (T) Integer.valueOf(random.nextInt());
        }

        if (type.asClass().equals(Long.class) || type.asClass().equals(long.class)) {
            return (T) Long.valueOf(random.nextLong());
        }

        if (type.asClass().equals(Float.class) || type.asClass().equals(float.class)) {
            return (T) Float.valueOf(random.nextFloat());
        }

        if (type.asClass().equals(Double.class) || type.asClass().equals(double.class)) {
            return (T) Double.valueOf(random.nextDouble());
        }

        throw new SpecimenException("Unsupported type: "+ type);
    }
}

