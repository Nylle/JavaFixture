package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.annotations.testcases.TestCase;
import com.github.nylle.javafixture.annotations.testcases.TestWithCases;
import com.github.nylle.javafixture.testobjects.TestObject;
import com.github.nylle.javafixture.testobjects.TestObjectGeneric;
import com.github.nylle.javafixture.testobjects.inheritance.GenericChild;
import com.github.nylle.javafixture.testobjects.withconstructor.TestObjectWithConstructedField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.nylle.javafixture.CustomizationContext.noContext;
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
        var sut = new GenericSpecimen<>(new SpecimenType<Class<String>>() {}, context, specimenFactory);

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(Class.class);
        assertThat(actual).isEqualTo(String.class);
    }

    @Test
    void createGeneric() {
        var sut = new GenericSpecimen<>(new SpecimenType<TestObjectGeneric<String, Integer>>() {}, context, specimenFactory);

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(TestObjectGeneric.class);
        assertThat(actual.getT()).isInstanceOf(String.class);
        assertThat(actual.getU()).isInstanceOf(Integer.class);
        assertThat(actual.getString()).isInstanceOf(String.class);
    }

    @Test
    void subSpecimenAreProperlyCached() {
        var result = new GenericSpecimen<>(new SpecimenType<TestObjectGeneric<Optional<String>, Optional<Integer>>>() {}, context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(result.getT()).isInstanceOf(Optional.class);
        assertThat(result.getU()).isInstanceOf(Optional.class);
    }

    @Test
    void constructedGenericsAreNotCached() {
        var first = new GenericSpecimen<>(new SpecimenType<Optional<Integer>>() {}, context, specimenFactory).create(noContext(), new Annotation[0]);
        var second = new GenericSpecimen<>(new SpecimenType<Optional<Integer>>() {}, context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(first).isNotEqualTo(second);

    }

    @Test
    void cannotSetNonExistingField() {
        var sut = new GenericSpecimen<>(new SpecimenType<TestObjectGeneric<String, Integer>>() {}, context, specimenFactory);

        var customizationContext = new CustomizationContext(List.of(), Map.of("nonExistingField", "foo"), false);

        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> sut.create(customizationContext, new Annotation[0]))
                .withMessage("Cannot customize field 'nonExistingField': Field not found in class 'com.github.nylle.javafixture.testobjects.TestObjectGeneric<java.lang.String, java.lang.Integer>'.")
                .withNoCause();
    }

    @Test
    void cannotOmitNonExistingField() {
        var sut = new GenericSpecimen<>(new SpecimenType<TestObjectGeneric<String, Integer>>() {}, context, specimenFactory);

        var customizationContext = new CustomizationContext(List.of("nonExistingField"), Map.of(), false);

        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> sut.create(customizationContext, new Annotation[0]))
                .withMessage("Cannot customize field 'nonExistingField': Field not found in class 'com.github.nylle.javafixture.testobjects.TestObjectGeneric<java.lang.String, java.lang.Integer>'.")
                .withNoCause();
    }

    @Test
    void customFieldIsOnlyUsedInTopLevelObject() {
        var sut = new GenericSpecimen<>(new SpecimenType<WithTestObject<Integer>>() {}, context, specimenFactory);

        var customizationContext = new CustomizationContext(List.of(), Map.of("topLevelValue", 42), false);

        var actual = sut.create(customizationContext, new Annotation[0]);

        assertThat(actual.getTopLevelValue()).isEqualTo(42);
        assertThat(actual.getTestObject()).isNotNull();
        assertThat(actual.getTestObject().getValue()).isNotNull();
        assertThat(actual.getTestObject().getStrings()).isNotEmpty();
        assertThat(actual.getTestObject().getIntegers()).isNotEmpty();
    }

    @Test
    void createdObjectsAreNotCached() {
        var sut = new GenericSpecimen<>(new SpecimenType<WithTestObject<Integer>>() {}, context, specimenFactory);
        var actual = sut.create(new CustomizationContext(List.of(), Map.of(), false), new Annotation[0]);
        var second = sut.create(new CustomizationContext(List.of(), Map.of(), false), new Annotation[0]);

        assertThat(actual).isNotEqualTo(second);
    }

    @DisplayName("objects are not cached, neither constructed nor populated")
    @TestWithCases
    @TestCase(bool1 = true)
    @TestCase(bool1 = false)
    void constructedObjectsAreNotCached(boolean useConstructor) {
        var sut = new GenericSpecimen<>(new SpecimenType<WithTestObject<TestObjectWithConstructedField>>() {}, context, specimenFactory);

        var actual = sut.create(new CustomizationContext(List.of(), Map.of(), useConstructor), new Annotation[0]);
        var second = sut.create(new CustomizationContext(List.of(), Map.of(), useConstructor), new Annotation[0]);

        assertThat(actual).isNotEqualTo(second);
    }

    @DisplayName("when generic type is an interface, we will fixture the non-default methods")
    @Test
    void interfacesAreFixtured() {
        var sut = new GenericSpecimen<>(new SpecimenType<Comparable<Integer>>() {}, context, specimenFactory);

        var actual = sut.create(new CustomizationContext(List.of(), Map.of(), false), new Annotation[0]);
        var second = sut.create(new CustomizationContext(List.of(), Map.of(), false), new Annotation[0]);

        assertThat(actual.compareTo(0))
                .as("two different fixtured objects should have two different fixtured results")
                .isNotEqualTo(second.compareTo(0));
        assertThat(actual).isNotEqualTo(second);
    }

    @Nested
    @DisplayName("when specimen has superclass")
    class WhenInheritance {

        @Test
        @DisplayName("all fields are random")
        void allFieldsArePopulated() {
            var sut = new GenericSpecimen<>(new SpecimenType<GenericChild<String>>() {}, context, specimenFactory);

            var actual = sut.create(noContext(), new Annotation[0]);

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

            var actual = sut.create(new CustomizationContext(List.of(), customization, false), new Annotation[0]);

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
            var sut = new GenericSpecimen<>(new SpecimenType<GenericChild<String>>() {}, context, specimenFactory);

            Map<String, Object> customization = Map.of(
                    "fieldIn3Classes", "foo",
                    "fieldIn2Classes", 100.0);

            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.create(new CustomizationContext(List.of(), customization, false), new Annotation[0]));
        }

        @Test
        @DisplayName("containing multiple fields of the same name, they cannot be omitted")
        void firstFieldPerNameIsOmitted() {
            var sut = new GenericSpecimen<>(new SpecimenType<GenericChild<String>>() {}, context, specimenFactory);

            var omitting = List.of(
                    "fieldIn3Classes",
                    "fieldIn2Classes");

            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.create(new CustomizationContext(omitting, Map.of(), false), new Annotation[0]));
        }
    }

    public static class WithTestObject<T> {
        private T topLevelValue;
        private TestObject testObject;

        public T getTopLevelValue() {
            return topLevelValue;
        }

        public TestObject getTestObject() {
            return testObject;
        }
    }
}
