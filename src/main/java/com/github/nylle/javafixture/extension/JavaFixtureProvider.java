package com.github.nylle.javafixture.extension;

import static java.util.Arrays.stream;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import com.github.nylle.javafixture.JavaFixture;

public class JavaFixtureProvider implements ArgumentsProvider, AnnotationConsumer<TestWithFixture> {
    @Override
    public void accept(TestWithFixture testWithCases) { }

    @Override
    public Stream<Arguments> provideArguments(ExtensionContext context) {

        return Stream.of(Arguments.of(context.getTestMethod().stream().flatMap(m -> stream(m.getParameterTypes()).map(c -> new JavaFixture().create(c))).toArray()));

    }
}
