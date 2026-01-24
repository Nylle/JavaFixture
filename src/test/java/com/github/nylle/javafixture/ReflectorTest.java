package com.github.nylle.javafixture;

import com.github.nylle.javafixture.testobjects.TestObjectWithJakartaValidationAnnotations;
import com.github.nylle.javafixture.testobjects.TestObjectWithJakartaValidationAnnotationsOnMethod;
import com.github.nylle.javafixture.testobjects.inheritance.GenericChild;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.validation.constraints.Size;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReflectorTest {

    @Nested
    @DisplayName("when validating a customisation")
    class ValidateCustomization {

        @Test
        @DisplayName("with existing fields, nothing happens")
        void validCustomization() {

            var sut = new Reflector<>(new GenericChild<String>(), new SpecimenType<>(){});

            var validCustomisation = new CustomizationContext(List.of(), Map.of("baseField.subField", "foo"), false);

            assertThatCode(() -> sut.validateCustomization(validCustomisation))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("with any missing fields, an exception is thrown")
        void invalidCustomization() {

            var sut = new Reflector<>(new GenericChild<String>(), new SpecimenType<>() {});

            var invalidCustomisation = new CustomizationContext(List.of(), Map.of("nonExistingField.subField", "foo"), false);

            assertThatExceptionOfType(CustomizationException.class)
                    .isThrownBy(() -> sut.validateCustomization(invalidCustomisation))
                    .withMessage("Cannot customize field 'nonExistingField': Field not found in class 'com.github.nylle.javafixture.testobjects.inheritance.GenericChild<java.lang.String>'.")
                    .withNoCause();
        }

        @Test
        @DisplayName("to set duplicate fields, an exception is thrown")
        void customizingDuplicateFields() {

            var sut = new Reflector<>(new GenericChild<String>(), new SpecimenType<>(){});

            Map<String, Object> customization = Map.of("fieldIn2Classes.subField", 100.0);

            var invalidCustomisation = new CustomizationContext(List.of(), customization, false);

            assertThatExceptionOfType(CustomizationException.class)
                    .isThrownBy(() -> sut.validateCustomization(invalidCustomisation))
                    .withMessageContaining("Cannot customize field 'fieldIn2Classes'. Duplicate field names found:")
                    .withMessageContaining("private java.lang.Double com.github.nylle.javafixture.testobjects.inheritance.GenericParent.fieldIn2Classes")
                    .withMessageContaining("private java.lang.Integer com.github.nylle.javafixture.testobjects.inheritance.GenericBase.fieldIn2Classes")
                    .withNoCause();
        }

        @Test
        @DisplayName("to omit duplicate fields, an exception is thrown")
        void omittingDuplicateFields() {

            var sut = new Reflector<>(new GenericChild<String>(), new SpecimenType<>(){});

            var omitting = List.of("fieldIn2Classes.subField");

            var invalidCustomisation = new CustomizationContext(omitting, Map.of(), false);

            assertThatExceptionOfType(CustomizationException.class)
                    .isThrownBy(() -> sut.validateCustomization(invalidCustomisation))
                    .withMessageContaining("Cannot customize field 'fieldIn2Classes'. Duplicate field names found:")
                    .withMessageContaining("private java.lang.Double com.github.nylle.javafixture.testobjects.inheritance.GenericParent.fieldIn2Classes")
                    .withMessageContaining("private java.lang.Integer com.github.nylle.javafixture.testobjects.inheritance.GenericBase.fieldIn2Classes")
                    .withNoCause();
        }
    }

    @Nested
    @DisplayName("when setting a field via reflection")
    class SetField {

        private String stringField;
        private int primitiveField;

        @DisplayName("an Object field can be set")
        @Test
        void setObject() throws Exception {
            var field = this.getClass().getDeclaredField("stringField");
            var sut = new Reflector<>(this, new SpecimenType<>(){});
            sut.setField(field, "new value");

            assertThat(stringField).isEqualTo("new value");
        }

        @DisplayName("an Object field can be set to null")
        @Test
        void setObjectToNull() throws Exception {
            stringField = "before";
            var field = this.getClass().getDeclaredField("stringField");
            var sut = new Reflector<>(this, new SpecimenType<>(){});
            sut.setField(field, null);

            assertThat(stringField).isNull();
        }

        @DisplayName("a primitive field can be set")
        @Test
        void setPrimitiveField() throws Exception {
            var field = this.getClass().getDeclaredField("primitiveField");
            var sut = new Reflector<>(this, new SpecimenType<>(){});
            sut.setField(field, 4);

            assertThat(primitiveField).isEqualTo(4);
        }

        @DisplayName("setting a primitive field to null keeps the default value")
        @Test
        void setPrimitiveFieldToNull() throws Exception {
            var field = this.getClass().getDeclaredField("primitiveField");
            var sut = new Reflector<>(this, new SpecimenType<>(){});
            sut.setField(field, null);

            assertThat(primitiveField).isEqualTo(0);
        }

        @DisplayName("an IllegalAccessException is turned into a SpecimenException")
        @Test
        void catchIllegalAccessException() throws Exception {
            var mockedField = Mockito.mock(Field.class);
            doReturn(this.getClass()).when(mockedField).getType();
            var sut = new Reflector<>("", new SpecimenType<>(){});
            doThrow(new IllegalAccessException("expected")).when(mockedField).set(any(), any());

            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.setField(mockedField, ""));
        }

        @DisplayName("a SecurityException is turned into a SpecimenException")
        @Test
        void catchSecurityException() {
            var mockedField = Mockito.mock(Field.class);
            doReturn(this.getClass()).when(mockedField).getType();
            var sut = new Reflector<>("", new SpecimenType<>(){});
            doThrow(new SecurityException("expected")).when(mockedField).setAccessible(true);
            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.setField(mockedField, ""));
        }

        @DisplayName("an InaccessibleObjectException is turned into a SpecimenException")
        @Test
        void catchInaccessibleObjectException() {
            var mockedField = Mockito.mock(Field.class);
            doReturn(this.getClass()).when(mockedField).getType();
            var sut = new Reflector<>("", new SpecimenType<>(){});
            doThrow(new InaccessibleObjectException("expected")).when(mockedField).setAccessible(true);
            assertThatExceptionOfType(SpecimenException.class)
                    .isThrownBy(() -> sut.setField(mockedField, ""));
        }
    }

    @Nested
    class GetFieldAnnotations {

        @Test
        void returnsSizeAnnotationOnPrivateField() {
            var sut = new Reflector<>(new TestObjectWithJakartaValidationAnnotations(), new SpecimenType<>(){});

            var field = sut.getDeclaredFields().filter(x -> x.getName().equals("withMinMaxAnnotation")).findFirst().get();

            var actual = Arrays.stream(sut.getFieldAnnotations(field)).collect(Collectors.toList());

            assertThat(actual).hasSize(1);
            assertThat(actual.get(0).annotationType()).isEqualTo(Size.class);
        }

        @Test
        void returnsSizeAnnotationOnGetter() {
            var sut = new Reflector<>(new TestObjectWithJakartaValidationAnnotationsOnMethod(), new SpecimenType<>(){});

            var field = sut.getDeclaredFields().filter(x -> x.getName().equals("withMinMaxAnnotation")).findFirst().get();

            var actual = Arrays.stream(sut.getFieldAnnotations(field)).collect(Collectors.toList());

            assertThat(actual).hasSize(1);
            assertThat(actual.get(0).annotationType()).isEqualTo(Size.class);
        }

        @Test
        void returnsSizeAnnotationOnFieldIfGetterThrowsIntrospectionException() {
            var sut = new Reflector<>(new TestObjectWithJakartaValidationAnnotations(), new SpecimenType<>(){});
            var expected = sut.getDeclaredFields().filter(x -> x.getName().equals("withMinMaxAnnotation")).findFirst().get().getAnnotations();

            var throwingField = mock(Field.class);
            when(throwingField.getModifiers()).thenAnswer(x -> { throw new IntrospectionException("expected for test"); });
            when(throwingField.getAnnotations()).thenReturn(expected);

            var actual = Arrays.stream(sut.getFieldAnnotations(throwingField)).collect(Collectors.toList());

            assertThat(actual).hasSize(1);
            assertThat(actual.get(0).annotationType()).isEqualTo(Size.class);
        }
    }
}
