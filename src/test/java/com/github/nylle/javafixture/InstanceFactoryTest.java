package com.github.nylle.javafixture;

import com.github.nylle.javafixture.testobjects.TestObjectWithGenericConstructor;
import com.github.nylle.javafixture.testobjects.TestObjectWithoutDefaultConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InstanceFactoryTest {

    @Test
    void canCreateInstanceFromConstructor() {

        var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure().useRandomConstructor(true))));

        TestObjectWithoutDefaultConstructor result = sut.construct(SpecimenType.fromClass(TestObjectWithoutDefaultConstructor.class));

        assertThat(result).isInstanceOf(TestObjectWithoutDefaultConstructor.class);
        assertThat(result.getStrings()).isInstanceOf(List.class);
        assertThat(result.getStrings()).isNotEmpty();
        assertThat(result.getStrings().get(0)).isInstanceOf(String.class);
    }

    @Test
    void optionalsAreNeverPresent() {

        var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure().useRandomConstructor(true))));

        TestObjectWithGenericConstructor result = sut.construct(SpecimenType.fromClass(TestObjectWithGenericConstructor.class));

        assertThat(result).isInstanceOf(TestObjectWithGenericConstructor.class);
        assertThat(result.getInteger()).isInstanceOf(Optional.class);
        assertThat(result.getInteger()).isNotPresent();
    }

    @Test
    void fieldsNotSetByConstructorAreNull() {

        var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure().useRandomConstructor(true))));

        TestObjectWithGenericConstructor result = sut.construct(SpecimenType.fromClass(TestObjectWithGenericConstructor.class));

        assertThat(result).isInstanceOf(TestObjectWithGenericConstructor.class);
        assertThat(result.getPrivateField()).isNull();
    }
}