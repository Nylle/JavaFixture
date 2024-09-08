package com.github.nylle.javafixture.instantiation;

import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;

public class Constructor<T> implements Instantiator<T> {

    private final java.lang.reflect.Constructor<T> constructor;

    private Constructor(java.lang.reflect.Constructor<T> constructor) {
        this.constructor = constructor;
    }

    public static <T> Constructor<T> create(java.lang.reflect.Constructor<T> constructor) {
        return new Constructor<>(constructor);
    }

    public T invoke(SpecimenFactory specimenFactory, CustomizationContext customizationContext) {
        try {
            return constructor.newInstance(stream(constructor.getParameters())
                    .map(p -> createParameter(p, specimenFactory, customizationContext))
                    .toArray());
        } catch(Exception ex) {
            return null;
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
