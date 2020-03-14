package com.github.nylle.javafixture.extension;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.SpecimenFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Type;

import static com.github.nylle.javafixture.ReflectionHelper.castToClass;
import static com.github.nylle.javafixture.ReflectionHelper.isParameterizedType;

public class JavaFixtureExtension implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return true;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return createSpecimen(parameterContext.getParameter().getParameterizedType()).create();
    }

    private ISpecimen<?> createSpecimen(Type genericType) {
        return isParameterizedType(genericType)
                ? new SpecimenFactory(new Context(new Configuration())).build((Class<?>) castToClass(genericType), genericType)
                : new SpecimenFactory(new Context(new Configuration())).build((Class<?>) castToClass(genericType));
    }
}

