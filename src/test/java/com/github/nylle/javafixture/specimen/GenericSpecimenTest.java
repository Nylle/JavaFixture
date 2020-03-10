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
        assertThatThrownBy(() -> new GenericSpecimen<>(null, context, specimenFactory, new PrimitiveSpecimen<>(int.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void typeMustBeParametrized() {
        assertThatThrownBy(() -> new GenericSpecimen<>(Object.class, context, specimenFactory, new PrimitiveSpecimen<>(int.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type does not appear to be generic: class java.lang.Object");
    }

    @Test
    void specimensAreRequired() {
        assertThatThrownBy(() -> new GenericSpecimen<>(Optional.class, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no specimens provided");

        assertThatThrownBy(() -> new GenericSpecimen<>(Optional.class, context, specimenFactory, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("specimens: null");
    }

    @Test
    void specimensMustMatchNumberOfGenericTypes() {
        assertThatThrownBy(() -> new GenericSpecimen<>(Optional.class, context, specimenFactory, new PrimitiveSpecimen<>(int.class), new PrimitiveSpecimen<>(int.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("number of type parameters (1) does not match number of provided specimens: 2");
    }

    @Test
    void contextIsRequired() {
        assertThatThrownBy(() -> new GenericSpecimen<>(Optional.class, null, specimenFactory, new PrimitiveSpecimen<>(int.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("context: null");
    }

    @Test
    void specimenFactoryIsRequired() {
        assertThatThrownBy(() -> new GenericSpecimen<>(Optional.class, context, null, new PrimitiveSpecimen<>(int.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("specimenFactory: null");
    }

    @Test
    void createClass() {
        var sut = new GenericSpecimen<>(Class.class, context, specimenFactory, new PrimitiveSpecimen<>(String.class));

        var actual = sut.create();

        assertThat(actual).isInstanceOf(Class.class);
        assertThat(actual).isEqualTo(String.class);
    }

    @Test
    void createGeneric() {
        var sut = new GenericSpecimen<>(new TestObjectGeneric<String, Integer>().getClass(), context, specimenFactory, new PrimitiveSpecimen<>(String.class), new PrimitiveSpecimen<>(Integer.class));

        var actual = sut.create();

        assertThat(actual).isInstanceOf(TestObjectGeneric.class);
        assertThat(actual.getT()).isInstanceOf(String.class);
        assertThat(actual.getU()).isInstanceOf(Integer.class);
        assertThat(actual.getString()).isInstanceOf(String.class);
    }
}