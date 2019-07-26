package com.github.nylle.javafixture.specimen;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.IntStream;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.Reflector;
import com.github.nylle.javafixture.Specimen;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

public class MapSpecimen<T, K, V> implements Specimen<T> {
    private final Class<T> type;
    private final Class<K> genericKeyType;
    private final Class<V> genericValueType;
    private final Context context;
    private final SpecimenFactory specimenFactory;

    public MapSpecimen(final Class<T> type, final Class<K> genericKeyType, final Class<V> genericValueType, final Context context, final SpecimenFactory specimenFactory) {

        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (genericKeyType == null) {
            throw new IllegalArgumentException("genericKeyType: null");
        }

        if (genericValueType == null) {
            throw new IllegalArgumentException("genericValueType: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if (!Reflector.isMap(type)) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.genericKeyType = genericKeyType;
        this.genericValueType = genericValueType;
        this.context = context;
        this.specimenFactory = specimenFactory;
    }

    @Override
    public T create() {
        Map<K, V> map = type.isInterface() ? createFromInterfaceType(type) : createFromConcreteType(type);

        IntStream.range(0, context.getConfiguration().getRandomCollectionSize())
                .boxed()
                .forEach(x -> map.put(
                        specimenFactory.build(genericKeyType).create(),
                        specimenFactory.build(genericValueType).create()));

        return (T) context.cached(SpecimenType.forMap(type, genericKeyType, genericValueType), map);
    }

    private Map<K, V> createFromConcreteType(final Class<T> type) {
        try {
            return (Map<K, V>) type.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new SpecimenException("Unable to create map of type " + type.getName(), e);
        }
    }

    private Map<K, V> createFromInterfaceType(final Class<T> type) {

        if (ConcurrentNavigableMap.class.isAssignableFrom(type)) {
            return new ConcurrentSkipListMap<>();
        }

        if (ConcurrentMap.class.isAssignableFrom(type)) {
            return new ConcurrentHashMap<>();
        }

        if (NavigableMap.class.isAssignableFrom(type)) {
            return new TreeMap<>();
        }

        if (SortedMap.class.isAssignableFrom(type)) {
            return new TreeMap<>();
        }

        throw new SpecimenException("Unsupported type: " + type);
    }
}
