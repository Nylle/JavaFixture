package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.TestInterface;
import com.github.nylle.javafixture.testobjects.TestObject;
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

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(TestInterface.class);
        assertThat(actual.publicField).isInstanceOf(Integer.class);
        assertThat(actual.publicField).isEqualTo(1);

        assertThat(actual.toString()).isInstanceOf(String.class);
        assertThat(actual.getTestObject()).isInstanceOf(TestObject.class);
    }

    @Test
    void resultIsCached() {

        var original = new InterfaceSpecimen<TestInterface>(SpecimenType.fromClass(TestInterface.class), context, specimenFactory).create(noContext(), new Annotation[0]);
        var cached = new InterfaceSpecimen<TestInterface>(SpecimenType.fromClass(TestInterface.class), context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(original).isInstanceOf(TestInterface.class);
        assertThat(original).isSameAs(cached);
        assertThat(original.toString()).isEqualTo(cached.toString());
        assertThat(original.getTestObject()).isSameAs(cached.getTestObject());
    }
}

