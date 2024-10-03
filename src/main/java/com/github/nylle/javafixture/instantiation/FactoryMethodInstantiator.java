package com.github.nylle.javafixture.instantiation;

import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FactoryMethodInstantiator<T> implements Instantiator<T> {

    private final Method factoryMethod;
    private final SpecimenType<T> targetType;

    public FactoryMethodInstantiator(Method factoryMethod, SpecimenType<T> targetType) {
        this.factoryMethod = factoryMethod;
        this.targetType = targetType;
    }

    public static <T> FactoryMethodInstantiator<T> create(Method factoryMethod, SpecimenType<T> targetType) {
        return new FactoryMethodInstantiator<>(factoryMethod, targetType);
    }

    public Result<T> invoke(SpecimenFactory specimenFactory, CustomizationContext customizationContext) {
        try {
            List<Object> arguments = new ArrayList<>();
            for (int i = 0; i < factoryMethod.getParameterCount(); i++) {
                var specimen = specimenFactory.build(targetType.isParameterized()
                        ? targetType.getGenericTypeArgument(i)
                        : SpecimenType.fromClass(factoryMethod.getGenericParameterTypes()[i]));
                arguments.add(specimen.create(customizationContext, new Annotation[0]));
            }
            return Result.of((T) factoryMethod.invoke(null, arguments.toArray()));
        } catch (Exception ex) {
            return Result.empty(ex.getMessage());
        }
    }
}
