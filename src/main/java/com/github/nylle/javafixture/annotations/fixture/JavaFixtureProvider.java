package com.github.nylle.javafixture.annotations.fixture;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Fixture;
import com.github.nylle.javafixture.SpecimenType;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class JavaFixtureProvider implements ArgumentsProvider, AnnotationConsumer<TestWithFixture> {

    private TestWithFixture annotation;

    public JavaFixtureProvider() {
    }

    @Override
    public void accept(TestWithFixture testWithCases) {
        this.annotation = testWithCases;
    }

    @Override
    public Stream<Arguments> provideArguments(ExtensionContext context) {

        Configuration configuration = Configuration.configure()
                .collectionSizeRange(annotation.minCollectionSize(), annotation.maxCollectionSize())
                .usePositiveNumbersOnly(annotation.positiveNumbersOnly());

        return Stream.of(Arguments.of(context.getTestMethod().stream().flatMap(m -> stream(m.getGenericParameterTypes()).map(t -> new Fixture(configuration).create(SpecimenType.fromClass(t)))).toArray()));
    }
}
