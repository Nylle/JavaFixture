package com.github.nylle.javafixture;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
        var constructors = stream(type.asClass().getConstructors())
                .filter(x -> Modifier.isPublic(x.getModifiers()))
                .collect(toList());

        if (constructors.isEmpty()) {
            throw new SpecimenException(format("Cannot construct %s: no public constructor found", type.asClass()));
        }

        Constructor<?> constructor = constructors.get(random.nextInt(constructors.size()));

        try {
            return (T) constructor.newInstance(
                    stream(constructor.getGenericParameterTypes())
                            .map(t -> specimenFactory.build(SpecimenType.fromClass(t)))
                            .map(s -> s.create())
                            .toArray(size -> new Object[size]));
        } catch (Exception e) {
            throw new SpecimenException(format("Unable to construct class %s with constructor %s", type.asClass(), constructor.toString()), e);
        }
    }

    public <T> T instantiate(final SpecimenType<T> type) {
        try {
            return type.asClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return (T) ((ObjectInstantiator) ((Objenesis) new ObjenesisStd()).getInstantiatorOf(type.asClass())).newInstance();
        }
    }

    public <T> Object proxy(final SpecimenType<T> type) {
        return Proxy.newProxyInstance(
                type.asClass().getClassLoader(),
                new Class[]{type.asClass()},
                new ProxyInvocationHandler(specimenFactory, new HashMap<>()));
    }

    public <T> Object proxy(final SpecimenType<T> type, final Map<String, ISpecimen<?>> specimens) {
        return Proxy.newProxyInstance(
                type.asClass().getClassLoader(),
                new Class[]{type.asClass()},
                new ProxyInvocationHandler(specimenFactory, specimens));
    }
}
