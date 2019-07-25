package com.github.nylle.javafixture;

import static com.github.nylle.javafixture.Reflector.isParameterizedType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

public class CollectionFactory {

    public <T> T create(final Class<T> type, final Type genericType, final Randomizer randomizer) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Collection collection = type.isInterface() ? createFromInterface(type) : (Collection<?>) type.getDeclaredConstructor().newInstance();

        if (isParameterizedType(genericType)) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type elementType = parameterizedType.getActualTypeArguments()[0];
            for (int i = 0; i < randomizer.randomLength(); i++) {
                Object item = randomizer.random((Class<?>) elementType);
                collection.add(item);
            }
        }

        return (T) collection;
    }

    private Collection<?> createFromInterface(final Class<?> interfaceType) {

        if (List.class.isAssignableFrom(interfaceType)) {
            return new ArrayList<>();
        }

        if (NavigableSet.class.isAssignableFrom(interfaceType)) {
            return new TreeSet<>();
        }

        if (SortedSet.class.isAssignableFrom(interfaceType)) {
            return new TreeSet<>();
        }

        if (Set.class.isAssignableFrom(interfaceType)) {
            return new HashSet<>();
        }

        if (BlockingDeque.class.isAssignableFrom(interfaceType)) {
            return new LinkedBlockingDeque<>();
        }

        if (Deque.class.isAssignableFrom(interfaceType)) {
            return new ArrayDeque<>();
        }

        if (TransferQueue.class.isAssignableFrom(interfaceType)) {
            return new LinkedTransferQueue<>();
        }

        if (BlockingQueue.class.isAssignableFrom(interfaceType)) {
            return new LinkedBlockingQueue<>();
        }

        if (Queue.class.isAssignableFrom(interfaceType)) {
            return new LinkedList<>();
        }

        throw new SpecimenException("Unsupported type: "+ interfaceType);
    }

}

