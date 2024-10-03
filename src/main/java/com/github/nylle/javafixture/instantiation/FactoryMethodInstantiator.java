package com.github.nylle.javafixture.instantiation;

import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FactoryMethodInstantiator<T> implements Instantiator<T> {

    private Method factoryMethod;
    private final SpecimenType<T> targetType;
    private List<Method> factoryMethods;

    public FactoryMethodInstantiator(Method factoryMethod, SpecimenType<T> targetType) {
        this.factoryMethod = factoryMethod;
        this.targetType = targetType;
    }

    public static <T> FactoryMethodInstantiator<T> create(Method factoryMethod, SpecimenType<T> targetType) {
        return new FactoryMethodInstantiator<>(factoryMethod, targetType);
    }

    public FactoryMethodInstantiator(List<Method> factoryMethods, SpecimenType<T> targetType) {
        this.targetType = targetType;
        this.factoryMethods = factoryMethods;
    }

    public static <T> FactoryMethodInstantiator<T> create(SpecimenType<T> type) {
        return new FactoryMethodInstantiator<>(type.getFactoryMethods(), type);
    }

    public List<Result<T>> invokeList(SpecimenFactory specimenFactory, CustomizationContext customizationContext) {
     var results = factoryMethods.stream()
             .map( x -> invokeOne(x, specimenFactory, customizationContext))
             .collect(Collectors.toList());

     return results;
    }

    private Result<T> invokeOne(Method factoryMethod, SpecimenFactory specimenFactory, CustomizationContext customizationContext) {
        try {
            List<Object> arguments = new ArrayList<>();
            for (int i = 0; i < factoryMethod.getParameterCount(); i++) {
                var specimen = specimenFactory.build(targetType.isParameterized()
                        ? targetType.getGenericTypeArgument(i)
                        : SpecimenType.fromClass(factoryMethod.getGenericParameterTypes()[i]));
                arguments.add(specimen.create(customizationContext, new Annotation[0]));
            }
            return Result.of((T) factoryMethod.invoke(null, arguments.toArray()));
        } catch (InvocationTargetException ex) {
            return Result.empty(ex.getTargetException().getMessage());
        } catch (Exception ex) {
            return Result.empty(ex.getMessage());
        }
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
        } catch (InvocationTargetException ex) {
            return Result.empty(ex.getTargetException().getMessage());
        } catch (Exception ex) {
            return Result.empty(ex.getMessage());
        }
    }
}
