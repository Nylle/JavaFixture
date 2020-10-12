package com.github.nylle.javafixture.specimen;

import static com.github.nylle.javafixture.CustomizationContext.noContext;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.EnumSet;
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

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

public class CollectionSpecimen<T, G> implements ISpecimen<T> {
    private final SpecimenType<T> type;
    private final Context context;
    private ISpecimen<G> specimen;

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

        if (type.asClass().equals(EnumSet.class)) {
            return createEnumSet();
        }

        Collection<G> collection = context.cached(type, type.isInterface() ? createFromInterfaceType(type.asClass()) : createFromConcreteType(type));

        IntStream.range(0, context.getConfiguration().getRandomCollectionSize())
                .boxed()
                .filter(x -> specimen != null)
                .forEach(x -> collection.add(specimen.create()));

        return (T) collection;
    }

    private <G extends Enum> T createEnumSet() {
        final List<G> elements = IntStream.range(0, context.getConfiguration().getRandomCollectionSize())
                .boxed()
                .filter(x -> specimen != null)
                .map(x -> (G) specimen.create())
                .collect(toList());

        return (T) EnumSet.of(elements.get(0), (G[]) elements.stream().skip(1).toArray(size -> new Enum[size]));
    }

    private Collection<G> createFromConcreteType(final SpecimenType<T> type) {
        try {
            return (Collection<G>) type.asClass().getDeclaredConstructor().newInstance();
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
