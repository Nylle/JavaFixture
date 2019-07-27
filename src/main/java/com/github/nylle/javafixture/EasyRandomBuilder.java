package com.github.nylle.javafixture;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.util.stream.Stream;

import static java.nio.charset.Charset.forName;

public class EasyRandomBuilder<T> extends AbstractSpecimenBuilder<T> implements ISpecimenBuilder<T> {

    private final Class<T> typeReference;
    private final Configuration configuration;

    private final EasyRandom easyRandom;

    public EasyRandomBuilder(final Class<T> typeReference, final Configuration configuration) {
        this.typeReference = typeReference;
        this.configuration = configuration;
        this.easyRandom = new EasyRandom(new EasyRandomParameters()
                .charset(forName("UTF-8"))
                .stringLengthRange(36, 36)
                .collectionSizeRange(configuration.getMinCollectionSize(), configuration.getMaxCollectionSize())
                .ignoreRandomizationErrors(false));
    }

    @Override
    public T create() {
        return customize(easyRandom.nextObject(typeReference));
    }

    @Override
    public Stream<T> createMany() {
        return easyRandom.objects(typeReference, configuration.getStreamSize()).map(x -> customize(x));
    }

    @Override
    public Stream<T> createMany(int size) {
        return easyRandom.objects(typeReference, size).map(x -> customize(x));
    }
}
