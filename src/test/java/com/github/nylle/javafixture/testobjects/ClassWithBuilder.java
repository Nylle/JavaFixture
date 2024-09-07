package com.github.nylle.javafixture.testobjects;

public class ClassWithBuilder {
    private final String string;
    private final Integer number;

    private ClassWithBuilder(String string, Integer number) {
        this.string = string;
        this.number = number;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getString() {
        return string;
    }

    public Integer getNumber() {
        return number;
    }

    public static class Builder {
        private String string;
        private Integer number;

        public Builder string(String string) {
            this.string = string;
            return this;
        }

        public Builder number(Integer number) {
            this.number = number;
            return this;
        }

        public ClassWithBuilder build() {
            return new ClassWithBuilder(string, number);
        }
    }
}
