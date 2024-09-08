package com.github.nylle.javafixture.instantiation;

import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FactoryMethod<T> implements Instantiator<T> {

    private final Method factoryMethod;
    private final SpecimenType<T> targetType;

    public FactoryMethod(Method factoryMethod, SpecimenType<T> targetType) {
        this.factoryMethod = factoryMethod;
        this.targetType = targetType;
    }

    public static <T> FactoryMethod<T> create(Method factoryMethod, SpecimenType<T> targetType) {
        return new FactoryMethod<>(factoryMethod, targetType);
    }

    public T invoke(SpecimenFactory specimenFactory, CustomizationContext customizationContext) {
        try {
            List<Object> arguments = new ArrayList<>();
            for (int i = 0; i < factoryMethod.getParameterCount(); i++) {
                var genericParameterType = factoryMethod.getGenericParameterTypes()[i];
                var specimen = specimenFactory.build(targetType.isParameterized()
                        ? targetType.getGenericTypeArgument(i)
                        : SpecimenType.fromClass(genericParameterType));
                var o = specimen.create(customizationContext, new Annotation[0]);
                arguments.add(o);
            }
            return (T) factoryMethod.invoke(null, arguments.toArray());
        } catch (Exception ex) {
            return null;
        }
    }
}
