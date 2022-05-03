package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.InstanceFactory;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class CollectionSpecimen<T, G> implements ISpecimen<T> {
    private final SpecimenType<T> type;
    private final Context context;
    private ISpecimen<G> specimen;
    private final InstanceFactory instanceFactory;

    public CollectionSpecimen(final SpecimenType<T> type, final Context context, final SpecimenFactory specimenFactory) {

        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if (!type.isCollection()) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.context = context;

        if (type.isParameterized()) {
            this.specimen = specimenFactory.build(SpecimenType.fromClass(type.getGenericTypeArgument(0)));
        }
        this.instanceFactory = new InstanceFactory(specimenFactory);
    }

    @Override
    public T create(final CustomizationContext customizationContext, Annotation[] annotations) {
        if (context.isCached(type)) {
            return context.cached(type);
        }

        if (type.asClass().equals(EnumSet.class)) {
            return context.cached(type, createEnumSet(customizationContext));
        }

        var collection = context.cached(type, instanceFactory.createCollection((SpecimenType<Collection<G>>) type));

        IntStream.range(0, context.getConfiguration().getRandomCollectionSize())
                .boxed()
                .filter(x -> specimen != null)
                .forEach(x -> collection.add(specimen.create(customizationContext, new Annotation[0])));

        return (T) collection;
    }

    private <G extends Enum> T createEnumSet(CustomizationContext customizationContext) {
        final List<G> elements = IntStream.range(0, context.getConfiguration().getRandomCollectionSize())
                .boxed()
                .filter(x -> specimen != null)
                .map(x -> (G) specimen.create(customizationContext, new Annotation[0]))
                .collect(toList());

        return (T) EnumSet.of(elements.get(0), (G[]) elements.stream().skip(1).toArray(size -> new Enum[size]));
    }
}
