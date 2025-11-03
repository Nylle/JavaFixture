package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.annotations.fixture.TestWithFixture;
import com.github.nylle.javafixture.testobjects.TestAbstractClass;
import com.github.nylle.javafixture.testobjects.TestObject;
import com.github.nylle.javafixture.testobjects.abstractclasses.AbstractClassWithConcreteMethod;
import com.github.nylle.javafixture.testobjects.abstractclasses.AbstractClassWithConstructorException;
import com.github.nylle.javafixture.testobjects.interfaces.InterfaceWithoutImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.nylle.javafixture.CustomizationContext.noContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AbstractSpecimenTest {

    private SpecimenFactory specimenFactory;
    private Context context;

    @BeforeEach
    void setup() {
        context = new Context(new Configuration(2, 2, 3));
        specimenFactory = new SpecimenFactory(context);
    }

    @Nested
    class WhenConstructing {

        @Test
        void onlyAbstractTypesAreAllowed() {
            assertThatThrownBy(() -> new AbstractSpecimen<>(SpecimenType.fromClass(Map.class), context, specimenFactory))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("type: " + Map.class.getName());
        }

        @Test
        void typeIsRequired() {
            assertThatThrownBy(() -> new AbstractSpecimen<>((SpecimenType) null, context, specimenFactory))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("type: null");
        }

        @Test
        void contextIsRequired() {
            assertThatThrownBy(() -> new AbstractSpecimen<>(SpecimenType.fromClass(InterfaceWithoutImplementation.class), null, specimenFactory))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("context: null");
        }

        @Test
        void specimenFactoryIsRequired() {
            assertThatThrownBy(() -> new AbstractSpecimen<>(SpecimenType.fromClass(InterfaceWithoutImplementation.class), context, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("specimenFactory: null");
        }
    }

    @Test
    void createAbstractClass() {
        var sut = new AbstractSpecimen<TestAbstractClass>(SpecimenType.fromClass(TestAbstractClass.class), context, specimenFactory);

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(TestAbstractClass.class);
        assertThat(actual.getString()).isNotBlank();
        assertThat(actual.abstractMethod()).isNotBlank();
    }

    @DisplayName("if a void method is concrete, we call it")
    @Test
    void voidMethodsAreCalledIfPublic() {
        var sut = new AbstractSpecimen<AbstractClassWithConcreteMethod>(SpecimenType.fromClass(AbstractClassWithConcreteMethod.class), context, specimenFactory);

        var actual = sut.create(noContext(), new Annotation[0]);
        List<String> willBeEmptied = new ArrayList<>(List.of("1"));
        actual.methodWithSideEffect(willBeEmptied);
        assertThat(willBeEmptied).isEmpty();
    }

    @DisplayName("if a void method throws an exception, we pass it through")
    @Test
    void voidMethodPropagatesException() {
        var sut = new AbstractSpecimen<AbstractClassWithConcreteMethod>(SpecimenType.fromClass(AbstractClassWithConcreteMethod.class), context, specimenFactory);

        var actual = sut.create(noContext(), new Annotation[0]);
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> actual.methodWithSideEffect(List.of()));
        assertThatExceptionOfType(IOException.class).isThrownBy(actual::methodThatThrowsException);
    }

    @DisplayName("if a void method is abstract, we just do nothing")
    @Test
    void voidAbstractMethodsWillJustReturnVoid() {
        var sut = new AbstractSpecimen<AbstractClassWithConcreteMethod>(SpecimenType.fromClass(AbstractClassWithConcreteMethod.class), context, specimenFactory);

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThatCode(() -> actual.abstractMethodWithoutSideEffect(List.of())).as("trying to modify List.of() would throw")
                .doesNotThrowAnyException();
    }

    @Test
    void creatingAbstractClassWithoutConstructorFallsBackToManufacturing() {
        SpecimenType<Charset> specimenType = SpecimenType.fromClass(Charset.class);
        var sut = new AbstractSpecimen<>(specimenType, context, specimenFactory);
        assertThat(context.isCached(specimenType)).isFalse();

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(Charset.class);
        assertThat(context.isCached(specimenType)).isFalse();
    }

    @Test
    void exceptionsArePassedOnToManufacturingFallback() {
        var specimenType = SpecimenType.fromClass(AbstractClassWithConstructorException.class);
        var sut = new AbstractSpecimen<>(specimenType, context, specimenFactory);

        assertThatExceptionOfType(SpecimenException.class)
                .isThrownBy(() -> sut.create(noContext(), new Annotation[0]))
                .withMessage("Cannot create instance of class com.github.nylle.javafixture.testobjects.abstractclasses.AbstractClassWithConstructorException: no factory-method found")
                .havingCause()
                .isInstanceOf(SpecimenException.class)
                .withMessage("Unable to create instance of abstract class com.github.nylle.javafixture.testobjects.abstractclasses.AbstractClassWithConstructorException: null")
                .havingCause()
                .isInstanceOf(InvocationTargetException.class)
                .havingCause()
                .isInstanceOf(IllegalArgumentException.class)
                .withMessage("expected for tests");
    }

    @Test
    void resultIsNotCached() {

        var original = new AbstractSpecimen<TestAbstractClass>(SpecimenType.fromClass(TestAbstractClass.class), context, specimenFactory).create(noContext(), new Annotation[0]);
        var second = new AbstractSpecimen<TestAbstractClass>(SpecimenType.fromClass(TestAbstractClass.class), context, specimenFactory).create(noContext(), new Annotation[0]);

        assertThat(original).isInstanceOf(TestAbstractClass.class);
        assertThat(original).isNotEqualTo(second);
        assertThat(original.hashCode()).isNotEqualTo(second.hashCode());
        assertThat(original.toString()).isNotEqualTo(second.toString());
        assertThat(original.getString()).isNotEqualTo(second.getString());
    }

    @TestWithFixture
    void canCustomizeFieldInNestedObject(String expected) {
        var sut = new AbstractSpecimen<WithTestAbstractClass>(SpecimenType.fromClass(WithTestAbstractClass.class), context, specimenFactory);
        var customizationContext = new CustomizationContext(
                List.of(),
                Map.of("testObject.value", expected, "testAbstractClass.string", expected),
                false);

        var actual = sut.create(customizationContext, new Annotation[0]);

        assertThat(actual.getTestObject()).isNotNull();
        assertThat(actual.getTestObject().getValue()).isEqualTo(expected);
        assertThat(actual.getTestObject().getIntegers()).isNotNull();
        assertThat(actual.getTestObject().getStrings()).isNotEmpty();

        assertThat(actual.getTestAbstractClass()).isNotNull();
        assertThat(actual.getTestAbstractClass().getString()).isEqualTo(expected);
    }

    @Test
    void canOmitFieldInNestedObject() {
        var sut = new AbstractSpecimen<WithTestAbstractClass>(SpecimenType.fromClass(WithTestAbstractClass.class), context, specimenFactory);
        var customizationContext = new CustomizationContext(
                List.of("testObject.value", "testAbstractClass.string"),
                Map.of(),
                false);

        var actual = sut.create(customizationContext, new Annotation[0]);

        assertThat(actual.getTestObject()).isNotNull();
        assertThat(actual.getTestObject().getValue()).isNull();
        assertThat(actual.getTestObject().getIntegers()).isNotNull();
        assertThat(actual.getTestObject().getStrings()).isNotEmpty();

        assertThat(actual.getTestAbstractClass()).isNotNull();
        assertThat(actual.getTestAbstractClass().getString()).isNull();
    }

    public abstract static class WithTestAbstractClass {
        private TestAbstractClass testAbstractClass;
        private TestObject testObject;

        public TestAbstractClass getTestAbstractClass() {
            return testAbstractClass;
        }

        public TestObject getTestObject() {
            return testObject;
        }
    }
}

