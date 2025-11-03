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
import com.github.nylle.javafixture.testobjects.inheritance.Child;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.nylle.javafixture.CustomizationContext.noContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ObjectSpecimenTest {

    private SpecimenFactory specimenFactory;
    private Context context;

    @BeforeEach
    void setup() {
        context = new Context(new Configuration(2, 2, 3));
        specimenFactory = new SpecimenFactory(context);
    }

    @Nested
    class WhenConstructing {

        @Test
        void onlyObjectTypesAreAllowed() {
            assertThatThrownBy(() -> new ObjectSpecimen<>(SpecimenType.fromClass(Map.class), context, specimenFactory))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("type: " + Map.class.getName());
        }

        @Test
        void typeIsRequired() {
            assertThatThrownBy(() -> new ObjectSpecimen<>(null, context, specimenFactory))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("type: null");
        }

        @Test
        void contextIsRequired() {
            assertThatThrownBy(() -> new ObjectSpecimen<>(SpecimenType.fromClass(Object.class), null, specimenFactory))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("context: null");
        }

        @Test
        void specimenFactoryIsRequired() {
            assertThatThrownBy(() -> new ObjectSpecimen<>(SpecimenType.fromClass(Object.class), context, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("specimenFactory: null");
        }
    }

    @Test
    void create() {
        var sut = new ObjectSpecimen<TestObject>(SpecimenType.fromClass(TestObject.class), context, specimenFactory);

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(TestObject.class);
        assertThat(actual.getValue()).isInstanceOf(String.class);

        assertThat(actual.getIntegers()).isInstanceOf(ArrayList.class);
        assertThat(actual.getIntegers().size()).isEqualTo(2);
        assertThat(actual.getIntegers().get(0)).isInstanceOf(Integer.class);

        assertThat(actual.getStrings()).isInstanceOf(HashMap.class);
        assertThat(actual.getStrings().size()).isEqualTo(2);

        var first = (Map.Entry) actual.getStrings().entrySet().iterator().next();
        assertThat(first.getKey()).isExactlyInstanceOf(Integer.class);
        assertThat(first.getValue()).isExactlyInstanceOf(String.class);

        var second = (Map.Entry) actual.getStrings().entrySet().iterator().next();
        assertThat(second.getKey()).isExactlyInstanceOf(Integer.class);
        assertThat(second.getValue()).isExactlyInstanceOf(String.class);
    }

    @DisplayName("when creating objects, they are not cached, regardless of the creation technique")
    @TestWithCases
    @TestCase(bool1 = true)
    @TestCase(bool1 = false)
    void resultIsNotCached(boolean useRandomConstructor) {

        var customizationContext = new CustomizationContext(List.of(), Map.of(), useRandomConstructor);
        var original = new ObjectSpecimen<TestObject>(SpecimenType.fromClass(TestObject.class), context, specimenFactory).create(customizationContext, new Annotation[0]);
        var second = new ObjectSpecimen<TestObject>(SpecimenType.fromClass(TestObject.class), context, specimenFactory).create(customizationContext, new Annotation[0]);

        assertThat(original).isInstanceOf(TestObject.class);
        assertThat(original).isNotEqualTo(second);
        assertThat(original.getValue()).isNotEqualTo(second.getValue());
        assertThat(original.getIntegers()).isNotEqualTo(second.getIntegers());
    }

    @Test
    void revertToConstructorIfReflectionFails() {

        var actual = new ObjectSpecimen<Throwable>(new SpecimenType<>() {}, context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(Throwable.class);
    }

    @Test
    void ignoresStaticFields() {

        var actual = new ObjectSpecimen<TestObject>(SpecimenType.fromClass(TestObject.class), context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(actual.STATIC_FIELD).isEqualTo("unchanged");
    }

    @Test
    void cannotSetNonExistingField() {
        var sut = new ObjectSpecimen<TestObject>(SpecimenType.fromClass(TestObject.class), context, specimenFactory);

        var customizationContext = new CustomizationContext(List.of(), Map.of("nonExistingField", "foo"), false);

        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> sut.create(customizationContext, new Annotation[0]))
                .withMessage("Cannot customize field 'nonExistingField': Field not found in class 'com.github.nylle.javafixture.testobjects.TestObject'.")
                .withNoCause();
    }

    @Test
    void cannotOmitNonExistingField() {
        var sut = new ObjectSpecimen<TestObject>(SpecimenType.fromClass(TestObject.class), context, specimenFactory);

        var customizationContext = new CustomizationContext(List.of("nonExistingField"), Map.of(), false);

        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> sut.create(customizationContext, new Annotation[0]))
                .withMessage("Cannot customize field 'nonExistingField': Field not found in class 'com.github.nylle.javafixture.testobjects.TestObject'.")
                .withNoCause();
    }

    @Test
    void customFieldIsOnlyUsedInTopLevelObject() {
        var sut = new ObjectSpecimen<WithTestObject>(SpecimenType.fromClass(WithTestObject.class), context, specimenFactory);
        var customizationContext = new CustomizationContext(List.of(), Map.of("topLevelValue", 42), false);
        var actual = sut.create(customizationContext, new Annotation[0]);
        assertThat(actual.getTopLevelValue()).isEqualTo(42);
        assertThat(actual.getTestObject()).isNotNull();
        assertThat(actual.getTestObject().getValue()).isNotNull();
        assertThat(actual.getTestObject().getStrings()).isNotEmpty();
        assertThat(actual.getTestObject().getIntegers()).isNotEmpty();
    }

    @Test
    void canCustomizeFieldInNestedObject() {
        var sut = new ObjectSpecimen<WithTestObject>(SpecimenType.fromClass(WithTestObject.class), context, specimenFactory);
        var customizationContext = new CustomizationContext(List.of("testObject.integers"), Map.of("testObject.value", "42"), false);
        var actual = sut.create(customizationContext, new Annotation[0]);
        assertThat(actual.getTestObject()).isNotNull();
        assertThat(actual.getTestObject().getValue()).isEqualTo("42");
        assertThat(actual.getTestObject().getIntegers()).isNull();
        assertThat(actual.getTestObject().getStrings()).isNotEmpty();
    }

    @Nested
    @DisplayName("when specimen has superclass")
    class WhenInheritance {

        @Test
        @DisplayName("all fields are random")
        void allFieldsArePopulated() {
            var sut = new ObjectSpecimen<Child>(SpecimenType.fromClass(Child.class), context, specimenFactory);

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
            var sut = new ObjectSpecimen<Child>(SpecimenType.fromClass(Child.class), context, specimenFactory);

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
        @DisplayName("fields with duplicate names cannot be customized")
        void firstFieldPerNameIsCustomized() {
            var sut = new ObjectSpecimen<Child>(SpecimenType.fromClass(Child.class), context, specimenFactory);

            Map<String, Object> customization = Map.of(
                    "fieldIn3Classes", "foo",
                    "fieldIn2Classes", 100.0);

            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.create(new CustomizationContext(List.of(), customization, false), new Annotation[0]));
        }

        @Test
        @DisplayName("fields with duplicate names cannot be customized")
        void firstFieldPerNameIsOmitted() {
            var sut = new ObjectSpecimen<Child>(SpecimenType.fromClass(Child.class), context, specimenFactory);

            var omitting = List.of(
                    "fieldIn3Classes",
                    "fieldIn2Classes");

            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.create(new CustomizationContext(omitting, Map.of(), false), new Annotation[0]));
        }
    }

    public static class WithTestObject {
        private int topLevelValue;
        private TestObject testObject;

        public int getTopLevelValue() {
            return topLevelValue;
        }

        public TestObject getTestObject() {
            return testObject;
        }
    }
}

