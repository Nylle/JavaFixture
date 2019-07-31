package com.github.nylle.javafixture.parameterized;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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

}