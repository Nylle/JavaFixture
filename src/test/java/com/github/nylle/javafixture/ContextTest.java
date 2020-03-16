package com.github.nylle.javafixture;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ContextTest {

    @Test
    void configurationIsRequired() {
        assertThatThrownBy(() -> new Context(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("configuration: null");
    }

    @Test
    void cachedReturnsSameInstanceWhenNew() {
        var sut = new Context(new Configuration());

        var expected = new TestObject("Hello!");
        var actual = sut.cached(SpecimenType.fromClass(Integer.class), expected);

        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getValue()).isEqualTo("Hello!");
    }

    @Test
    void cachedReturnsCachedInstanceWhenExisting() {
        var sut = new Context(new Configuration());

        var expected = new TestObject("Hello!");
        sut.cached(SpecimenType.fromClass(Integer.class), expected);

        var unexpected = new TestObject("World!");
        var actual = sut.cached(SpecimenType.fromClass(Integer.class), unexpected);

        assertThat(actual).isNotEqualTo(unexpected);
        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getValue()).isEqualTo("Hello!");

    }

    class TestObject {
        private final String value;

        TestObject(String value) {
            this.value = value;
        }

        String getValue() {
            return value;
        }
    }

}