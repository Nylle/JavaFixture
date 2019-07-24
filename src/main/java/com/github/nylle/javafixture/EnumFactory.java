package com.github.nylle.javafixture;

import java.util.Random;

public class EnumFactory {
    public <T> T create(final Class<T> type, final Random random) {
        return type.getEnumConstants()[random.nextInt(type.getEnumConstants().length)];
    }
}

