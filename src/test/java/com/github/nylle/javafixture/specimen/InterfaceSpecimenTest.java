package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.TestObject;
import com.github.nylle.javafixture.testobjects.interfaces.InterfaceWithoutImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.nio.charset.Charset;

import static com.github.nylle.javafixture.CustomizationContext.noContext;
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
        assertThatThrownBy(() -> new InterfaceSpecimen<>(SpecimenType.fromClass(Charset.class), context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: " + Charset.class.getName());
    }

    @Test
    void typeIsRequired() {
        assertThatThrownBy(() -> new InterfaceSpecimen<>((SpecimenType) null, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void contextIsRequired() {
        assertThatThrownBy(() -> new InterfaceSpecimen<>(SpecimenType.fromClass(InterfaceWithoutImplementation.class), null, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("context: null");
    }

    @Test
    void specimenFactoryIsRequired() {
        assertThatThrownBy(() -> new InterfaceSpecimen<>(SpecimenType.fromClass(InterfaceWithoutImplementation.class), context, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("specimenFactory: null");
    }

    @Test
    void createInterface() {
        var sut = new InterfaceSpecimen<InterfaceWithoutImplementation>(SpecimenType.fromClass(InterfaceWithoutImplementation.class), context, specimenFactory);

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(InterfaceWithoutImplementation.class);
        assertThat(actual.publicField).isInstanceOf(Integer.class);
        assertThat(actual.publicField).isEqualTo(1);

        assertThat(actual.toString()).isInstanceOf(String.class);
        assertThat(actual.getTestObject()).isInstanceOf(TestObject.class);
    }

    @Test
    void resultIsNotCached() {

        var original = new InterfaceSpecimen<InterfaceWithoutImplementation>(SpecimenType.fromClass(InterfaceWithoutImplementation.class), context, specimenFactory).create(noContext(), new Annotation[0]);
        var second = new InterfaceSpecimen<InterfaceWithoutImplementation>(SpecimenType.fromClass(InterfaceWithoutImplementation.class), context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(original).isInstanceOf(InterfaceWithoutImplementation.class);
        assertThat(original).isNotEqualTo(second);
        assertThat(original.toString()).isNotEqualTo(second.toString());
        assertThat(original.getTestObject()).isNotEqualTo(second.getTestObject());
    }
}

