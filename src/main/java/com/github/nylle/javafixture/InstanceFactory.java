package com.github.nylle.javafixture;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.util.Arrays.stream;

public class InstanceFactory {

    private final SpecimenFactory specimenFactory;
    private final Random random;

    public InstanceFactory(SpecimenFactory specimenFactory) {
        this.specimenFactory = specimenFactory;
        this.random = new Random();
    }

    public <T> T construct(final SpecimenType<T> type) {
        var constructors = type.asClass().getConstructors();

        if (constructors.length == 0) {
            return instantiate(type);
        }

        try {
            return (T) constructors[random.nextInt(constructors.length)].newInstance(
                    stream(constructors[random.nextInt(constructors.length)].getGenericParameterTypes())
                            .map(t -> specimenFactory.build(SpecimenType.fromClass(t)))
                            .map(s -> s.create())
                            .toArray(size -> new Object[size]));
        } catch (Exception e) {
            return instantiate(type);
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
