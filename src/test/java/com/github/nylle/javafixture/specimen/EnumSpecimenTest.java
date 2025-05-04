package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.annotations.testcases.TestCase;
import com.github.nylle.javafixture.annotations.testcases.TestWithCases;
import com.github.nylle.javafixture.testobjects.TestEnum;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static com.github.nylle.javafixture.CustomizationContext.noContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnumSpecimenTest {

    @Nested
    class WhenConstructing {

        @Test
        void typeIsRequired() {
            assertThatThrownBy(() -> new EnumSpecimen<>(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("type: null");
        }

        @Test
        void onlyEnumTypes() {
            assertThatThrownBy(() -> new EnumSpecimen<>(SpecimenType.fromClass(Object.class)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("type: " + Object.class.getName());
        }

        @Test
        void contextIsNotRequired() {
            assertThatCode(() -> new EnumSpecimen<>(SpecimenType.fromClass(TestEnum.class)))
                    .doesNotThrowAnyException();
        }
    }

    @Test
    void createEnum() {
        var sut = new EnumSpecimen<>(SpecimenType.fromClass(TestEnum.class));

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(TestEnum.class);
        assertThat(actual.toString()).isIn("VALUE1", "VALUE2", "VALUE3");
    }

    @TestWithCases
    @TestCase(class1 = String.class, bool2 = false)
    @TestCase(class1 = TestEnum.class, bool2 = true)
    void supportsType(Class<?> type, boolean expected) {
        assertThat(EnumSpecimen.supportsType(SpecimenType.fromClass(type))).isEqualTo(expected);
    }

    @Nested
    class SpecTest {

        @TestWithCases
        @TestCase(class1 = String.class, bool2 = false)
        @TestCase(class1 = TestEnum.class, bool2 = true)
        void supports(Class<?> type, boolean expected) {
            assertThat(EnumSpecimen.meta().supports(SpecimenType.fromClass(type))).isEqualTo(expected);
        }

        @Test
        void createReturnsNewSpecimen() {
            assertThat(EnumSpecimen.meta().create(SpecimenType.fromClass(TestEnum.class), null, null))
                    .isInstanceOf(EnumSpecimen.class);
        }

        @Test
        void createThrows() {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> EnumSpecimen.meta().create(SpecimenType.fromClass(String.class), null, null))
                    .withMessageContaining("type: java.lang.String");
        }
    }
}
