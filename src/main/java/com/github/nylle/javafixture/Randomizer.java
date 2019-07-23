package com.github.nylle.javafixture;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Randomizer {

    private static final int MAX_COLLECTION_SIZE = 10;

    public static <T> T random(final Class<T> type) {

        if(type.isPrimitive() || Reflector.isBoxedOrString(type)) {
            return PrimitiveRandomizer.random(type);
        }

        if(type.isEnum()) {
            //TODO EnumRandomizer
        }

        if(Reflector.isCollection(type)) {
            //TODO CollectionRandomizer
        }

        if(type.isInterface()) {
            return InterfaceRandomizer.random(type);
        }

        return ObjectRandomizer.random(type);
    }

    public static <T> Stream<T> randomStreamOf(final Class<T> type) {
        return IntStream.range(0, randomLength()).boxed().map(x -> random(type));
    }

    public static <T> Stream<T> randomStreamOf(final int length, final Class<T> type) {
        return IntStream.range(0, length).boxed().map(x -> random(type));
    }

    private static int randomLength() {
        return 1 + new Random().nextInt(MAX_COLLECTION_SIZE);
    }
}

