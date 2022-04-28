package com.github.nylle.javafixture;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import javassist.util.proxy.ProxyFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class InstanceFactory {

    private final SpecimenFactory specimenFactory;
    private final Random random;

    public InstanceFactory(SpecimenFactory specimenFactory) {
        this.specimenFactory = specimenFactory;
        this.random = new Random();
    }

    public <T> T construct(final SpecimenType<T> type) {
        var constructors = type.getDeclaredConstructors()
                .stream()
                .filter(x -> Modifier.isPublic(x.getModifiers()))
                .collect(toList());

        if (constructors.isEmpty()) {
            return manufacture(type);
        }

        return construct(type, constructors.get(random.nextInt(constructors.size())));
    }

    public <T> T manufacture(final SpecimenType<T> type) {
        var results = type.getFactoryMethods()
                .stream()
                .filter(x -> Modifier.isPublic(x.getModifiers()))
                .map(x -> manufactureOrNull(x, type))
                .filter(x -> x != null)
                .map(x -> (T) x)
                .collect(toList());

        if (results.isEmpty()) {
            throw new SpecimenException(format("Cannot manufacture %s", type.asClass()));
        }

        return results.get(random.nextInt(results.size()));
    }

    public <T> T instantiate(final SpecimenType<T> type) {
        try {
            return type.asClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return (T) ((ObjectInstantiator) ((Objenesis) new ObjenesisStd()).getInstantiatorOf(type.asClass())).newInstance();
        }
    }

    public <T> Object proxy(final SpecimenType<T> type) {
        return proxy(type, new HashMap<>());
    }

    public <T> Object proxy(final SpecimenType<T> type, final Map<String, ISpecimen<?>> specimens) {
        if (type.isInterface()) {
            return Proxy.newProxyInstance(
                    type.asClass().getClassLoader(),
                    new Class[]{type.asClass()},
                    new ProxyInvocationHandler(specimenFactory, specimens));
        }

        return createProxyForAbstract(type, specimens);
    }

    public <G, T extends Collection<G>> T createCollection(final SpecimenType<T> type) {
        return type.isInterface() ? createCollectionFromInterfaceType(type.asClass()) : createCollectionFromConcreteType(type);
    }

    private <T> T construct(final SpecimenType<T> type, final Constructor<?> constructor) {
        try {
            constructor.setAccessible(true);
            return (T) constructor.newInstance(stream(constructor.getGenericParameterTypes())
                    .map(t -> specimenFactory.build(SpecimenType.fromClass(t)))
                    .map(s -> s.create(new Annotation[0]))
                    .toArray());
        } catch (Exception e) {
            return manufacture(type);
        }
    }

    private <T> T createProxyForAbstract(final SpecimenType<T> type, final Map<String, ISpecimen<?>> specimens) {
        try {
            var factory = new ProxyFactory();
            factory.setSuperclass(type.asClass());
            return (T) factory.create(new Class<?>[0], new Object[0], new ProxyInvocationHandler(specimenFactory, specimens));
        } catch (Exception e) {
            throw new SpecimenException(format("Unable to construct abstract class %s: %s", type.asClass(), e.getMessage()), e);
        }
    }

    private <T> T manufactureOrNull(final Method method, SpecimenType<T> type) {
        try {
            Type[] parameterTypes;
            if (type.isParameterized()) {
                parameterTypes = type.getGenericTypeArguments();
            } else {
                parameterTypes = method.getGenericParameterTypes();
            }
            return (T) method.invoke(null, stream(parameterTypes)
                    .map(t -> specimenFactory.build(SpecimenType.fromClass(t)))
                    .map(s -> s.create(new Annotation[0]))
                    .toArray());
        } catch (Exception ex) {
            return null;
        }
    }

    private <G, T extends Collection<G>> T createCollectionFromInterfaceType(final Class<T> type) {

        if (List.class.isAssignableFrom(type)) {
            return (T) new ArrayList<G>();
        }

        if (NavigableSet.class.isAssignableFrom(type)) {
            return (T) new TreeSet<G>();
        }

        if (SortedSet.class.isAssignableFrom(type)) {
            return (T) new TreeSet<G>();
        }

        if (Set.class.isAssignableFrom(type)) {
            return (T) new HashSet<G>();
        }

        if (BlockingDeque.class.isAssignableFrom(type)) {
            return (T) new LinkedBlockingDeque<G>();
        }

        if (Deque.class.isAssignableFrom(type)) {
            return (T) new ArrayDeque<G>();
        }

        if (TransferQueue.class.isAssignableFrom(type)) {
            return (T) new LinkedTransferQueue<G>();
        }

        if (BlockingQueue.class.isAssignableFrom(type)) {
            return (T) new LinkedBlockingQueue<G>();
        }

        if (Queue.class.isAssignableFrom(type)) {
            return (T) new LinkedList<G>();
        }

        if (Collection.class.isAssignableFrom(type)) {
            return (T) new ArrayList<G>();
        }

        throw new SpecimenException("Unsupported type: " + type);
    }

    private <G, T extends Collection<G>> T createCollectionFromConcreteType(final SpecimenType<T> type) {
        try {
            return type.asClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new SpecimenException("Unable to create collection of type " + type.getName(), e);
        }
    }
}
