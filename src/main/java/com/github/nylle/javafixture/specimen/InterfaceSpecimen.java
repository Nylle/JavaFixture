package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.ReflectionHelper;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class InterfaceSpecimen<T> implements ISpecimen<T> {

    private final Class<T> type;
    private final Context context;
    private final SpecimenFactory specimenFactory;
    private final SpecimenType specimenType;

    public InterfaceSpecimen(final Class<T> type, final Context context, final SpecimenFactory specimenFactory) {

        if(type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if(!type.isInterface() || ReflectionHelper.isMap(type) || ReflectionHelper.isCollection(type)) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.context = context;
        this.specimenFactory = specimenFactory;
        this.specimenType = SpecimenType.forObject(type);
    }

    @Override
    public T create() {
        if(context.isCached(specimenType)){
            return (T) context.cached(specimenType);
        }

        return (T) context.cached(specimenType, Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new GenericInvocationHandler()));
    }

    class GenericInvocationHandler implements InvocationHandler {

        private Map<String, Object> methodResults = new HashMap<>();

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) {
            if(method.getReturnType() != void.class) {
                methodResults.computeIfAbsent(method.toString(), x -> specimenFactory.build(method.getReturnType()).create());
                return methodResults.get(method.toString());
            }

            return null;
        }
    }


}

