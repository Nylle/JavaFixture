package com.github.nylle.javafixture;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Builder<T> {

    private final Method buildMethod;
    private final Method builderMethod;

    private Builder(Method builderMethod, Method buildMethod) {
        this.builderMethod = builderMethod;
        this.buildMethod = buildMethod;
    }

    public static <T> Builder<T> create(Method builderMethodCandidate, SpecimenType<T> targetType) {
        return findBuildMethod(builderMethodCandidate.getReturnType(), targetType.asClass())
                .map(x -> new Builder<T>(builderMethodCandidate, x))
                .orElse(null);
    }

    public T invoke(SpecimenFactory specimenFactory, CustomizationContext customizationContext) {
        try {
            var builder = builderMethod.invoke(null, new Object[]{});

            for (var method : findSetMethods()) {
                method.invoke(builder, new Object[]{specimenFactory.build(SpecimenType.fromClass(method.getGenericParameterTypes()[0])).create(customizationContext, new Annotation[0])});
            }

            return (T) buildMethod.invoke(builder, new Object[]{});
        } catch (InvocationTargetException | IllegalAccessException ex) {
            return null;
        }
    }

    private List<Method> findSetMethods() {
        return Stream.of(builderMethod.getReturnType().getDeclaredMethods())
                .filter(x -> !Modifier.isStatic(x.getModifiers()))
                .filter(x -> Modifier.isPublic(x.getModifiers()))
                .filter(x -> x.getParameterCount() == 1)
                .collect(toList());
    }

    private static <T> Optional<Method> findBuildMethod(Class<?> builderClass, Class<T> targetType) {
        return Stream.of(builderClass.getDeclaredMethods())
                .filter(x -> !Modifier.isStatic(x.getModifiers()))
                .filter(x -> Modifier.isPublic(x.getModifiers()))
                .filter(x -> x.getReturnType().equals(targetType))
                .filter(x -> x.getParameterCount() == 0)
                .findFirst();
    }
}
