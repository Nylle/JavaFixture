package com.github.nylle.javafixture;

import com.github.nylle.javafixture.testobjects.TestObject;
import com.github.nylle.javafixture.testobjects.TestPrimitive;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionHelperTest {

    @Test
    void setField() throws NoSuchFieldException {

        var instance = new TestPrimitive();
        instance.setHello("old");

        Field field = instance.getClass().getDeclaredField("hello");

        ReflectionHelper.setField(field, instance, "new");

        assertThat(instance.getHello()).isEqualTo("new");
    }

    @Test
    void setFieldByName() {

        var instance = new TestPrimitive();
        instance.setHello("old");

        ReflectionHelper.setField("hello", instance, "new");

        assertThat(instance.getHello()).isEqualTo("new");
    }

    @Test
    void unsetFieldByName() {

        var instance = new TestPrimitive();
        instance.setHello("old");
        instance.setPrimitive(99);

        ReflectionHelper.unsetField("hello", instance);
        ReflectionHelper.unsetField("primitive", instance);

        assertThat(instance.getHello()).isNull();
        assertThat(instance.getPrimitive()).isEqualTo(0);
    }

    @Test
    void isStatic() throws NoSuchFieldException {
        var field = TestObject.class.getDeclaredField("value");
        var staticField = TestObject.class.getDeclaredField("STATIC_FIELD");

        assertThat(ReflectionHelper.isStatic(staticField)).isTrue();
        assertThat(ReflectionHelper.isStatic(field)).isFalse();
    }
}
