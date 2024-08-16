package com.github.nylle.javafixture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Test
    void overwriteWillRemovePreviousValue() {
        var sut = new Context(new Configuration());

        var unexpected = new TestObject("Hello!");
        sut.cached(SpecimenType.fromClass(Integer.class), unexpected);

        var expected = new TestObject("World!");
        var actual = sut.overwrite(SpecimenType.fromClass(Integer.class), expected);

        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getValue()).isEqualTo("World!");

    }

    @DisplayName("remove")
    @Nested
    class RemoveTest {

        @DisplayName("will return null if type was not in cache")
        @Test
        void removeWillReturnNull() {
            var sut = new Context(new Configuration());

            var actual = sut.remove(new SpecimenType<>() {});

            assertThat(actual).isNull();

        }

        @DisplayName("will return instance from cache if type was in cache")
        @Test
        void returnCacheInstance() {
            var sut = new Context(new Configuration());
            var specimenType = new SpecimenType<>() {};
            var object = new Object();

            sut.cached(specimenType, object);

            assertThat(sut.isCached(specimenType)).isTrue();

            var actual = sut.remove(specimenType);

            assertThat(actual).isSameAs(object);
            assertThat(sut.isCached(specimenType)).isFalse();

        }
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
