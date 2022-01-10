package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.TestAbstractClass;
import com.github.nylle.javafixture.testobjects.TestInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.nio.charset.Charset;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AbstractSpecimenTest {

    private SpecimenFactory specimenFactory;
    private Context context;

    @BeforeEach
    void setup() {
        context = new Context(new Configuration(2, 2, 3));
        specimenFactory = new SpecimenFactory(context);
    }

    @Test
    void onlyAbstractTypes() {
        assertThatThrownBy(() -> new AbstractSpecimen<>(SpecimenType.fromClass(Map.class), context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: " + Map.class.getName());
    }

    @Test
    void typeIsRequired() {
        assertThatThrownBy(() -> new AbstractSpecimen<>((SpecimenType) null, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void contextIsRequired() {
        assertThatThrownBy(() -> new AbstractSpecimen<>(SpecimenType.fromClass(TestInterface.class), null, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("context: null");
    }

    @Test
    void specimenFactoryIsRequired() {
        assertThatThrownBy(() -> new AbstractSpecimen<>(SpecimenType.fromClass(TestInterface.class), context, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("specimenFactory: null");
    }

    @Test
    void createAbstractClass() {
        var sut = new AbstractSpecimen<TestAbstractClass>(SpecimenType.fromClass(TestAbstractClass.class), context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(TestAbstractClass.class);
        assertThat(actual.getString()).isNotBlank();
        assertThat(actual.abstractMethod()).isNotBlank();
    }

    @Test
    void createAbstractClassWithoutConstructor() {
        var sut = new AbstractSpecimen<Charset>(SpecimenType.fromClass(Charset.class), context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(Charset.class);
    }

    @Test
    void resultIsCached() {

        var original = new AbstractSpecimen<TestAbstractClass>(SpecimenType.fromClass(TestAbstractClass.class), context, specimenFactory).create(new Annotation[0]);
        var cached = new AbstractSpecimen<TestAbstractClass>(SpecimenType.fromClass(TestAbstractClass.class), context, specimenFactory).create(new Annotation[0]);

        assertThat(original).isInstanceOf(TestAbstractClass.class);
        assertThat(original).isSameAs(cached);
        assertThat(original.toString()).isEqualTo(cached.toString());
        assertThat(original.getString()).isSameAs(cached.getString());
    }
}

