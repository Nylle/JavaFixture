package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.reflect.Array;
import java.util.stream.IntStream;

public class ArraySpecimen<T> implements ISpecimen<T> {
    private final Class<T> type;
    private final Context context;
    private final SpecimenFactory specimenFactory;
    private final SpecimenType specimenType;

    public ArraySpecimen(Class<T> type, Context context, SpecimenFactory specimenFactory) {
        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if (!type.isArray()) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.context = context;
        this.specimenFactory = specimenFactory;
        this.specimenType = SpecimenType.forObject(type);
    }

    @Override
    public T create() {
        if(context.isCached(specimenType)){
            return (T) context.cached(specimenType);
        }

        int length = context.getConfiguration().getRandomCollectionSize();

        T result = (T) context.cached(specimenType, Array.newInstance(type.getComponentType(), length));

        IntStream.range(0, length).boxed().forEach(i -> Array.set(result, i, specimenFactory.build(type.getComponentType()).create()));

        return result;
    }
}
