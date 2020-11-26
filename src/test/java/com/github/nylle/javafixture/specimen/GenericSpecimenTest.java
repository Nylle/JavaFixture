package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.TestObjectGeneric;
import com.github.nylle.javafixture.testobjects.inheritance.GenericChild;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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

    @Test
    void cannotSetNonExistingField() {
        var sut = new GenericSpecimen<>(new SpecimenType<TestObjectGeneric<String, Integer>>() {}, context, specimenFactory);

        var customizationContext = new CustomizationContext(List.of(), Map.of("nonExistingField", "foo"));

        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> sut.create(customizationContext))
                .withMessage("Cannot customize field 'nonExistingField': Field not found in class 'com.github.nylle.javafixture.testobjects.TestObjectGeneric<java.lang.String, java.lang.Integer>'.")
                .withNoCause();
    }

    @Test
    void cannotOmitNonExistingField() {
        var sut = new GenericSpecimen<>(new SpecimenType<TestObjectGeneric<String, Integer>>() {}, context, specimenFactory);

        var customizationContext = new CustomizationContext(List.of("nonExistingField"), Map.of());

        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> sut.create(customizationContext))
                .withMessage("Cannot customize field 'nonExistingField': Field not found in class 'com.github.nylle.javafixture.testobjects.TestObjectGeneric<java.lang.String, java.lang.Integer>'.")
                .withNoCause();
    }

    @Nested
    @DisplayName("when specimen has superclass")
    class WhenInheritance {

        @Test
        @DisplayName("all fields are random")
        void allFieldsArePopulated() {
            var sut = new GenericSpecimen<>(new SpecimenType<GenericChild<String>>() {}, context, specimenFactory);

            var actual = sut.create();

            assertThat(actual.getChildField()).isNotNull();
            assertThat(actual.getParentField()).isNotNull();
            assertThat(actual.getBaseField()).isNotNull();
            assertThat(actual.getFieldIn3ClassesChild()).isNotNull();
            assertThat(actual.getFieldIn3ClassesParent()).isNotNull();
            assertThat(actual.getFieldIn3ClassesBase()).isNotNull();
            assertThat(actual.getFieldIn2ClassesParent()).isNotNull();
            assertThat(actual.getFieldIn2ClassesBase()).isNotNull();
        }

        @Test
        @DisplayName("fields across all superclasses can be customised")
        void subClassFieldsAreCustomizable() {
            var sut = new GenericSpecimen<>(new SpecimenType<GenericChild<String>>() {}, context, specimenFactory);

            Map<String, Object> customization = Map.of(
                    "childField", "foo",
                    "parentField", "bar",
                    "baseField", "baz");

            var actual = sut.create(new CustomizationContext(List.of(), customization));

            assertThat(actual.getChildField()).isEqualTo("foo");
            assertThat(actual.getParentField()).isEqualTo("bar");
            assertThat(actual.getBaseField()).isEqualTo("baz");
            assertThat(actual.getFieldIn3ClassesChild()).isNotNull();
            assertThat(actual.getFieldIn3ClassesParent()).isNotNull();
            assertThat(actual.getFieldIn3ClassesBase()).isNotNull();
            assertThat(actual.getFieldIn2ClassesParent()).isNotNull();
            assertThat(actual.getFieldIn2ClassesBase()).isNotNull();
        }

        @Test
        @DisplayName("containing multiple fields of the same name, the first is customised while the others are random")
        void firstFieldPerNameIsCustomized() {
            var sut = new GenericSpecimen<>(new SpecimenType<GenericChild<String>>() {}, context, specimenFactory);

            Map<String, Object> customization = Map.of(
                    "fieldIn3Classes", "foo",
                    "fieldIn2Classes", 100.0);

            var actual = sut.create(new CustomizationContext(List.of(), customization));

            assertThat(actual.getFieldIn3ClassesChild()).isEqualTo("foo");
            assertThat(actual.getFieldIn3ClassesParent()).isNotNull();
            assertThat(actual.getFieldIn3ClassesBase()).isNotNull();

            assertThat(actual.getFieldIn2ClassesParent()).isEqualTo(100.0);
            assertThat(actual.getFieldIn2ClassesBase()).isNotNull();

            assertThat(actual.getChildField()).isNotNull();
            assertThat(actual.getParentField()).isNotNull();
            assertThat(actual.getBaseField()).isNotNull();
        }

        @Test
        @DisplayName("containing multiple fields of the same name, the first is omitted while the others are random")
        void firstFieldPerNameIsOmitted() {
            var sut = new GenericSpecimen<>(new SpecimenType<GenericChild<String>>() {}, context, specimenFactory);

            var omitting = List.of(
                    "fieldIn3Classes",
                    "fieldIn2Classes");

            var actual = sut.create(new CustomizationContext(omitting, Map.of()));

            assertThat(actual.getFieldIn3ClassesChild()).isNull();
            assertThat(actual.getFieldIn3ClassesParent()).isNotNull();
            assertThat(actual.getFieldIn3ClassesBase()).isNotNull();

            assertThat(actual.getFieldIn2ClassesParent()).isNull();
            assertThat(actual.getFieldIn2ClassesBase()).isNotNull();

            assertThat(actual.getChildField()).isNotNull();
            assertThat(actual.getParentField()).isNotNull();
            assertThat(actual.getBaseField()).isNotNull();
        }
    }
}
