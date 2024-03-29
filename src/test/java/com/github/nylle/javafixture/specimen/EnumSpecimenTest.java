package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.TestEnum;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static com.github.nylle.javafixture.CustomizationContext.noContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnumSpecimenTest {

    @Test
    void typeIsRequired() {
        assertThatThrownBy(() -> new EnumSpecimen<>(null ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void onlyEnumTypes() {
        assertThatThrownBy(() -> new EnumSpecimen<>(SpecimenType.fromClass(Object.class) ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: " + Object.class.getName());
    }

    @Test
    void contextIsNotRequired() {
        assertThatCode(() -> new EnumSpecimen<>(SpecimenType.fromClass(TestEnum.class) ))
                .doesNotThrowAnyException();
    }

    @Test
    void createEnum() {
        var sut = new EnumSpecimen<>(SpecimenType.fromClass(TestEnum.class) );

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(TestEnum.class);
        assertThat(actual.toString()).isIn("VALUE1", "VALUE2", "VALUE3");
    }

}
