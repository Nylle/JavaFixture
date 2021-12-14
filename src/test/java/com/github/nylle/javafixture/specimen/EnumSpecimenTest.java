package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.TestEnum;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Map;

import static com.github.nylle.javafixture.Configuration.configure;
import static com.github.nylle.javafixture.Fixture.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnumSpecimenTest {

    @Test
    void typeIsRequired() {
        assertThatThrownBy(() -> new EnumSpecimen<>(null, new Context(configure())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void onlyEnumTypes() {
        assertThatThrownBy(() -> new EnumSpecimen<>(SpecimenType.fromClass(Object.class), new Context(configure())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: " + Object.class.getName());
    }

    @Test
    void contextIsRequired() {
        assertThatThrownBy(() -> new EnumSpecimen<>(SpecimenType.fromClass(TestEnum.class), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("context: null");
    }

    @Test
    void createEnum() {
        var sut = new EnumSpecimen<>(SpecimenType.fromClass(TestEnum.class), new Context(configure()));

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(TestEnum.class);
        assertThat(actual.toString()).isIn("VALUE1", "VALUE2", "VALUE3");
    }

    @Test
    void canBePredefined() {
        var expected = fixture().create(TestEnum.class);

        var context = new Context(Configuration.configure(), Map.of(SpecimenType.fromClass(TestEnum.class).hashCode(), expected));

        var sut = new EnumSpecimen<>(SpecimenType.fromClass(TestEnum.class), context);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isSameAs(expected);
    }
}
