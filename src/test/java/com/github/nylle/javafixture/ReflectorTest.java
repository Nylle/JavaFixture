package com.github.nylle.javafixture;

import com.github.nylle.javafixture.specimen.PrimitiveSpecimen;
import com.github.nylle.javafixture.testobjects.inheritance.Child;
import com.github.nylle.javafixture.testobjects.inheritance.GenericChild;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
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

            Reflector<Child> sut = new Reflector<>(new Child(), specimenFactory);

            Child actual = sut.populate(new CustomizationContext(asList(), new HashMap<>()));

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

            Reflector<Child> sut = new Reflector<>(new Child(), specimenFactory);

            Map<String, Object> customization = new HashMap<>();
            customization.put("childField", "foo");
            customization.put("parentField", "bar");
            customization.put("baseField", "baz");

            Child actual = sut.populate(new CustomizationContext(asList(), customization));

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

            Reflector<GenericChild<String>> sut = new Reflector<>(new GenericChild<String>(), specimenFactory);

            Map<String, ISpecimen<?>> specimen = new HashMap<>();
            specimen.put("T", new PrimitiveSpecimen<String>(SpecimenType.fromClass(String.class), context));

            GenericChild<String> actual = sut.populate(new CustomizationContext(asList(), new HashMap<>()), specimen);

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

            Reflector<GenericChild<String>> sut = new Reflector<>(new GenericChild<String>(), specimenFactory);

            Map<String, ISpecimen<?>> specimen = new HashMap<>();
            specimen.put("T", new PrimitiveSpecimen<String>(SpecimenType.fromClass(String.class), context));

            Map<String, Object> customization = new HashMap<>();
            customization.put("childField", "foo");
            customization.put("parentField", "bar");
            customization.put("baseField", "baz");

            GenericChild<String> actual = sut.populate(new CustomizationContext(asList(), customization), specimen);

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

            Reflector<GenericChild<String>> sut = new Reflector<>(new GenericChild<String>(), specimenFactory);

            Map<String, Object> customization = new HashMap<>();
            customization.put("baseField", "foo");

            CustomizationContext validCustomisation = new CustomizationContext(asList(), customization);

            assertThatCode(() -> sut.validateCustomization(validCustomisation, new SpecimenType<GenericChild<String>>() {}))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("with any missing fields, an exception is thrown")
        void invalidCustomization() {

            Reflector<GenericChild<String>> sut = new Reflector<>(new GenericChild<String>(), specimenFactory);

            Map<String, Object> customization = new HashMap<>();
            customization.put("nonExistingField", "foo");

            CustomizationContext invalidCustomisation = new CustomizationContext(asList(), customization);

            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.validateCustomization(invalidCustomisation, new SpecimenType<GenericChild<String>>() {}))
                    .withMessage("Cannot customize field 'nonExistingField': Field not found in class 'com.github.nylle.javafixture.testobjects.inheritance.GenericChild<java.lang.String>'.")
                    .withNoCause();
        }

        @Test
        @DisplayName("to set duplicate fields, an exception is thrown")
        void customizingDuplicateFields() {

            Reflector<GenericChild<String>> sut = new Reflector<>(new GenericChild<String>(), specimenFactory);

            Map<String, Object> customization = new HashMap<>();
            customization.put("fieldIn2Classes", 100.0);

            CustomizationContext invalidCustomisation = new CustomizationContext(asList(), customization);

            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.validateCustomization(invalidCustomisation, new SpecimenType<GenericChild<String>>() {}))
                    .withMessageContaining("Cannot customize field 'fieldIn2Classes'. Duplicate field names found:")
                    .withMessageContaining("private java.lang.Double com.github.nylle.javafixture.testobjects.inheritance.GenericParent.fieldIn2Classes")
                    .withMessageContaining("private java.lang.Integer com.github.nylle.javafixture.testobjects.inheritance.GenericBase.fieldIn2Classes")
                    .withNoCause();
        }

        @Test
        @DisplayName("to omit duplicate fields, an exception is thrown")
        void omittingDuplicateFields() {

            Reflector<GenericChild<String>> sut = new Reflector<>(new GenericChild<String>(), specimenFactory);

            List<String> omitting = asList("fieldIn2Classes");

            CustomizationContext invalidCustomisation = new CustomizationContext(omitting, new HashMap<>());

            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.validateCustomization(invalidCustomisation, new SpecimenType<GenericChild<String>>() {}))
                    .withMessageContaining("Cannot customize field 'fieldIn2Classes'. Duplicate field names found:")
                    .withMessageContaining("private java.lang.Double com.github.nylle.javafixture.testobjects.inheritance.GenericParent.fieldIn2Classes")
                    .withMessageContaining("private java.lang.Integer com.github.nylle.javafixture.testobjects.inheritance.GenericBase.fieldIn2Classes")
                    .withNoCause();
        }
    }
}
