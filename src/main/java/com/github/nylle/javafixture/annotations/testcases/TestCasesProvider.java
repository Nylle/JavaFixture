package com.github.nylle.javafixture.annotations.testcases;

import com.github.nylle.javafixture.SpecimenType;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.nylle.javafixture.Fixture.fixture;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

class TestCasesProvider implements ArgumentsProvider, AnnotationConsumer<TestWithCases> {

    @Override
    public void accept(TestWithCases testWithCases) {
    }

    @Override
    public Stream<Arguments> provideArguments(ExtensionContext context) {
        var testMethod = valid(context);

        var fixtures = testMethod.parameters()
                .filter(parameter -> parameter.getAnnotation(Fixture.class) != null)
                .map(parameter -> parameter.getParameterizedType())
                .map(type -> fixture().create(SpecimenType.fromClass(type)))
                .collect(toList());

        var parameterTypes = testMethod.parameters()
                .filter(parameter -> parameter.getAnnotation(Fixture.class) == null)
                .map(parameter -> parameter.getType())
                .collect(toList());

        return testMethod.declaredAnnotations(TestCases.class)
                        .flatMap(testCases -> stream(testCases.value()))
                        .map(testCase -> new ReflectedTestCase(testCase))
                        .map(testCase -> IntStream.range(0, parameterTypes.size()).boxed().map(i -> testCase.getTestCaseValueFor(parameterTypes.get(i), i)))
                        .map(caseValues -> Stream.concat(caseValues, fixtures.stream()))
                        .map(values -> Arguments.of(values.toArray()));
    }

    private static TestMethod valid(ExtensionContext context) {
        var testMethod = new TestMethod(context);

        var parameters = testMethod.parameters().collect(toList());

        var strictParamIndices = IntStream.range(0, parameters.size()).boxed().filter(p -> parameters.get(p).getAnnotation(Strict.class) != null).collect(toList());
        if (strictParamIndices.isEmpty()) {
            return testMethod;
        }

        var strictAndFixture = strictParamIndices.stream().filter(p -> parameters.get(p).getAnnotation(Fixture.class) != null).collect(toList());
        if (!strictAndFixture.isEmpty()) {
            throw new TestCaseException("Arguments annotated with @Fixture cannot be @Strict: " +
                                        strictAndFixture.stream()
                                                .map(i -> parameters.get(i).getName())
                                                .collect(Collectors.joining(", ")));
        }

        var strictAndNotEnum = strictParamIndices.stream().filter(p -> !parameters.get(p).getType().isEnum()).collect(toList());
        if (!strictAndNotEnum.isEmpty()) {
            throw new TestCaseException("Arguments annotated with @Strict must be of type Enum. The following arguments are not: " +
                                        strictAndNotEnum.stream()
                                                .map(i -> parameters.get(i).getName())
                                                .collect(Collectors.joining(", ")));
        }

        var testCases = testMethod.declaredAnnotations(TestCases.class)
                .flatMap(cs -> stream(cs.value()))
                .map(c -> new ReflectedTestCase(c))
                .collect(toList());

        var parameterTypes = IntStream.range(0, parameters.size()).boxed().collect(toMap(k -> k, v -> parameters.get(v).getType()));

        strictParamIndices.forEach(i -> {
            var values = testCases.stream().map(x -> x.getTestCaseValueFor(parameterTypes.get(i), i)).collect(toList());
            var uncovered = stream(((Class<? extends Enum>) parameterTypes.get(i)).getEnumConstants())
                    .filter(x -> !values.contains(x))
                    .map(x -> parameterTypes.get(i).getSimpleName() + "." + x)
                    .collect(Collectors.joining("\n  "));
            if (!uncovered.isEmpty()) {
                throw new AssertionError("@Strict requires all Enum values to be covered by test-cases. Missing values for " + parameters.get(i).getName() + ":\n  " + uncovered);
            }
        });

        return testMethod;
    }

    private static class TestMethod {

        private final List<Parameter> parameters;
        private final List<Annotation> declaredAnnotations;

        public TestMethod(ExtensionContext context) {
            this.parameters = context.getTestMethod().stream()
                    .flatMap(method -> Stream.ofNullable(method.getParameters()).flatMap(params -> stream(params)))
                    .collect(toList());
            this.declaredAnnotations = context.getTestMethod().stream()
                    .flatMap(method -> Stream.ofNullable(method.getDeclaredAnnotations()).flatMap(annotations -> stream(annotations)))
                    .collect(toList());
        }

        public Stream<Parameter> parameters() {
            return parameters.stream();
        }

        public <T extends Annotation> Stream<T> declaredAnnotations(Class<T> type) {
            return declaredAnnotations.stream()
                    .filter(x -> x.annotationType().equals(type))
                    .map(x -> (T) x);
        }
    }
}
