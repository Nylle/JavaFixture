package com.github.nylle.javafixture.annotations.testcases;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.assertThat;

class TestCaseTest {

    @TestWithCases
    @TestCase(str1 = "", int2 = 0)
    @TestCase(str1 = " ", int2 = 1)
    @TestCase(str1 = "foo", int2 = 3)
    @TestCase(str1 = "hello", int2 = 5)
    void testStringLength(String input, int expected) {
        assertThat(input.length()).isEqualTo(expected);
    }

    @TestWithCases
    @TestCase(class1 = ArrayList.class, class2 = List.class, bool3 = true)
    @TestCase(class1 = LinkedList.class, class2 = List.class, bool3 = true)
    @TestCase(class1 = HashSet.class, class2 = Set.class, bool3 = true)
    @TestCase(class1 = TreeSet.class, class2 = SortedSet.class, bool3 = true)
    @TestCase(class1 = HashSet.class, class2 = NavigableSet.class, bool3 = false)
    void testClassIsAssignable(Class type, Class interfaceType, boolean expected) {
        assertThat(interfaceType.isAssignableFrom(type)).isEqualTo(expected);
    }

    @Nested
    @DisplayName("verify that primitive wrapper classes are supported as method arguments")
    class PrimitiveAutoboxing {

        @TestWithCases
        @TestCase(byte1 = 1, short2 = 2, int3 = 3, long4 = 6)
        @TestCase(byte1 = 2, short2 = -2, int3 = 0, long4 = 0)
        void testIntegerTypes(Byte first, Short second, Integer third, Long sum) {
            assertThat(Long.valueOf(first + second + third)).isEqualTo(sum);
        }

        @TestWithCases
        @TestCase(float1 = 1.5f, double2 = 1.5d)
        @TestCase(float1 = 3.25f, double2 = 3.25d)
        void testFloatingPointTypes(Float first, Double second) {
            final var firstDecimal = new BigDecimal(first, new MathContext(2, RoundingMode.HALF_UP));
            final var secondDecimal = new BigDecimal(second, new MathContext(2, RoundingMode.HALF_UP));
            assertThat(firstDecimal).isEqualTo(secondDecimal);
        }

        @TestWithCases
        @TestCase(char1 = '#', str2 = "#")
        @TestCase(char1 = '$', str2 = "$")
        void testCharacterType(Character character, String characterAsString) {
            assertThat(character).isEqualTo(characterAsString.charAt(0));
        }

        @TestWithCases
        @TestCase(bool1 = true, str2 = "true")
        @TestCase(bool2 = false, str2 = "false")
        void testBooleanType(Boolean bool, String expectedBooleanValue) {
            assertThat(bool).isEqualTo(Boolean.parseBoolean(expectedBooleanValue));
        }
    }
}
