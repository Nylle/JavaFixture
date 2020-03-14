package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.TestObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ObjectSpecimenTest {

    private SpecimenFactory specimenFactory;
    private Context context;

    @BeforeEach
    void setup() {
        context = new Context(new Configuration(2, 2, 3));
        specimenFactory = new SpecimenFactory(context);
    }

    @Test
    void onlyObjectTypes() {
        assertThatThrownBy(() -> new ObjectSpecimen<>(SpecimenType.fromClass(Map.class), context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: " + Map.class.getName());
    }

    @Test
    void typeIsRequired() {
        assertThatThrownBy(() -> new ObjectSpecimen<>(null, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void contextIsRequired() {
        assertThatThrownBy(() -> new ObjectSpecimen<>(SpecimenType.fromClass(Object.class), null, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("context: null");
    }

    @Test
    void specimenFactoryIsRequired() {
        assertThatThrownBy(() -> new ObjectSpecimen<>(SpecimenType.fromClass(Object.class), context, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("specimenFactory: null");
    }

    @Test
    void create() {
        var sut = new ObjectSpecimen<TestObject>(SpecimenType.fromClass(TestObject.class), context, specimenFactory);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(TestObject.class);
        assertThat(actual.getValue()).isInstanceOf(String.class);

        assertThat(actual.getIntegers()).isInstanceOf(ArrayList.class);
        assertThat(actual.getIntegers().size()).isEqualTo(2);
        assertThat(actual.getIntegers().get(0)).isInstanceOf(Integer.class);

        assertThat(actual.getStrings()).isInstanceOf(HashMap.class);
        assertThat(actual.getStrings().size()).isEqualTo(2);

        var first = (Map.Entry)actual.getStrings().entrySet().iterator().next();
        assertThat(first.getKey()).isExactlyInstanceOf(Integer.class);
        assertThat(first.getValue()).isExactlyInstanceOf(String.class);

        var second = (Map.Entry)actual.getStrings().entrySet().iterator().next();
        assertThat(second.getKey()).isExactlyInstanceOf(Integer.class);
        assertThat(second.getValue()).isExactlyInstanceOf(String.class);
    }

    @Test
    void resultIsCached() {

        var original = new ObjectSpecimen<TestObject>(SpecimenType.fromClass(TestObject.class), context, specimenFactory).create();
        var cached = new ObjectSpecimen<TestObject>(SpecimenType.fromClass(TestObject.class), context, specimenFactory).create();

        assertThat(original).isInstanceOf(TestObject.class);
        assertThat(original).isSameAs(cached);
        assertThat(original.getValue()).isEqualTo(cached.getValue());
        assertThat(original.getIntegers()).isEqualTo(cached.getIntegers());
    }

    @Test
    void ignoresStaticFields() {

        var actual = new ObjectSpecimen<TestObject>(SpecimenType.fromClass(TestObject.class), context, specimenFactory).create();

        assertThat(actual.STATIC_FIELD).isEqualTo("unchanged");
    }
}

