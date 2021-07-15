package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.TestEnum;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
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
        EnumSpecimen<TestEnum> sut = new EnumSpecimen<>(SpecimenType.fromClass(TestEnum.class), new Context(configure()));

        TestEnum actual = sut.create();

        assertThat(actual).isInstanceOf(TestEnum.class);
        assertThat(actual.toString()).isIn("VALUE1", "VALUE2", "VALUE3");
    }

    @Test
    void canBePredefined() {
        TestEnum expected = fixture().create(TestEnum.class);

        Map<Integer, Object> map = new HashMap<Integer, Object>();
        map.put(SpecimenType.fromClass(TestEnum.class).hashCode(), expected);

        Context context = new Context(Configuration.configure(), map);

        EnumSpecimen<TestEnum> sut = new EnumSpecimen<>(SpecimenType.fromClass(TestEnum.class), context);

        TestEnum actual = sut.create();

        assertThat(actual).isSameAs(expected);
    }
}
