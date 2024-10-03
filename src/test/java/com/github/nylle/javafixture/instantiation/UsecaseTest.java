package com.github.nylle.javafixture.instantiation;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.withconstructor.ConstructorExceptionAndThrowingFactoryMethod;
import com.github.nylle.javafixture.testobjects.withconstructor.TestObjectWithPrivateConstructor;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class UsecaseTest {

    private final SpecimenFactory specimenFactory = new SpecimenFactory(new Context(Configuration.configure()));
    private CustomizationContext customizationContext = CustomizationContext.noContext();

    @Test
    void catchesConstructorException() {
        var type = SpecimenType.fromClass(TestClass.class);

        var actual = instanceFactory(type, customizationContext, specimenFactory);

        assertThat(actual).isNotNull();
        assertThat(((TestClass) actual).getField()).isNull();
    }

    @Test
    void collectsAllErrorMessages() {
        var type = SpecimenType.fromClass(TestObjectWithPrivateConstructor.class);

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> instanceFactory(type, customizationContext, specimenFactory))
                .withMessageContaining("class com.github.nylle.javafixture.instantiation.ConstructorInstantiator cannot access a member of class com.github.nylle.javafixture.testobjects.withconstructor.TestObjectWithPrivateConstructor with modifiers \"private\"")
                .withMessageContaining("class com.github.nylle.javafixture.instantiation.ConstructorInstantiator cannot access a member of class com.github.nylle.javafixture.testobjects.withconstructor.TestObjectWithPrivateConstructor with modifiers \"protected\"");
    }

    @Test
    void chainsErrorMessages() {
        var type = SpecimenType.fromClass(ConstructorExceptionAndThrowingFactoryMethod.class);

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> instanceFactory(type, customizationContext, specimenFactory))
                .withMessageContaining("constructor exception")
                .withMessageContaining("factory method exception");
    }

    private <T> T instanceFactory(SpecimenType<T> type, CustomizationContext customizationContext, SpecimenFactory specimenFactory) {

        var result = type.getDeclaredConstructors().stream()
                .map(x -> ConstructorInstantiator.create(x))
                .map(x -> x.invoke(specimenFactory, customizationContext))
                .collect(Collectors.toList());

        if (result.stream().allMatch(x -> x.isEmpty())) {
            var result2 = type.getFactoryMethods().stream()
                    .map(x -> FactoryMethodInstantiator.create(x, type))
                    .map(x -> x.invoke(specimenFactory, customizationContext))
                    .collect(Collectors.toList());

            if (result2.stream().anyMatch(x -> x.isPresent())) {
                return result2.stream().filter(x -> x.isPresent()).findFirst().get().getValue();
            }
            result.addAll(result2);
        }

        return result.stream()
                .filter(x -> x.isPresent())
                .findFirst()
                .map(x -> x.getValue())
                .orElseThrow(() -> new RuntimeException(result.stream().filter(x -> x.isEmpty()).map(x -> x.getMessage()).collect(Collectors.joining("\n"))));
    }


    public static class TestClass {
        private String field;

        public TestClass(String field) {
            throw new IllegalArgumentException("unknown field");
        }

        TestClass() {
        }

        public String getField() {
            return field;
        }
    }

    public static class TestClass2 {

        public static boolean exceptionThrown = false;

        public TestClass2() {
        }

        public TestClass2(String field) {
            exceptionThrown = true;
            throw new RuntimeException("unknown field");
        }
    }
}
