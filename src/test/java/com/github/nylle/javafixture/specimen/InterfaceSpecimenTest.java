package com.github.nylle.javafixture.specimen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.TestAbstractClass;
import com.github.nylle.javafixture.testobjects.TestInterface;
import com.github.nylle.javafixture.testobjects.TestObject;

class InterfaceSpecimenTest {

    private SpecimenFactory specimenFactory;
    private Context context;

    @BeforeEach
    void setup() {
        context = new Context(new Configuration(2, 2, 3));
        specimenFactory = new SpecimenFactory(context);
    }

    @Test
    void onlyAbstractTypes() {
        assertThatThrownBy(() -> new InterfaceSpecimen<>(SpecimenType.fromClass(Map.class), context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: " + Map.class.getName());
    }

    @Test
    void typeIsRequired() {
        assertThatThrownBy(() -> new InterfaceSpecimen<>((SpecimenType) null, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void contextIsRequired() {
        assertThatThrownBy(() -> new InterfaceSpecimen<>(SpecimenType.fromClass(TestInterface.class), null, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("context: null");
    }

    @Test
    void specimenFactoryIsRequired() {
        assertThatThrownBy(() -> new InterfaceSpecimen<>(SpecimenType.fromClass(TestInterface.class), context, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("specimenFactory: null");
    }

    @Test
    void createInterface() {
        var sut = new InterfaceSpecimen<TestInterface>(SpecimenType.fromClass(TestInterface.class), context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(TestInterface.class);
        assertThat(actual.publicField).isInstanceOf(Integer.class);
        assertThat(actual.publicField).isEqualTo(1);

        assertThat(actual.toString()).isInstanceOf(String.class);
        assertThat(actual.getTestObject()).isInstanceOf(TestObject.class);
    }

    @Test
    void createAbstractClass() {
        var sut = new InterfaceSpecimen<TestAbstractClass>(SpecimenType.fromClass(TestAbstractClass.class), context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(TestAbstractClass.class);
        assertThat(actual.getString()).isNotBlank();
        assertThat(actual.abstractMethod()).isNotBlank();
    }


    @Test
    void resultIsCached() {

        var original = new InterfaceSpecimen<TestInterface>(SpecimenType.fromClass(TestInterface.class), context, specimenFactory).create();
        var cached = new InterfaceSpecimen<TestInterface>(SpecimenType.fromClass(TestInterface.class), context, specimenFactory).create();

        assertThat(original).isInstanceOf(TestInterface.class);
        assertThat(original).isSameAs(cached);
        assertThat(original.toString()).isEqualTo(cached.toString());
        assertThat(original.getTestObject()).isSameAs(cached.getTestObject());
    }
}

