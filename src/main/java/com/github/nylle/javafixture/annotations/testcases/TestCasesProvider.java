package com.github.nylle.javafixture.annotations.testcases;

import com.github.nylle.javafixture.Fixture;
import com.github.nylle.javafixture.SpecimenType;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

class TestCasesProvider implements ArgumentsProvider, AnnotationConsumer<TestWithCases> {

    @Override
    public void accept(TestWithCases testWithCases) {
    }

    @Override
    public Stream<Arguments> provideArguments(ExtensionContext context) {
        var fixtures = context.getTestMethod().stream()
                .flatMap(method -> stream(method.getParameters())
                        .filter(parameter -> hasFixtureAnnotation(parameter))
                        .map(parameter -> parameter.getParameterizedType())
                        .map(type -> new Fixture().create(SpecimenType.fromClass(type))))
                .collect(toList());

        return context.getTestMethod().stream()
                .flatMap(method -> stream(method.getDeclaredAnnotations())
                        .filter(annotation -> annotation.annotationType().equals(TestCases.class))
                        .map(cases -> (TestCases) cases)
                        .flatMap(testCases -> stream(testCases.value()))
                        .map(testCase -> mapToArguments(testCase, getParameterTypes(context), fixtures)));
    }

    private static Arguments mapToArguments(TestCase testCase, List<Class<?>> parameters, List<Object> fixtures) {
        ReflectedTestCase reflectedTestCase = new ReflectedTestCase(testCase);
        return Arguments.of(Stream.concat(IntStream.range(0, parameters.size()).boxed()
                .map(i -> reflectedTestCase.getTestCaseValueFor(parameters.get(i), i)), fixtures.stream()).toArray());
    }

    private static List<Class<?>> getParameterTypes(ExtensionContext context) {
        return context.getTestMethod().stream()
                .flatMap(method -> stream(method.getParameters())
                        .filter(parameter -> !hasFixtureAnnotation(parameter))
                        .map(parameter -> parameter.getType()))
                .collect(toList());
    }

    private static boolean hasFixtureAnnotation(Parameter parameter) {
        return parameter.getAnnotation(com.github.nylle.javafixture.annotations.testcases.Fixture.class) != null;
    }
}
