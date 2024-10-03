package com.github.nylle.javafixture.instantiation;

import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;

public class ConstructorInstantiator<T> implements Instantiator<T> {

    private final java.lang.reflect.Constructor<T> constructor;

    private ConstructorInstantiator(java.lang.reflect.Constructor<T> constructor) {
        this.constructor = constructor;
    }

    public static <T> ConstructorInstantiator<T> create(java.lang.reflect.Constructor<T> constructor) {
        return new ConstructorInstantiator<>(constructor);
    }

    public Result<T> invoke(SpecimenFactory specimenFactory, CustomizationContext customizationContext) {
        try {
            return Result.of(constructor.newInstance(stream(constructor.getParameters())
                    .map(p -> createParameter(p, specimenFactory, customizationContext))
                    .toArray()));
        } catch (InvocationTargetException ex) {
            return Result.empty(ex.getTargetException().getMessage());
        } catch (Exception ex) {
            return Result.empty(ex.getMessage());
        }
    }

    private static Object createParameter(Parameter parameter, SpecimenFactory specimenFactory, CustomizationContext customizationContext) {
        if (customizationContext.getIgnoredFields().contains(parameter.getName())) {
            return Primitive.defaultValue(parameter.getType());
        }
        if (customizationContext.getCustomFields().containsKey(parameter.getName())) {
            return customizationContext.getCustomFields().get(parameter.getName());
        }
        return specimenFactory
                .build(SpecimenType.fromClass(parameter.getParameterizedType()))
                .create(new CustomizationContext(List.of(), Map.of(), customizationContext.useRandomConstructor()), new Annotation[0]);
    }
}
