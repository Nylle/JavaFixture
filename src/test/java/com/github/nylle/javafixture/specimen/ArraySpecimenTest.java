package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.example.AccountManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArraySpecimenTest {

    private Context context;
    private SpecimenFactory specimenFactory;

    @BeforeEach
    void setup() {
        context = new Context(new Configuration(2, 2, 3));
        specimenFactory = new SpecimenFactory(context);
    }

    @Test
    void onlyArrayTypes() {
        assertThatThrownBy(() -> new ArraySpecimen<>(SpecimenType.fromClass(Map.class), context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: " + Map.class.getName());
    }

    @Test
    void typeIsRequired() {
        assertThatThrownBy(() -> new ArraySpecimen<>((SpecimenType<?>) null, context, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @Test
    void contextIsRequired() {
        assertThatThrownBy(() -> new ArraySpecimen<>(SpecimenType.fromClass(int[].class), null, specimenFactory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("context: null");
    }

    @Test
    void specimenFactoryIsRequired() {
        assertThatThrownBy(() -> new ArraySpecimen<>(SpecimenType.fromClass(int[].class), context, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("specimenFactory: null");
    }

    @Test
    void createPrimitiveArray() {
        var sut = new ArraySpecimen<int[]>(SpecimenType.fromClass(int[].class), context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(int[].class);
        assertThat(actual.length).isEqualTo(2);
    }

    @Test
    void createObjectArray() {
        var sut = new ArraySpecimen<Object[]>(SpecimenType.fromClass(Object[].class), context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(Object[].class);
        assertThat(actual.length).isEqualTo(2);
    }

    @Test
    void canHandleCircularReferences() {
        var sut = new ArraySpecimen<AccountManager[]>(SpecimenType.fromClass(AccountManager[].class), context, specimenFactory);

        var actual = sut.create(new Annotation[0]);

        assertThat(actual).isInstanceOf(AccountManager[].class);
        assertThat(actual.length).isEqualTo(2);
        assertThat(actual[0]).isInstanceOf(AccountManager.class);
        assertThat(actual[0]).isSameAs(actual[1]);
        assertThat(actual[0].getOtherAccountManagers()).isInstanceOf(AccountManager[].class);
        assertThat(actual[0].getOtherAccountManagers()).isSameAs(actual);
    }
}
