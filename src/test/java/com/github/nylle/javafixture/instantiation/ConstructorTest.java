package com.github.nylle.javafixture.instantiation;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.testobjects.TestObject;
import com.github.nylle.javafixture.testobjects.withconstructor.TestObjectWithConstructedField;
import com.github.nylle.javafixture.testobjects.withconstructor.TestObjectWithGenericConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ConstructorTest {

    @Test
    @DisplayName("returns instance")
    void canCreateInstanceFromConstructor() throws NoSuchMethodException {
        var sut = Constructor.create(TestObjectWithGenericConstructor.class.getConstructor(String.class, Optional.class));

        var actual = sut.invoke(new SpecimenFactory(new Context(Configuration.configure())), new CustomizationContext(List.of(), Map.of(), false));

        assertThat(actual).isInstanceOf(TestObjectWithGenericConstructor.class);
        assertThat(actual.getValue()).isInstanceOf(String.class);
        assertThat(actual.getInteger()).isInstanceOf(Optional.class);
    }

    @Test
    @DisplayName("fields not set by constructor are null")
    void fieldsNotSetByConstructorAreNull() throws NoSuchMethodException {
        var sut = Constructor.create(TestObjectWithGenericConstructor.class.getConstructor(String.class, Optional.class));

        var actual = sut.invoke(new SpecimenFactory(new Context(Configuration.configure())), new CustomizationContext(List.of(), Map.of(), false));

        assertThat(actual).isInstanceOf(TestObjectWithGenericConstructor.class);
        assertThat(actual.getPrivateField()).isNull();
    }

    @Test
    @DisplayName("arguments can be customized")
    void argumentsCanBeCustomized() throws NoSuchMethodException {
        var sut = Constructor.create(TestObject.class.getConstructor(String.class, List.class, Map.class));

        // use arg0, because .class files do not store formal parameter names by default
        var actual = sut.invoke(new SpecimenFactory(new Context(Configuration.configure())), new CustomizationContext(List.of(), Map.of("arg0", "customized"), true));

        assertThat(actual.getValue()).isEqualTo("customized");
    }

    @Test
    @DisplayName("using constructor is used for all instances")
    void usingConstructorIsRecursive() throws NoSuchMethodException {
        var sut = Constructor.create(TestObjectWithConstructedField.class.getConstructor(int.class, TestObjectWithGenericConstructor.class));

        var actual = sut.invoke(new SpecimenFactory(new Context(Configuration.configure())), new CustomizationContext(List.of(), Map.of(), true));

        assertThat(actual).isInstanceOf(TestObjectWithConstructedField.class);
        assertThat(actual.getTestObjectWithGenericConstructor().getPrivateField()).isNull();
        assertThat(actual.getNotSetByConstructor()).isNull();
    }

    @Test
    @DisplayName("customized arguments are only used for the top level object (no nested objects)")
    void constructorArgumentsAreUsedOnce() throws NoSuchMethodException {
        var sut = Constructor.create(TestObjectWithConstructedField.class.getConstructor(int.class, TestObjectWithGenericConstructor.class));

        // use arg0, because .class files do not store formal parameter names by default
        var actual = sut.invoke(new SpecimenFactory(new Context(Configuration.configure())), new CustomizationContext(List.of(), Map.of("arg0", 2), true));

        assertThat(actual.getSetByConstructor()).isEqualTo(2);
    }

    @Test
    @DisplayName("customized arguments are used for exclusion, too")
    void ignoredConstructorArgsAreRespected() throws NoSuchMethodException {
        var sut = Constructor.create(TestObjectWithConstructedField.class.getConstructor(int.class, TestObjectWithGenericConstructor.class));

        // use arg0, because .class files do not store formal parameter names by default
        var actual = sut.invoke(new SpecimenFactory(new Context(Configuration.configure())), new CustomizationContext(List.of("arg0"), Map.of(), true));

        assertThat(actual.getSetByConstructor()).isEqualTo(0);
    }
}
