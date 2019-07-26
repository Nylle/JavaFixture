package com.github.nylle.javafixture.specimen;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenFactory;

class MapSpecimenTest {
    private Context context;
    private SpecimenFactory specimenFactory;

    @BeforeEach
    void setup() {
        context = new Context(new Configuration(2, 2));
        specimenFactory = new SpecimenFactory(context);
    }

    @Test
    void onlyMapTypes() {
        assertThatThrownBy(() -> new MapSpecimen<>(List.class, String.class, Object.class, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: " + List.class.getName());
    }

    @Test
    void typeIsRequired() {
        assertThatThrownBy(() -> new MapSpecimen<>(null, String.class, Object.class, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void genericKeyTypeIsRequired() {
        assertThatThrownBy(() -> new MapSpecimen<>(Map.class, null, Object.class, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("genericKeyType: null");
    }

    @Test
    void genericValueTypeIsRequired() {
        assertThatThrownBy(() -> new MapSpecimen<>(Map.class, String.class, null, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("genericValueType: null");
    }

    @Test
    void contextIsRequired() {
        assertThatThrownBy(() -> new MapSpecimen<>(Map.class, String.class, Object.class, null, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("context: null");
    }

    @Test
    void specimenFactoryIsRequired() {
        assertThatThrownBy(() -> new MapSpecimen<>(Map.class, String.class, Object.class, context, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("specimenFactory: null");
    }

}