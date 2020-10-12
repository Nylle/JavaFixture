package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.TestObjectGeneric;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;
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
        assertThatThrownBy(() -> new GenericSpecimen<>(null, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void typeMustBeParametrized() {
        assertThatThrownBy(() -> new GenericSpecimen<>(SpecimenType.fromClass(Object.class), context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: java.lang.Object");
    }

    @Test
    void typeMustNotBeCollection() {
        assertThatThrownBy(() -> new GenericSpecimen<>(SpecimenType.fromClass(Collection.class), context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: java.util.Collection");
    }

    @Test
    void typeMustNotBeMap() {
        assertThatThrownBy(() -> new GenericSpecimen<>(SpecimenType.fromClass(Map.class), context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: java.util.Map");
    }

    @Test
    void contextIsRequired() {
        assertThatThrownBy(() -> new GenericSpecimen<>(SpecimenType.fromClass(Optional.class), null, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("context: null");
    }

    @Test
    void specimenFactoryIsRequired() {
        assertThatThrownBy(() -> new GenericSpecimen<>(SpecimenType.fromClass(Optional.class), context, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("specimenFactory: null");
    }

    @Test
    void createClass() {
        var sut = new GenericSpecimen<>(new SpecimenType<Class<String>>(){}, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(Class.class);
        assertThat(actual).isEqualTo(String.class);
    }

    @Test
    void createGeneric() {
        var sut = new GenericSpecimen<>(new SpecimenType<TestObjectGeneric<String, Integer>>(){}, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(TestObjectGeneric.class);
        assertThat(actual.getT()).isInstanceOf(String.class);
        assertThat(actual.getU()).isInstanceOf(Integer.class);
        assertThat(actual.getString()).isInstanceOf(String.class);
    }

    @Test
    void subSpecimenAreProperlyCached() {
        var result = new GenericSpecimen<>(new SpecimenType<TestObjectGeneric<Optional<String>, Optional<Integer>>>(){}, context, specimenFactory).create();

        assertThat(result.getT().get()).isInstanceOf(String.class);
        assertThat(result.getU().get()).isInstanceOf(Integer.class);
    }
}
