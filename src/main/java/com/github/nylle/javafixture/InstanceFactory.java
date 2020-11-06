package com.github.nylle.javafixture;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import javassist.util.proxy.ProxyFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
        var constructors = type.getDeclaredConstructors()
                .stream()
                .filter(x -> Modifier.isPublic(x.getModifiers()))
                .collect(toList());

        if (constructors.isEmpty()) {
            throw new SpecimenException(format("Cannot construct %s: no public constructor found", type.asClass()));
        }

        return construct(type, constructors.get(random.nextInt(constructors.size())));
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
                    new Class[] { type.asClass() },
                    new ProxyInvocationHandler(specimenFactory, specimens));
        }

        return manufacture(type, specimens);
    }

    private <T> T manufacture(final SpecimenType<T> type, final Map<String, ISpecimen<?>> specimens) {
        var constructed = type.getDeclaredConstructors()
                .stream()
                .map(constructor -> constructOrNull(type, constructor))
                .filter(Objects::nonNull)
                .collect(toList());

        if (!constructed.isEmpty()) {
            return constructed.get(random.nextInt(constructed.size()));
        }

        var factoryCreated = type.getFactoryMethods()
                .stream()
                .map(x -> (T) manufactureOrNull(x))
                .filter(Objects::nonNull)
                .collect(toList());

        if (!factoryCreated.isEmpty()) {
            return factoryCreated.get(random.nextInt(factoryCreated.size()));
        }

        return createProxyForAbstract(type, specimens);
    }

    private <T> T construct(final SpecimenType<T> type, final Constructor<?> constructor) {
        try {
            constructor.setAccessible(true);
            return (T) constructor.newInstance(stream(constructor.getGenericParameterTypes())
                    .map(t -> specimenFactory.build(SpecimenType.fromClass(t)))
                    .map(s -> s.create())
                    .toArray());
        } catch (Exception e) {
            throw new SpecimenException(format("Unable to construct class %s with constructor %s: %s", type.asClass(), constructor.toString(), e.getMessage()), e);
        }
    }

    private <T> T constructOrNull(final SpecimenType<T> type, final Constructor<?> constructor) {
        try {
            return construct(type, constructor);
        } catch (SpecimenException ignored) {
            return null;
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

    private <T> T manufactureOrNull(final Method method) {
        try {
            return (T) method.invoke(stream(method.getGenericParameterTypes())
                    .map(t -> specimenFactory.build(SpecimenType.fromClass(t)))
                    .map(s -> s.create())
                    .toArray());
        } catch (Exception ex) {
            return null;
        }
    }
}
