package com.github.nylle.javafixture;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static com.github.nylle.javafixture.ReflectionHelper.isParameterizedType;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ProxyFactory implements InvocationHandler {

    private final SpecimenFactory specimenFactory;
    private final Map<String, Object> methodResults = new HashMap<>();
    private final Map<String, ISpecimen<?>> specimens;

    private ProxyFactory(final SpecimenFactory specimenFactory, final Map<String, ISpecimen<?>> specimens) {
        this.specimenFactory = specimenFactory;
        this.specimens = specimens;
    }

    public static <T> Object create(final Class<T> type, final SpecimenFactory specimenFactory) {
        return Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new ProxyFactory(specimenFactory, new HashMap<>()));
    }

    public static <T> Object createGeneric(final Class<T> type, final SpecimenFactory specimenFactory, final Map<String, ISpecimen<?>> specimens) {
        return Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new ProxyFactory(specimenFactory, specimens));
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) {
        return method.getReturnType() == void.class
                ? null
                : methodResults.computeIfAbsent(method.toString(), key -> specimens.getOrDefault(method.getGenericReturnType().getTypeName(), resolveSpecimen(method)).create());
    }

    private ISpecimen<?> resolveSpecimen(final Method method) {
        return isParameterizedType(method.getGenericReturnType())
                ? specimenFactory.build(method.getReturnType(), stream(((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()).map(t -> getParameterClass(t)).collect(toList()))
                : specimenFactory.build(method.getReturnType());
    }

    private Class<?> getParameterClass(Type type) {
        var candidate = specimens.get(type.getTypeName());
        return candidate != null
                ? candidate.create().getClass()
                : (Class<?>) type;
    }
}
