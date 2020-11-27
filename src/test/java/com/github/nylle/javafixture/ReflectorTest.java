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

            var invalidCustomisation = new CustomizationContext(List.of(), Map.of("nonExistingField", "foo"));

            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.validateCustomization(invalidCustomisation, new SpecimenType<>() {}))
                    .withMessage("Cannot customize field 'nonExistingField': Field not found in class 'com.github.nylle.javafixture.testobjects.inheritance.GenericChild<java.lang.String>'.")
                    .withNoCause();
        }

        @Test
        @DisplayName("to set duplicate fields, an exception is thrown")
        void customizingDuplicateFields() {

            var sut = new Reflector<>(new GenericChild<String>(), specimenFactory);

            Map<String, Object> customization = Map.of(
                    "fieldIn3Classes", "foo",
                    "fieldIn2Classes", 100.0);

            var invalidCustomisation = new CustomizationContext(List.of(), customization);

            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.validateCustomization(invalidCustomisation, new SpecimenType<>() {}))
                    .withMessageContaining("Cannot customize field 'fieldIn2Classes'. Duplicate field names found:")
                    .withMessageContaining("private java.lang.Double com.github.nylle.javafixture.testobjects.inheritance.GenericParent.fieldIn2Classes")
                    .withMessageContaining("private java.lang.Integer com.github.nylle.javafixture.testobjects.inheritance.GenericBase.fieldIn2Classes")
                    .withNoCause();
        }

        @Test
        @DisplayName("to omit duplicate fields, an exception is thrown")
        void omittingDuplicateFields() {

            var sut = new Reflector<>(new GenericChild<String>(), specimenFactory);

            var omitting = List.of(
                    "fieldIn3Classes",
                    "fieldIn2Classes");

            var invalidCustomisation = new CustomizationContext(omitting, Map.of());

            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.validateCustomization(invalidCustomisation, new SpecimenType<>() {}))
                    .withMessageContaining("Cannot customize field 'fieldIn2Classes'. Duplicate field names found:")
                    .withMessageContaining("private java.lang.Double com.github.nylle.javafixture.testobjects.inheritance.GenericParent.fieldIn2Classes")
                    .withMessageContaining("private java.lang.Integer com.github.nylle.javafixture.testobjects.inheritance.GenericBase.fieldIn2Classes")
                    .withNoCause();
        }
    }
}
