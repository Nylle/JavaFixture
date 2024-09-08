package com.github.nylle.javafixture.instantiation;

import com.github.nylle.javafixture.annotations.testcases.TestCase;
import com.github.nylle.javafixture.annotations.testcases.TestWithCases;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PrimitiveTest {

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
