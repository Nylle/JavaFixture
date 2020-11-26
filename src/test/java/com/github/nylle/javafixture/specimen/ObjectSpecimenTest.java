package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.TestObject;
import com.github.nylle.javafixture.testobjects.inheritance.Child;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    void onlyObjectTypes() {
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

    @Test
    void create() {
        var sut = new ObjectSpecimen<TestObject>(SpecimenType.fromClass(TestObject.class), context, specimenFactory);

        var actual = sut.create();

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

    @Test
    void resultIsCached() {

        var original = new ObjectSpecimen<TestObject>(SpecimenType.fromClass(TestObject.class), context, specimenFactory).create();
        var cached = new ObjectSpecimen<TestObject>(SpecimenType.fromClass(TestObject.class), context, specimenFactory).create();

        assertThat(original).isInstanceOf(TestObject.class);
        assertThat(original).isSameAs(cached);
        assertThat(original.getValue()).isEqualTo(cached.getValue());
        assertThat(original.getIntegers()).isEqualTo(cached.getIntegers());
    }

    @Test
    void ignoresStaticFields() {

        var actual = new ObjectSpecimen<TestObject>(SpecimenType.fromClass(TestObject.class), context, specimenFactory).create();

        assertThat(actual.STATIC_FIELD).isEqualTo("unchanged");
    }

    @Test
    void cannotSetNonExistingField() {
        var sut = new ObjectSpecimen<TestObject>(SpecimenType.fromClass(TestObject.class), context, specimenFactory);

        var customizationContext = new CustomizationContext(List.of(), Map.of("nonExistingField", "foo"));

        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> sut.create(customizationContext))
                .withMessage("Cannot customize field 'nonExistingField': Field not found in class 'com.github.nylle.javafixture.testobjects.TestObject'.")
                .withNoCause();
    }

    @Test
    void cannotOmitNonExistingField() {
        var sut = new ObjectSpecimen<TestObject>(SpecimenType.fromClass(TestObject.class), context, specimenFactory);

        var customizationContext = new CustomizationContext(List.of("nonExistingField"), Map.of());

        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> sut.create(customizationContext))
                .withMessage("Cannot customize field 'nonExistingField': Field not found in class 'com.github.nylle.javafixture.testobjects.TestObject'.")
                .withNoCause();
    }

    @Nested
    @DisplayName("when specimen has superclass")
    class WhenInheritance {

        @Test
        @DisplayName("all fields are random")
        void allFieldsArePopulated() {
            var sut = new ObjectSpecimen<Child>(SpecimenType.fromClass(Child.class), context, specimenFactory);

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
            var sut = new ObjectSpecimen<Child>(SpecimenType.fromClass(Child.class), context, specimenFactory);

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
            var sut = new ObjectSpecimen<Child>(SpecimenType.fromClass(Child.class), context, specimenFactory);

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
            var sut = new ObjectSpecimen<Child>(SpecimenType.fromClass(Child.class), context, specimenFactory);

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

