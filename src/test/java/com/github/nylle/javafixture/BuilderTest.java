package com.github.nylle.javafixture;

import com.github.nylle.javafixture.testobjects.ClassWithBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BuilderTest {

    @Test
    void invokeReturnsBuiltObjectWithAllMethodsCalled() throws NoSuchMethodException {
        var sut = Builder.create(ClassWithBuilder.class.getMethod("builder"), SpecimenType.fromClass(ClassWithBuilder.class));

        var result = sut.invoke(new SpecimenFactory(new Context(new Configuration())), CustomizationContext.noContext());

        assertThat(result).isInstanceOf(ClassWithBuilder.class);

        var actual = (ClassWithBuilder) result;

        assertThat(actual.getNumber()).isNotNull();
        assertThat(actual.getString()).isNotNull();
    }

}
