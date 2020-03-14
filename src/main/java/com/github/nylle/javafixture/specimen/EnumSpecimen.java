package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.FixtureType;
import com.github.nylle.javafixture.ISpecimen;

import java.util.Random;

import static com.github.nylle.javafixture.CustomizationContext.noContext;

public class EnumSpecimen<T> implements ISpecimen<T> {

    private final FixtureType<T> type;
    private final Random random;

    public EnumSpecimen(final FixtureType<T> type) {

        if(type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (!type.isEnum()) {
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
    public T create(CustomizationContext customizationContext) {
        return type.getEnumConstants()[random.nextInt(type.getEnumConstants().length)];
    }
}
