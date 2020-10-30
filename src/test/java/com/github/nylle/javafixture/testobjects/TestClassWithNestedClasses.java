package com.github.nylle.javafixture.testobjects;

import java.util.List;

public class TestClassWithNestedClasses {

    public TestClassWithNestedClasses() {
    }

    public static class NestedStaticBaseClass {
        private final String string;

        public NestedStaticBaseClass(String string) {
            this.string = string;
        }

        public String getString() {
            return string;
        }
    }

    public static class NestedStaticDerivedClass extends NestedStaticBaseClass {
        private final List<String> strings;

        public List<String> getStrings() {
            return strings;
        }

        public NestedStaticDerivedClass(List<String> strings) {
            super("DEFAULT");
            this.strings = strings;
        }
    }
}
