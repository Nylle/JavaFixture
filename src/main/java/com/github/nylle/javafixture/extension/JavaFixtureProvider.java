package com.github.nylle.javafixture.extension;

import com.github.nylle.javafixture.JavaFixture;
import com.github.nylle.javafixture.generic.FixtureType;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class JavaFixtureProvider implements ArgumentsProvider, AnnotationConsumer<TestWithFixture> {
    @Override
    public void accept(TestWithFixture testWithCases) { }

    @Override
    public Stream<Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(Arguments.of(context.getTestMethod().stream().flatMap(m -> stream(m.getGenericParameterTypes()).map(t -> new JavaFixture().create(FixtureType.fromClass(t)))).toArray()));
    }
}
