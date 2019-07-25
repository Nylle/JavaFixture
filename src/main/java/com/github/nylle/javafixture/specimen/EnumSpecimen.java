package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Specimen;

import java.util.Random;

public class EnumSpecimen<T> implements Specimen<T> {

    private final Class<T> type;
    private final Random random;

    public EnumSpecimen(final Class<T> type) {

        if(type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (!type.isEnum()) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.random = new Random();
    }

    @Override
    public T create() {
        return type.getEnumConstants()[random.nextInt(type.getEnumConstants().length)];
    }
}
