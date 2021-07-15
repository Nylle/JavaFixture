package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.TestObjectGeneric;
import com.github.nylle.javafixture.testobjects.inheritance.GenericChild;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;
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
        GenericSpecimen<Class<String>> sut = new GenericSpecimen<>(new SpecimenType<Class<String>>(){}, context, specimenFactory);

        Class<String> actual = sut.create();

        assertThat(actual).isInstanceOf(Class.class);
        assertThat(actual).isEqualTo(String.class);
    }

    @Test
    void createGeneric() {
        GenericSpecimen<TestObjectGeneric<String, Integer>> sut = new GenericSpecimen<>(new SpecimenType<TestObjectGeneric<String, Integer>>(){}, context, specimenFactory);

        TestObjectGeneric<String, Integer> actual = sut.create();

        assertThat(actual).isInstanceOf(TestObjectGeneric.class);
        assertThat(actual.getT()).isInstanceOf(String.class);
        assertThat(actual.getU()).isInstanceOf(Integer.class);
        assertThat(actual.getString()).isInstanceOf(String.class);
    }

    @Test
    void subSpecimenAreProperlyCached() {
        TestObjectGeneric<Optional<String>, Optional<Integer>> result = new GenericSpecimen<>(new SpecimenType<TestObjectGeneric<Optional<String>, Optional<Integer>>>(){}, context, specimenFactory).create();

        assertThat(result.getT().get()).isInstanceOf(String.class);
        assertThat(result.getU().get()).isInstanceOf(Integer.class);
    }

    @Test
    void cannotSetNonExistingField() {
        GenericSpecimen<TestObjectGeneric<String, Integer>> sut = new GenericSpecimen<>(new SpecimenType<TestObjectGeneric<String, Integer>>() {}, context, specimenFactory);

        HashMap<String, Object> map = new HashMap<>();
        map.put("nonExistingField", "foo");

        CustomizationContext customizationContext = new CustomizationContext(asList(), map);

        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> sut.create(customizationContext))
                .withMessage("Cannot customize field 'nonExistingField': Field not found in class 'com.github.nylle.javafixture.testobjects.TestObjectGeneric<java.lang.String, java.lang.Integer>'.")
                .withNoCause();
    }

    @Test
    void cannotOmitNonExistingField() {
        GenericSpecimen<TestObjectGeneric<String, Integer>> sut = new GenericSpecimen<>(new SpecimenType<TestObjectGeneric<String, Integer>>() {}, context, specimenFactory);

        CustomizationContext customizationContext = new CustomizationContext(asList("nonExistingField"), new HashMap<>());

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
            GenericSpecimen<GenericChild<String>> sut = new GenericSpecimen<>(new SpecimenType<GenericChild<String>>() {}, context, specimenFactory);

            GenericChild<String> actual = sut.create();

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
            GenericSpecimen<GenericChild<String>> sut = new GenericSpecimen<>(new SpecimenType<GenericChild<String>>() {}, context, specimenFactory);

            Map<String, Object> customization = new HashMap<>();
            customization.put("childField", "foo");
            customization.put("parentField", "bar");
            customization.put("baseField", "baz");

            GenericChild<String> actual = sut.create(new CustomizationContext(asList(), customization));

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
        @DisplayName("containing multiple fields of the same name, they cannot be customised")
        void firstFieldPerNameIsCustomized() {
            GenericSpecimen<GenericChild<String>> sut = new GenericSpecimen<>(new SpecimenType<GenericChild<String>>() {}, context, specimenFactory);

            Map<String, Object> customization = new HashMap<>();
            customization.put("fieldIn3Classes", "foo");
            customization.put("fieldIn2Classes", 100.0);

            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.create(new CustomizationContext(asList(), customization)));
        }

        @Test
        @DisplayName("containing multiple fields of the same name, they cannot be omitted")
        void firstFieldPerNameIsOmitted() {
            GenericSpecimen<GenericChild<String>> sut = new GenericSpecimen<>(new SpecimenType<GenericChild<String>>() {}, context, specimenFactory);

            List<String> omitting = asList(
                    "fieldIn3Classes",
                    "fieldIn2Classes");

            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.create(new CustomizationContext(omitting, new HashMap<>())));
        }
    }
}
