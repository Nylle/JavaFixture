package com.github.nylle.javafixture;

import com.github.nylle.javafixture.testobjects.TestObject;
import com.github.nylle.javafixture.testobjects.TestPrimitive;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpecimenFieldTest {

    @Test
    void set() throws NoSuchFieldException {
        var instance = new TestPrimitive();
        instance.setHello("old");

        var sut = new SpecimenField(instance.getClass().getDeclaredField("hello"));

        sut.set(instance, "new");

        assertThat(instance.getHello()).isEqualTo("new");
    }

    @Test
    void isStatic() throws NoSuchFieldException {
        var field = new SpecimenField(TestObject.class.getDeclaredField("value"));
        var staticField = new SpecimenField(TestObject.class.getDeclaredField("STATIC_FIELD"));

        assertThat(field.isStatic()).isFalse();
        assertThat(staticField.isStatic()).isTrue();
    }
}
