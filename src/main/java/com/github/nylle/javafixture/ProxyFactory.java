package com.github.nylle.javafixture;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;

public class ProxyFactory implements InvocationHandler {

    private final SpecimenFactory specimenFactory;
    private final Map<String, Object> methodResults = new HashMap<>();
    private final Map<String, ISpecimen<?>> specimens;

    private ProxyFactory(final SpecimenFactory specimenFactory, final Map<String, ISpecimen<?>> specimens) {
        this.specimenFactory = specimenFactory;
        this.specimens = specimens;
    }

    public static <T> Object create(final FixtureType<T> type, final SpecimenFactory specimenFactory) {
        return Proxy.newProxyInstance(type.asClass().getClassLoader(), new Class[]{type.asClass()}, new ProxyFactory(specimenFactory, new HashMap<>()));
    }

    public static <T> Object create(final Class<T> type, final SpecimenFactory specimenFactory, final Map<String, ISpecimen<?>> specimens) {
        return Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new ProxyFactory(specimenFactory, specimens));
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) {
        if (method.getReturnType() == void.class) {
            return null;
        }

        return methodResults.computeIfAbsent(
                method.toString(),
                key -> specimens.getOrDefault(method.getGenericReturnType().getTypeName(), resolveSpecimen(method)).create());
    }

    private ISpecimen<?> resolveSpecimen(final Method method) {
        if (FixtureType.isParameterized(method.getGenericReturnType())) {
            return specimenFactory.build(
                    FixtureType.fromRawType(
                            FixtureType.castToClass(((ParameterizedType) (method.getGenericReturnType())).getRawType()),
                            stream(((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments())
                                    .map(t -> resolveType(t))
                                    .toArray(size -> new Type[size])));
        }

        return specimenFactory.build(FixtureType.fromClass(method.getReturnType()));
    }

    private Type resolveType(Type type) {
        if (specimens.containsKey(type.getTypeName())) {
            return specimens.get(type.getTypeName()).create().getClass();
        } else {
            return type;
        }
    }
}
