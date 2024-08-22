package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.stream.IntStream;

public class ArraySpecimen<T> implements ISpecimen<T> {
    private final SpecimenType<T> type;
    private final Context context;
    private final SpecimenFactory specimenFactory;

    public ArraySpecimen(final SpecimenType<T> type, Context context, SpecimenFactory specimenFactory) {
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
    }

    @Override
    public T create(final CustomizationContext customizationContext, Annotation[] annotations) {
        if (context.isCached(type)) {
            return context.cached(type);
        }

        int length = context.getConfiguration().getRandomCollectionSize();

        T result = (T) context.cached(type, Array.newInstance(type.getComponentType(), length));

        IntStream.range(0, length).boxed().forEach(i -> Array.set(result, i, specimenFactory.build(SpecimenType.fromClass(type.getComponentType())).create(customizationContext, new Annotation[0])));

        return context.remove(type);
    }
}
