package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.testobjects.TestInterface;
import com.github.nylle.javafixture.testobjects.TestObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InterfaceSpecimenTest {

    private SpecimenFactory specimenFactory;
    private Context context;

    @BeforeEach
    void setup() {
        context = new Context(new Configuration(2, 2, 3));
        specimenFactory = new SpecimenFactory(context);
    }

    @Test
    void onlyInterfaceTypes() {
        assertThatThrownBy(() -> new InterfaceSpecimen<>(Map.class, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: " + Map.class.getName());
    }

    @Test
    void typeIsRequired() {
        assertThatThrownBy(() -> new InterfaceSpecimen<>(null, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void contextIsRequired() {
        assertThatThrownBy(() -> new InterfaceSpecimen<>(TestInterface.class, null, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("context: null");
    }

    @Test
    void specimenFactoryIsRequired() {
        assertThatThrownBy(() -> new InterfaceSpecimen<>(TestInterface.class, context, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("specimenFactory: null");
    }

    @Test
    void create() {
        var sut = new InterfaceSpecimen<>(TestInterface.class, context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(TestInterface.class);
        assertThat(actual.publicField).isInstanceOf(Integer.class);
        assertThat(actual.publicField).isEqualTo(1);

        assertThat(actual.toString()).isInstanceOf(String.class);
        assertThat(actual.getTestObject()).isInstanceOf(TestObject.class);
    }

    @Test
    void resultIsCached() {

        var original = new InterfaceSpecimen<>(TestInterface.class, context, specimenFactory).create();
        var cached = new InterfaceSpecimen<>(TestInterface.class, context, specimenFactory).create();

        assertThat(original).isInstanceOf(TestInterface.class);
        assertThat(original).isSameAs(cached);
        assertThat(original.toString()).isEqualTo(cached.toString());
        assertThat(original.getTestObject()).isSameAs(cached.getTestObject());
    }
}

