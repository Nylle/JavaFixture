package com.github.nylle.javafixture.instantiation;

import com.github.nylle.javafixture.annotations.fixture.TestWithFixture;
import com.github.nylle.javafixture.annotations.testcases.TestCase;
import com.github.nylle.javafixture.annotations.testcases.TestWithCases;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PrimitiveTest {

    @Nested
    class defaultValue {

        @Test
        void returnsDefaultBoolean() {
            assertThat(Primitive.defaultValue(boolean.class)).isEqualTo(false);
        }

        @Test
        void returnsDefaultCharacter() {
            assertThat(Primitive.defaultValue(char.class)).isEqualTo('\0');
        }

        @Test
        void returnsDefaultByte() {
            assertThat(Primitive.defaultValue(byte.class)).isEqualTo((byte) 0);
        }

        @TestWithCases
        @TestCase(class1 = short.class)
        @TestCase(class1 = int.class)
        void returnsDefaultInteger(Class<?> type) {
            assertThat(Primitive.defaultValue(type)).isEqualTo(0);
        }

        @Test
        void returnsDefaultLong() {
            assertThat(Primitive.defaultValue(long.class)).isEqualTo(0L);
        }

        @Test
        void returnsDefaultFloat() {
            assertThat(Primitive.defaultValue(float.class)).isEqualTo(0.0f);
        }

        @Test
        void returnsDefaultDouble() {
            assertThat(Primitive.defaultValue(double.class)).isEqualTo(0.0d);
        }

        @TestWithCases
        @TestCase(class1 = Boolean.class, bool2 = true)
        @TestCase(class1 = Character.class, bool2 = true)
        @TestCase(class1 = Byte.class, bool2 = true)
        @TestCase(class1 = Short.class, bool2 = true)
        @TestCase(class1 = Integer.class, bool2 = true)
        @TestCase(class1 = Long.class, bool2 = true)
        @TestCase(class1 = Float.class, bool2 = true)
        @TestCase(class1 = Double.class, bool2 = true)
        @TestCase(class1 = boolean.class, bool2 = false)
        @TestCase(class1 = char.class, bool2 = false)
        @TestCase(class1 = byte.class, bool2 = false)
        @TestCase(class1 = short.class, bool2 = false)
        @TestCase(class1 = int.class, bool2 = false)
        @TestCase(class1 = long.class, bool2 = false)
        @TestCase(class1 = float.class, bool2 = false)
        @TestCase(class1 = double.class, bool2 = false)
        void returnsNullForBoxedPrimitive(Class<?> type, boolean isNull) {
            assertThat(Primitive.defaultValue(type) == null).isEqualTo(isNull);
        }
    }

    @Nested
    class fromBoxed {

        @TestWithCases
        @TestCase(class1 = Boolean.class, class2 = boolean.class)
        @TestCase(class1 = Character.class, class2 = char.class)
        @TestCase(class1 = Byte.class, class2 = byte.class)
        @TestCase(class1 = Short.class, class2 = short.class)
        @TestCase(class1 = Integer.class, class2 = int.class)
        @TestCase(class1 = Long.class, class2 = long.class)
        @TestCase(class1 = Float.class, class2 = float.class)
        @TestCase(class1 = Double.class, class2 = double.class)
        @TestCase(class1 = boolean.class, class2 = boolean.class)
        @TestCase(class1 = char.class, class2 = char.class)
        @TestCase(class1 = byte.class, class2 = byte.class)
        @TestCase(class1 = short.class, class2 = short.class)
        @TestCase(class1 = int.class, class2 = int.class)
        @TestCase(class1 = long.class, class2 = long.class)
        @TestCase(class1 = float.class, class2 = float.class)
        @TestCase(class1 = double.class, class2 = double.class)
        void returnsPrimitiveTypeWhenBoxed(Class<?> type, Class<?> expected) {
            assertThat(Primitive.fromBoxed(type)).isEqualTo(expected);
        }
    }

    @Nested
    class parse {

        @TestWithFixture
        void returnsString(String proband) {
            assertThat(Primitive.parse(proband, String.class)).isEqualTo(proband);
        }

        @Test
        void returnsBooleanFromString() {
            assertThat(Primitive.parse("true", Boolean.class)).isTrue();
            assertThat(Primitive.parse("xxx", Boolean.class)).isFalse();
            assertThat(Primitive.parse("true", boolean.class)).isTrue();
            assertThat(Primitive.parse("xxx", boolean.class)).isFalse();
        }

        @Test
        void returnsCharacterFromString() {
            assertThat(Primitive.parse("x", Character.class)).isEqualTo('x');
            assertThat(Primitive.parse("x", char.class)).isEqualTo('x');
        }

        @Test
        void throwsWhenStringIsNotACharacter() {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> Primitive.parse("xx", Character.class))
                    .withMessage("Expected a single character: xx");
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> Primitive.parse("xx", char.class))
                    .withMessage("Expected a single character: xx");
        }

        @Test
        void returnsByteFromString() {
            assertThat(Primitive.parse("123", Byte.class)).isEqualTo((byte) 123);
            assertThat(Primitive.parse("123", byte.class)).isEqualTo((byte) 123);
        }

        @Test
        void returnsShortFromString() {
            assertThat(Primitive.parse("123", Short.class)).isEqualTo((short) 123);
            assertThat(Primitive.parse("123", short.class)).isEqualTo((short) 123);
        }

        @Test
        void returnsIntegerFromString() {
            assertThat(Primitive.parse("123", Integer.class)).isEqualTo(123);
            assertThat(Primitive.parse("123", int.class)).isEqualTo(123);
        }

        @Test
        void returnsLongFromString() {
            assertThat(Primitive.parse("123", Long.class)).isEqualTo(123L);
            assertThat(Primitive.parse("123", long.class)).isEqualTo(123L);
        }

        @Test
        void returnsFloatFromString() {
            assertThat(Primitive.parse("12.3", Float.class)).isEqualTo(12.3f);
            assertThat(Primitive.parse("12.3", float.class)).isEqualTo(12.3f);
        }

        @Test
        void returnsDoubleFromString() {
            assertThat(Primitive.parse("12.3", Double.class)).isEqualTo(12.3);
            assertThat(Primitive.parse("12.3", double.class)).isEqualTo(12.3);
        }

        @Test
        void throwsWhenTypeIsNotPrimitiveOrString() {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> Primitive.parse("xx", Optional.class))
                    .withMessage("Unsupported type: class java.util.Optional");
        }
    }
}
