package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.Reflector;
import com.github.nylle.javafixture.Specimen;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

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

public class CollectionSpecimen<T, G> implements Specimen<T> {
    private final Class<T> type;
    private final Class<G> genericType;
    private final Context context;
    private final SpecimenFactory specimenFactory;

    public CollectionSpecimen(final Class<T> type, final Class<G> genericType, final Context context, final SpecimenFactory specimenFactory) {

        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (genericType == null) {
            throw new IllegalArgumentException("genericType: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if (!Reflector.isCollection(type)) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.genericType = genericType;
        this.context = context;
        this.specimenFactory = specimenFactory;
    }

    @Override
    public T create() {
        Collection<G> collection = type.isInterface() ? createFromInterfaceType(type) : createFromConcreteType(type);

        IntStream.range(0, context.getConfiguration().getRandomCollectionSize())
                .boxed()
                .forEach(x -> collection.add(specimenFactory.build(genericType).create()));


        return (T) context.cached(SpecimenType.forCollection(type, genericType), collection);
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

        throw new SpecimenException("Unsupported type: " + type);
    }

}
