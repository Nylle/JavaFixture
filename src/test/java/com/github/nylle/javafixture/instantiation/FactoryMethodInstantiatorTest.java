package com.github.nylle.javafixture.instantiation;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.factorymethod.FactoryMethodWithArgument;
import com.github.nylle.javafixture.testobjects.factorymethod.FactoryMethodWithGenericArgument;
import com.github.nylle.javafixture.testobjects.factorymethod.FactoryMethodWithoutArgument;
import com.github.nylle.javafixture.testobjects.factorymethod.GenericClassWithFactoryMethodWithoutArgument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.Optional;

import static com.github.nylle.javafixture.CustomizationContext.noContext;
import static com.github.nylle.javafixture.SpecimenType.fromClass;
import static org.assertj.core.api.Assertions.assertThat;

class FactoryMethodInstantiatorTest {

    @Test
    @DisplayName("returns instance of class using factory method without arguments")
    void factoryMethodWithoutArgument() throws NoSuchMethodException {
        var sut = FactoryMethodInstantiator.<FactoryMethodWithoutArgument>create(FactoryMethodWithoutArgument.class.getDeclaredMethod("factoryMethod"), fromClass(FactoryMethodWithoutArgument.class));

        var actual = sut.invoke(new SpecimenFactory(new Context(Configuration.configure())), noContext());

        assertThat(actual.getValue().getValue()).isEqualTo(42);
    }

    @Test
    @DisplayName("returns instance of class using factory method with arguments")
    void returnsInstanceOfClassUsingFactoryMethodWithArguments() throws NoSuchMethodException {
        var sut = FactoryMethodInstantiator.<FactoryMethodWithArgument>create(FactoryMethodWithArgument.class.getDeclaredMethod("factoryMethod", int.class), fromClass(FactoryMethodWithArgument.class));

        var actual = sut.invoke(new SpecimenFactory(new Context(Configuration.configure())), noContext());

        assertThat(actual.getValue().getValue()).isNotNull();
    }

    @Test
    @DisplayName("returns instance of class using factory method with generic argument")
    void factoryMethodWithGenericArgument() throws NoSuchMethodException {
        var sut = FactoryMethodInstantiator.create(FactoryMethodWithGenericArgument.class.getDeclaredMethod("factoryMethod", Object.class), new SpecimenType<FactoryMethodWithGenericArgument<Integer>>() {});

        var actual = sut.invoke(new SpecimenFactory(new Context(Configuration.configure())), noContext());

        assertThat(actual.getValue().getValue()).isNotNull();
    }

    @Test
    @DisplayName("returns instance of abstract class using factory method without arguments")
    void returnsInstanceOfAbstractClassUsingFactoryMethod() throws NoSuchMethodException {
        var sut = FactoryMethodInstantiator.create(Charset.class.getDeclaredMethod("defaultCharset"), new SpecimenType<Charset>() {});

        var actual = sut.invoke(new SpecimenFactory(new Context(Configuration.configure())), noContext());

        assertThat(actual.getValue()).isInstanceOf(Charset.class);
    }

    @Test
    @DisplayName("returns instance of Optional using factory method with arguments")
    void createOptionalWithArgument() throws NoSuchMethodException {
        var sut = FactoryMethodInstantiator.create(Optional.class.getDeclaredMethod("of", Object.class), new SpecimenType<Optional<String>>() {});

        var actual = sut.invoke(new SpecimenFactory(new Context(Configuration.configure())), noContext());

        assertThat(actual.getValue()).isInstanceOf(Optional.class);
        assertThat(actual.getValue()).isNotEmpty();
        assertThat(actual.getValue().get()).isInstanceOf(String.class);
    }

    @Test
    @DisplayName("returns instance of Optional using factory method without arguments")
    void createOptionalWithoutArgument() throws NoSuchMethodException {
        var sut = FactoryMethodInstantiator.create(Optional.class.getDeclaredMethod("empty"), new SpecimenType<Optional<String>>() {});

        var actual = sut.invoke(new SpecimenFactory(new Context(Configuration.configure())), noContext());

        assertThat(actual.getValue()).isInstanceOf(Optional.class);
        assertThat(actual.getValue()).isEmpty();
    }

    @Test
    @DisplayName("returns instance of generic class using generic method without arguments")
    void genericNoArgumentFactoryMethod() throws NoSuchMethodException {
        var sut = FactoryMethodInstantiator.create(GenericClassWithFactoryMethodWithoutArgument.class.getDeclaredMethod("factoryMethod"), new SpecimenType<GenericClassWithFactoryMethodWithoutArgument<Integer>>() {});

        var actual = sut.invoke(new SpecimenFactory(new Context(Configuration.configure())), noContext());

        assertThat(actual.getValue()).isNotNull();
        assertThat(actual.getValue().getValue()).isEqualTo(42);
    }
}
