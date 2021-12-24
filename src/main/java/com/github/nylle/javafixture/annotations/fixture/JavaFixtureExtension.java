package com.github.nylle.javafixture.annotations.fixture;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Fixture;
import com.github.nylle.javafixture.SpecimenType;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class JavaFixtureExtension implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if(!extensionContext.getRequiredTestMethod().isAnnotationPresent(FixturedTest.class))
            return false;
        return parameterContext.getParameter().getAnnotations().length <= 0;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        var annotation = extensionContext.getRequiredTestMethod().getAnnotation(FixturedTest.class);
        Configuration configuration = Configuration.configure()
                .collectionSizeRange(annotation.minCollectionSize(), annotation.maxCollectionSize())
                .usePositiveNumbersOnly(annotation.positiveNumbersOnly());

        return new Fixture(configuration).create(SpecimenType.fromClass(parameterContext.getParameter().getParameterizedType()));
    }

}

