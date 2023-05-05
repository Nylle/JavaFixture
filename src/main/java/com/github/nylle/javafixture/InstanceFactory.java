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
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

    public <T> T construct(final SpecimenType<T> type, CustomizationContext customizationContext) {
        var constructors = type.getDeclaredConstructors()
                .stream()
                .filter(x -> Modifier.isPublic(x.getModifiers()))
                .collect(toList());

        if (constructors.isEmpty()) {
            return manufacture(type, customizationContext);
        }

        return construct(type, constructors.get(random.nextInt(constructors.size())), customizationContext);
    }

    public <T> T manufacture(final SpecimenType<T> type, CustomizationContext customizationContext) {
        var factoryMethods = type.getFactoryMethods();
        Collections.shuffle(factoryMethods);
        var results = factoryMethods
                .stream()
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> !hasSpecimenTypeAsParameter(method, type))
                .map(x -> manufactureOrNull(x, type, customizationContext))
                .filter(x -> x != null)
                .findFirst();

        return results.orElseThrow(() -> new SpecimenException(format("Cannot manufacture %s", type.asClass())));
    }

    private <T> boolean hasSpecimenTypeAsParameter(Method m, SpecimenType<T> type) {
        return stream(m.getGenericParameterTypes())
                .anyMatch(t -> t.getTypeName().equals(type.asClass().getName()));
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

    private <T> T construct(final SpecimenType<T> type, final Constructor<?> constructor, CustomizationContext customizationContext) {
        try {
            constructor.setAccessible(true);
            return (T) constructor.newInstance(stream(constructor.getParameters())
                    .map(p -> createParameter(p, customizationContext))
                    .toArray());
        } catch (Exception e) {
            return manufacture(type, customizationContext);
        }
    }

    private Object createParameter(Parameter parameter, CustomizationContext customizationContext) {
        if (customizationContext.getCustomFields().containsKey(parameter.getName())) {
            return customizationContext.getCustomFields().remove(parameter.getName());
        }
        var specimen = specimenFactory.build(SpecimenType.fromClass(parameter.getParameterizedType()));
        return specimen.create(customizationContext, new Annotation[0]);
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

    private <T> T manufactureOrNull(final Method method, SpecimenType<T> type, CustomizationContext customizationContext) {
        try {
            List<Object> arguments = new ArrayList<>();
            for (int i = 0; i < method.getParameterCount(); i++) {
                var genericParameterType = method.getGenericParameterTypes()[i];
                var specimen = specimenFactory.build(type.isParameterized()
                        ? SpecimenType.fromClass(type.getGenericTypeArgument(i))
                        : SpecimenType.fromClass(genericParameterType));
                var o = specimen.create(customizationContext, new Annotation[0]);
                arguments.add(o);
            }
            return (T) method.invoke(null, arguments.toArray());
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
