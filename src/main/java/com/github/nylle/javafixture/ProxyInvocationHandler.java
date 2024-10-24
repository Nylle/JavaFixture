package com.github.nylle.javafixture;

import javassist.util.proxy.MethodHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static com.github.nylle.javafixture.CustomizationContext.noContext;
import static java.util.Arrays.stream;

public class ProxyInvocationHandler implements InvocationHandler, MethodHandler {

    private final SpecimenFactory specimenFactory;
    private final Map<String, Object> methodResults = new HashMap<>();
    private final Map<String, ISpecimen<?>> specimens;

    public ProxyInvocationHandler(final SpecimenFactory specimenFactory, final Map<String, ISpecimen<?>> specimens) {
        this.specimenFactory = specimenFactory;
        this.specimens = specimens;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable{
        return invokeOrFixture(proxy, method, method, args);
    }

    @Override
    public Object invoke(final Object self, final Method thisMethod, final Method proceed, final Object[] args) throws Throwable{
        return invokeOrFixture(self, thisMethod, proceed, args);
    }

    private Object invokeOrFixture(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable{
        if (Modifier.isAbstract(thisMethod.getModifiers())) {
            if (thisMethod.getReturnType() == void.class) {
                return null;
            }

            return methodResults.computeIfAbsent(
                    thisMethod.toString(),
                    key -> specimens.getOrDefault(thisMethod.getGenericReturnType().getTypeName(), resolveSpecimen(thisMethod)).create(noContext(), new Annotation[0]));
        } else {
            try {
                return proceed.invoke(self, args);
            } catch (InvocationTargetException ex) {
                throw ex.getCause();
            }
        }
    }

    private ISpecimen<?> resolveSpecimen(final Method method) {
        if (SpecimenType.isParameterized(method.getGenericReturnType())) {
            return specimenFactory.build(
                    SpecimenType.fromRawType(
                            SpecimenType.castToClass(((ParameterizedType) (method.getGenericReturnType())).getRawType()),
                            stream(((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments())
                                    .map(t -> resolveType(t))
                                    .toArray(size -> new Type[size])));
        }

        return specimenFactory.build(SpecimenType.fromClass(method.getReturnType()));
    }

    private Type resolveType(Type type) {
        if (specimens.containsKey(type.getTypeName())) {
            return specimens.get(type.getTypeName()).create(noContext(), new Annotation[0]).getClass();
        }

        return type;
    }
}
