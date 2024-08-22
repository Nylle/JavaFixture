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

public class InstanceFactory {

    private final SpecimenFactory specimenFactory;
    private final PseudoRandom random;

    private static final Map<Class<?>, Object> primitiveDefaults = Map.of(
            Boolean.TYPE, false,
            Character.TYPE, '\0',
            Byte.TYPE, (byte) 0,
            Short.TYPE, 0,
            Integer.TYPE, 0,
            Long.TYPE, 0L,
            Float.TYPE, 0.0f,
            Double.TYPE, 0.0d
    );

    public InstanceFactory(SpecimenFactory specimenFactory) {
        this.specimenFactory = specimenFactory;
        this.random = new PseudoRandom();
    }

    public <T> T construct(SpecimenType<T> type, CustomizationContext customizationContext) {
        return random.shuffled(type.getDeclaredConstructors())
                .stream()
                .filter(x -> Modifier.isPublic(x.getModifiers()))
                .findFirst()
                .map(x -> construct(type, x, customizationContext))
                .orElseGet(() -> manufacture(type, customizationContext, new SpecimenException("No public constructor found")));
    }

    public <T> T manufacture(SpecimenType<T> type, CustomizationContext customizationContext, Throwable throwable) {
        return random.shuffled(type.getFactoryMethods())
                .stream()
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> !hasSpecimenTypeAsParameter(method, type))
                .map(x -> manufactureOrNull(x, type, customizationContext))
                .filter(x -> x != null)
                .findFirst()
                .orElseThrow(() -> new SpecimenException(format("Cannot create instance of %s: no factory-method found", type.asClass()), throwable));
    }

    public <T> T instantiate(SpecimenType<T> type) {
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
        return type.isInterface()
                ? createProxyForInterface(type, specimens)
                : createProxyForAbstract(type, specimens);
    }

    public <G, T extends Collection<G>> T createCollection(final SpecimenType<T> type) {
        return type.isInterface()
                ? createCollectionFromInterfaceType(type.asClass())
                : createCollectionFromConcreteType(type);
    }

    private <T> boolean hasSpecimenTypeAsParameter(Method m, SpecimenType<T> type) {
        return stream(m.getGenericParameterTypes()).anyMatch(t -> t.getTypeName().equals(type.asClass().getName()));
    }

    private <T> T construct(final SpecimenType<T> type, final Constructor<?> constructor, CustomizationContext customizationContext) {
        try {
            constructor.setAccessible(true);
            return (T) constructor.newInstance(stream(constructor.getParameters())
                    .map(p -> createParameter(p, customizationContext))
                    .toArray());
        } catch (Exception ex) {
            return manufacture(type, customizationContext, ex);
        }
    }

    private static Object defaultValue(Class<?> type) {
        return type.isPrimitive()
                ? primitiveDefaults.get(type)
                : null;
    }

    private Object createParameter(Parameter parameter, CustomizationContext customizationContext) {
        if (customizationContext.getIgnoredFields().contains(parameter.getName())) {
            return defaultValue(parameter.getType());
        }
        if (customizationContext.getCustomFields().containsKey(parameter.getName())) {
            return customizationContext.getCustomFields().get(parameter.getName());
        }
        return specimenFactory
                .build(SpecimenType.fromClass(parameter.getParameterizedType()))
                .create(new CustomizationContext(List.of(), Map.of(), customizationContext.useRandomConstructor()), new Annotation[0]);
    }

    private <T> Object createProxyForInterface(SpecimenType<T> type, Map<String, ISpecimen<?>> specimens) {
        try {
            var proxyFactory = new ProxyFactory();
            proxyFactory.setInterfaces(new Class<?>[]{type.asClass()});
            return proxyFactory.create(new Class[0], new Object[0], new ProxyInvocationHandler(specimenFactory, specimens));
        } catch (Exception e) {
            throw new SpecimenException(format("Unable to proxy interface %s: %s", type.asClass(), e.getMessage()), e);
        }
    }

    private <T> T createProxyForAbstract(final SpecimenType<T> type, final Map<String, ISpecimen<?>> specimens) {
        try {
            var factory = new ProxyFactory();
            factory.setSuperclass(type.asClass());
            return (T) factory.create(new Class<?>[0], new Object[0], new ProxyInvocationHandler(specimenFactory, specimens));
        } catch (Exception ex) {
            throw new SpecimenException(format("Unable to create instance of abstract %s: %s", type.asClass(), ex.getMessage()), ex);
        }
    }

    private <T> T manufactureOrNull(final Method method, SpecimenType<T> type, CustomizationContext customizationContext) {
        try {
            List<Object> arguments = new ArrayList<>();
            for (int i = 0; i < method.getParameterCount(); i++) {
                var genericParameterType = method.getGenericParameterTypes()[i];
                var specimen = specimenFactory.build(type.isParameterized()
                        ? type.getGenericTypeArgument(i)
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
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new SpecimenException("Unable to create collection of type " + type.getName(), e);
        }
    }
}
