package com.github.nylle.javafixture;

import com.github.nylle.javafixture.testobjects.inheritance.GenericChild;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ReflectorTest {

    @Nested
    @DisplayName("when validating a customisation")
    class ValidateCustomization {

        @Test
        @DisplayName("with existing fields, nothing happens")
        void validCustomization() {

            var sut = new Reflector<>(new GenericChild<String>());

            var validCustomisation = new CustomizationContext(List.of(), Map.of("baseField", "foo"));

            assertThatCode(() -> sut.validateCustomization(validCustomisation, new SpecimenType<>() {}))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("with any missing fields, an exception is thrown")
        void invalidCustomization() {

            var sut = new Reflector<>(new GenericChild<String>());

            var invalidCustomisation = new CustomizationContext(List.of(), Map.of("nonExistingField", "foo"));

            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.validateCustomization(invalidCustomisation, new SpecimenType<>() {}))
                    .withMessage("Cannot customize field 'nonExistingField': Field not found in class 'com.github.nylle.javafixture.testobjects.inheritance.GenericChild<java.lang.String>'.")
                    .withNoCause();
        }

        @Test
        @DisplayName("to set duplicate fields, an exception is thrown")
        void customizingDuplicateFields() {

            var sut = new Reflector<>(new GenericChild<String>());

            Map<String, Object> customization = Map.of("fieldIn2Classes", 100.0);

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

            var sut = new Reflector<>(new GenericChild<String>());

            var omitting = List.of("fieldIn2Classes");

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
