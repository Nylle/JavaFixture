package com.github.nylle.javafixture.extension;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.SpecimenFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.lang.reflect.Type;
import java.util.stream.Stream;

import static com.github.nylle.javafixture.ReflectionHelper.castToClass;
import static com.github.nylle.javafixture.ReflectionHelper.isParameterizedType;
import static java.util.Arrays.stream;

public class JavaFixtureProvider implements ArgumentsProvider, AnnotationConsumer<TestWithFixture> {
    @Override
    public void accept(TestWithFixture testWithCases) { }

    @Override
    public Stream<Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(Arguments.of(context.getTestMethod().stream().flatMap(m -> stream(m.getGenericParameterTypes()).map(t -> createSpecimen(t).create())).toArray()));
    }

    private ISpecimen<?> createSpecimen(Type genericType) {
        return isParameterizedType(genericType)
                ? new SpecimenFactory(new Context(new Configuration())).build((Class<?>) castToClass(genericType), genericType)
                : new SpecimenFactory(new Context(new Configuration())).build((Class<?>) castToClass(genericType));
    }
}
