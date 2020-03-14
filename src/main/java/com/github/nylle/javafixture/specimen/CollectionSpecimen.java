package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.FixtureType;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.stream.IntStream;

import static com.github.nylle.javafixture.CustomizationContext.noContext;

public class CollectionSpecimen<T, G> implements ISpecimen<T> {
    private final FixtureType<T> type;
    private final Context context;
    private ISpecimen<G> specimen;

    public CollectionSpecimen(final FixtureType<T> type, final Context context, final SpecimenFactory specimenFactory) {

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
            throw new IllegalArgumentException("type: " + type.asClass().getName());
        }

        this.type = type;
        this.context = context;

        if(type.isParameterized()) {
            this.specimen = specimenFactory.build(FixtureType.fromClass(type.getGenericTypeArgument(0)));
        }
    }

    @Override
    public T create() {
        return create(noContext());
    }

    @Override
    public T create(final CustomizationContext customizationContext) {
        if (context.isCached(type)) {
            return (T) context.cached(type);
        }

        Collection<G> collection = context.cached(type, type.isInterface() ? createFromInterfaceType(type.asClass()) : createFromConcreteType(type.asClass()));

        IntStream.range(0, context.getConfiguration().getRandomCollectionSize())
                .boxed()
                .filter(x -> specimen != null)
                .forEach(x -> collection.add(specimen.create()));

        return (T) collection;
    }

    private Collection<G> createFromConcreteType(final Class<?> type) {
        try {
            return (Collection<G>) type.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new SpecimenException("Unable to create collection of type " + type.getName(), e);
        }
    }

    private Collection<G> createFromInterfaceType(final Class<T> type) {

        if (List.class.isAssignableFrom(type)) {
            return new ArrayList<>();
        }

        if (NavigableSet.class.isAssignableFrom(type)) {
            return new TreeSet<>();
        }

        if (SortedSet.class.isAssignableFrom(type)) {
            return new TreeSet<>();
        }

        if (Set.class.isAssignableFrom(type)) {
            return new HashSet<>();
        }

        if (BlockingDeque.class.isAssignableFrom(type)) {
            return new LinkedBlockingDeque<>();
        }

        if (Deque.class.isAssignableFrom(type)) {
            return new ArrayDeque<>();
        }

        if (TransferQueue.class.isAssignableFrom(type)) {
            return new LinkedTransferQueue<>();
        }

        if (BlockingQueue.class.isAssignableFrom(type)) {
            return new LinkedBlockingQueue<>();
        }

        if (Queue.class.isAssignableFrom(type)) {
            return new LinkedList<>();
        }

        if (Collection.class.isAssignableFrom(type)) {
            return new ArrayList<>();
        }

        throw new SpecimenException("Unsupported type: " + type);
    }

}
