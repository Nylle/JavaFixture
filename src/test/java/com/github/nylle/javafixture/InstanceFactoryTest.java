package com.github.nylle.javafixture;

import com.github.nylle.javafixture.testobjects.TestObjectWithGenericConstructor;
import com.github.nylle.javafixture.testobjects.TestObjectWithPrivateConstructor;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.Optional;

import static com.github.nylle.javafixture.SpecimenType.fromClass;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class InstanceFactoryTest {

    @Test
    void canCreateInstanceFromConstructor() {

        var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

        TestObjectWithGenericConstructor result = sut.construct(fromClass(TestObjectWithGenericConstructor.class));

        assertThat(result).isInstanceOf(TestObjectWithGenericConstructor.class);
        assertThat(result.getValue()).isInstanceOf(String.class);
        assertThat(result.getInteger()).isInstanceOf(Optional.class);
        assertThat(result.getInteger()).isPresent();
        assertThat(result.getInteger().get()).isInstanceOf(Integer.class);
    }

    @Test
    void fieldsNotSetByConstructorAreNull() {

        var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

        TestObjectWithGenericConstructor result = sut.construct(fromClass(TestObjectWithGenericConstructor.class));

        assertThat(result).isInstanceOf(TestObjectWithGenericConstructor.class);
        assertThat(result.getPrivateField()).isNull();
    }

    @Test
    void canOnlyUsePublicConstructor() {

        var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

        assertThatExceptionOfType(SpecimenException.class)
                .isThrownBy(() -> sut.construct(fromClass(TestObjectWithPrivateConstructor.class)))
                .withMessageContaining("no public constructor found")
                .withNoCause();
    }

    @Test
    void canCreateProxyForAbstract() {

        var sut = new InstanceFactory(new SpecimenFactory(new Context(Configuration.configure())));

        var actual = sut.proxy(new SpecimenType<Charset>() {});

        assertThat(actual).isInstanceOf(java.nio.charset.Charset.class);

    }
}
