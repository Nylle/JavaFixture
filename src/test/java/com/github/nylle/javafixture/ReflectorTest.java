package com.github.nylle.javafixture;

import com.github.nylle.javafixture.specimen.PrimitiveSpecimen;
import com.github.nylle.javafixture.testobjects.inheritance.Child;
import com.github.nylle.javafixture.testobjects.inheritance.GenericChild;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ReflectorTest {
    private SpecimenFactory specimenFactory;
    private Context context;

    @BeforeEach
    void setup() {
        context = new Context(new Configuration(2, 2, 3));
        specimenFactory = new SpecimenFactory(context);
    }

    @Nested
    @DisplayName("when populating a normal instance")
    class PopulateNormalInstance {

        @Test
        @DisplayName("all fields are random")
        void allFieldsArePopulated() {

            var sut = new Reflector<>(new Child(), specimenFactory);

            var actual = sut.populate(new CustomizationContext(List.of(), Map.of()));

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

            var sut = new Reflector<>(new Child(), specimenFactory);

            Map<String, Object> customization = Map.of(
                    "childField", "foo",
                    "parentField", "bar",
                    "baseField", "baz");

            var actual = sut.populate(new CustomizationContext(List.of(), customization));

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

            var sut = new Reflector<>(new Child(), specimenFactory);

            Map<String, Object> customization = Map.of(
                    "fieldIn3Classes", "foo",
                    "fieldIn2Classes", 100.0);

            var actual = sut.populate(new CustomizationContext(List.of(), customization));

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

            var sut = new Reflector<>(new Child(), specimenFactory);

            var omitting = List.of(
                    "fieldIn3Classes",
                    "fieldIn2Classes");

            var actual = sut.populate(new CustomizationContext(omitting, Map.of()));

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

    @Nested
    @DisplayName("when populating a generic instance")
    class PopulateGenericInstance {

        @Test
        @DisplayName("all fields are random")
        void allFieldsArePopulated() {

            var sut = new Reflector<>(new GenericChild<String>(), specimenFactory);

            Map<String, ISpecimen<?>> specimen = Map.of("T", new PrimitiveSpecimen<String>(SpecimenType.fromClass(String.class), context));

            var actual = sut.populate(new CustomizationContext(List.of(), Map.of()), specimen);

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

            var sut = new Reflector<>(new GenericChild<String>(), specimenFactory);

            Map<String, ISpecimen<?>> specimen = Map.of("T", new PrimitiveSpecimen<String>(SpecimenType.fromClass(String.class), context));

            Map<String, Object> customization = Map.of(
                    "childField", "foo",
                    "parentField", "bar",
                    "baseField", "baz");

            var actual = sut.populate(new CustomizationContext(List.of(), customization), specimen);

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

            var sut = new Reflector<>(new GenericChild<String>(), specimenFactory);

            Map<String, ISpecimen<?>> specimen = Map.of("T", new PrimitiveSpecimen<String>(SpecimenType.fromClass(String.class), context));

            Map<String, Object> customization = Map.of(
                    "fieldIn3Classes", "foo",
                    "fieldIn2Classes", 100.0);

            var actual = sut.populate(new CustomizationContext(List.of(), customization), specimen);

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

            var sut = new Reflector<>(new GenericChild<String>(), specimenFactory);

            Map<String, ISpecimen<?>> specimen = Map.of("T", new PrimitiveSpecimen<String>(SpecimenType.fromClass(String.class), context));

            var omitting = List.of(
                    "fieldIn3Classes",
                    "fieldIn2Classes");

            var actual = sut.populate(new CustomizationContext(omitting, Map.of()), specimen);

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

    @Nested
    @DisplayName("when validating a customisation")
    class ValidateCustomization {

        @Test
        @DisplayName("with existing fields, nothing happens")
        void validCustomization() {

            var sut = new Reflector<>(new GenericChild<String>(), specimenFactory);

            var validCustomisation = new CustomizationContext(List.of(), Map.of("baseField", "foo"));

            assertThatCode(() -> sut.validateCustomization(validCustomisation, new SpecimenType<>() {}))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("with any missing fields, an exception is thrown")
        void invalidCustomization() {

            var sut = new Reflector<>(new GenericChild<String>(), specimenFactory);

            var validCustomisation = new CustomizationContext(List.of(), Map.of("nonExistingField", "foo"));

            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.validateCustomization(validCustomisation, new SpecimenType<>() {}))
                    .withMessage("Cannot customize field 'nonExistingField': Field not found in class 'com.github.nylle.javafixture.testobjects.inheritance.GenericChild<java.lang.String>'.")
                    .withNoCause();
        }
    }
}
