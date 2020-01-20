package com.github.nylle.javafixture.specimen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class GenericSpecimenTest {
    @Test
    void typeIsRequired() {
        assertThatThrownBy(() -> new GenericSpecimen<>(null, Object.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void genericTypeIsRequired() {
        assertThatThrownBy(() -> new GenericSpecimen<>(Object.class, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("genericType: null");
    }

    @Test
    void onlyGenericTypes() {
        assertThatThrownBy(() -> new GenericSpecimen<>(Object.class, Object.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type is not generic: " + Object.class.getName());
    }

    @Test
    void createClass() {
        var sut = new GenericSpecimen<>(Class.class, String.class);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(Class.class);
        assertThat(actual).isEqualTo(String.class);
    }
}