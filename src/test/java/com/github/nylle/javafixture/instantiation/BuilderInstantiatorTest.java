package com.github.nylle.javafixture.instantiation;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.ClassWithBuilder;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;

class BuilderInstantiatorTest {

    @Test
    void invokeReturnsBuiltObjectWithAllMethodsCalled() throws NoSuchMethodException {
        var sut = BuilderInstantiator.create(ClassWithBuilder.class.getMethod("builder"), SpecimenType.fromClass(ClassWithBuilder.class));

        var result = sut.invoke(new SpecimenFactory(new Context(new Configuration())), CustomizationContext.noContext());

        assertThat(result).isInstanceOf(ClassWithBuilder.class);

        var actual = (ClassWithBuilder) result;

        assertThat(actual.getNumber()).isNotNull();
        assertThat(actual.getString()).isNotNull();
    }

    @Test
    void builderCanBeCnfigured() throws NoSuchMethodException {
        var sut = BuilderInstantiator.create(ClassWithBuilder.class.getMethod("builder"), SpecimenType.fromClass(ClassWithBuilder.class));

        // we seem to need to explicitly cast it like this.
        BiFunction<ClassWithBuilder.Builder, Integer, ClassWithBuilder.Builder> setter = ClassWithBuilder.Builder::number;
        BiFunction<ClassWithBuilder.Builder, String, ClassWithBuilder.Builder> without = ClassWithBuilder.Builder::string;
        var result = sut
                .with(setter, 2)
                .with(without, null) // we have lambdas, i.e. no method names, so no without
                .invoke(new SpecimenFactory(new Context(new Configuration())), CustomizationContext.noContext());

        assertThat(result).isInstanceOf(ClassWithBuilder.class);


        var actual = (ClassWithBuilder) result;

        assertThat(actual.getNumber()).isEqualTo(2);
        assertThat(actual.getString()).isNull();
    }

}
