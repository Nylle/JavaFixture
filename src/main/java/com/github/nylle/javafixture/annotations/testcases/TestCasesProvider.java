package com.github.nylle.javafixture.annotations.testcases;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

class TestCasesProvider implements ArgumentsProvider, AnnotationConsumer<TestWithCases> {

    @Override
    public void accept(TestWithCases testWithCases) { }

    @Override
    public Stream<Arguments> provideArguments(ExtensionContext context) {
        return context.getTestMethod().stream().flatMap(x -> stream(x.getDeclaredAnnotations())
                .filter(annotation -> annotation.annotationType().equals(TestCases.class))
                .map(cases -> (TestCases)cases)
                .flatMap(testCases -> stream(testCases.value()))
                .map(testCase -> mapToArguments(testCase, getParameterTypes(context))));
    }

    private static Arguments mapToArguments(TestCase testCase, List<Class<?>> parameters) {
        ReflectedTestCase reflectedTestCase = new ReflectedTestCase(testCase);
        return Arguments.of(IntStream.range(0, parameters.size())
                .boxed()
                .map(i -> reflectedTestCase.getTestCaseValueFor(parameters.get(i), i))
                .toArray());
    }

    private static List<Class<?>> getParameterTypes(ExtensionContext context) {
        return context.getTestMethod()
                .stream()
                .flatMap(x -> stream(x.getParameters()).map(y -> y.getType()))
                .collect(toList());
    }
}