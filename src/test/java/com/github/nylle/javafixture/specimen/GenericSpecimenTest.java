package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.testobjects.TestObjectGeneric;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GenericSpecimenTest {

    private SpecimenFactory specimenFactory;
    private Context context;

    @BeforeEach
    void setup() {
        context = new Context(new Configuration(2, 2, 3));
        specimenFactory = new SpecimenFactory(context);
    }

    @Test
    void typeIsRequired() {
        assertThatThrownBy(() -> new GenericSpecimen<>(null, context, specimenFactory, Object.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void typeMustBeParametrized() {
        assertThatThrownBy(() -> new GenericSpecimen<>(Object.class, context, specimenFactory, Object.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type does not appear to be generic: class java.lang.Object");
    }

    @Test
    void typeParametersMustMatchNumberOfGenericTypes() {
        assertThatThrownBy(() -> new GenericSpecimen<>(Optional.class, context, specimenFactory, Object.class, Object.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("number of type parameters (1) does not match number of provided generic types: 2");
    }

    @Test
    void genericTypeIsRequired() {
        assertThatThrownBy(() -> new GenericSpecimen<>(Optional.class, context, specimenFactory, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("genericTypes: null");
    }

    @Test
    void contextIsRequired() {
        assertThatThrownBy(() -> new GenericSpecimen<>(Optional.class, null, specimenFactory, Object.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("context: null");
    }

    @Test
    void specimenFactoryIsRequired() {
        assertThatThrownBy(() -> new GenericSpecimen<>(Optional.class, context, null, Object.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("specimenFactory: null");
    }

    @Test
    void createClass() {
        var sut = new GenericSpecimen<>(Class.class, context, specimenFactory, String.class);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(Class.class);
        assertThat(actual).isEqualTo(String.class);
    }

    @Test
    void createGeneric() {
        var sut = new GenericSpecimen<>(TestObjectGeneric.class, context, specimenFactory, String.class, Integer.class);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(TestObjectGeneric.class);
        assertThat(actual.getT()).isInstanceOf(String.class);
        assertThat(actual.getU()).isInstanceOf(Integer.class);
    }
}