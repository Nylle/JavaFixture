package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.testobjects.TestObject;
import com.github.nylle.javafixture.testobjects.abstractclasses.AbstractClassWithImplementation;
import com.github.nylle.javafixture.testobjects.abstractclasses.AbstractClassWithImplementationImpl;
import com.github.nylle.javafixture.testobjects.abstractclasses.AbstractClassWithoutImplementation;
import com.github.nylle.javafixture.testobjects.abstractclasses.GenericAbstractClassTUWithGenericImplementationU;
import com.github.nylle.javafixture.testobjects.abstractclasses.GenericAbstractClassTUWithGenericImplementationUImpl;
import com.github.nylle.javafixture.testobjects.interfaces.GenericInterfaceTUWithGenericImplementationU;
import com.github.nylle.javafixture.testobjects.interfaces.GenericInterfaceTUWithGenericImplementationUImpl;
import com.github.nylle.javafixture.testobjects.interfaces.InterfaceWithImplementation;
import com.github.nylle.javafixture.testobjects.interfaces.InterfaceWithImplementationImpl;
import com.github.nylle.javafixture.testobjects.interfaces.InterfaceWithoutImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.github.nylle.javafixture.CustomizationContext.noContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class ExperimentalAbstractSpecimenTest {

    private SpecimenFactory specimenFactory;

    private Context context;

    @BeforeEach
    void setup() {
        context = new Context(new Configuration(2, 2, 3));
        specimenFactory = new SpecimenFactory(context);
    }

    @Nested
    @DisplayName("When constructing the specimen")
    class WhenConstructing {

        @Test
        @DisplayName("type must not be null")
        void typeIsRequired() {
            assertThatThrownBy(() -> new ExperimentalAbstractSpecimen<>(null, List.of(), context, specimenFactory))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("type: null");
        }

        @Test
        @DisplayName("derivedTypes must not be null")
        void derivedTypesIsRequired() {
            assertThatThrownBy(() -> new ExperimentalAbstractSpecimen<>(SpecimenType.fromClass(InterfaceWithImplementation.class), null, context, specimenFactory))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("derivedTypes: null");
        }

        @Test
        @DisplayName("context must not be null")
        void contextIsRequired() {
            assertThatThrownBy(() -> new ExperimentalAbstractSpecimen<>(SpecimenType.fromClass(InterfaceWithImplementation.class), List.of(), null, specimenFactory))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("context: null");
        }

        @Test
        @DisplayName("specimenFactory must not be null")
        void specimenFactoryIsRequired() {
            assertThatThrownBy(() -> new ExperimentalAbstractSpecimen<>(SpecimenType.fromClass(InterfaceWithImplementation.class), List.of(), context, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("specimenFactory: null");
        }

        @Test
        @DisplayName("type must be either abstract or an interface")
        void typeMustBeAbstract() {
            assertThatThrownBy(() -> new ExperimentalAbstractSpecimen<>(SpecimenType.fromClass(Object.class), List.of(), context, specimenFactory))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("type: java.lang.Object");
        }

        @Test
        @DisplayName("type must not be a collection")
        void typeMustNotBeCollection() {
            assertThatThrownBy(() -> new ExperimentalAbstractSpecimen<>(SpecimenType.fromClass(Collection.class), List.of(), context, specimenFactory))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("type: java.util.Collection");
        }

        @Test
        @DisplayName("type must not be a map")
        void typeMustNotBeMap() {
            assertThatThrownBy(() -> new ExperimentalAbstractSpecimen<>(SpecimenType.fromClass(Map.class), List.of(), context, specimenFactory))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("type: java.util.Map");
        }
    }

    @Nested
    @DisplayName("When creating")
    class WhenCallingCreate {

        @Test
        @DisplayName("the result is cached")
        void resultIsCached() {
            var original = new InterfaceSpecimen<InterfaceWithoutImplementation>(SpecimenType.fromClass(InterfaceWithoutImplementation.class), context, specimenFactory).create(noContext(), new Annotation[0]);
            var cached = new InterfaceSpecimen<InterfaceWithoutImplementation>(SpecimenType.fromClass(InterfaceWithoutImplementation.class), context, specimenFactory).create(noContext(), new Annotation[0]);

            assertThat(original)
                    .isInstanceOf(InterfaceWithoutImplementation.class)
                    .isSameAs(cached);
            assertThat(original.toString()).isEqualTo(cached.toString());
            assertThat(original.getTestObject()).isSameAs(cached.getTestObject());
        }

        @Nested
        @DisplayName("an interface")
        class ForInterfaceType {

            @Test
            @DisplayName("a proxy will be returned if no implementation was found")
            void returnsProxyIfNoImplementationWasFound() {
                var sut = new ExperimentalAbstractSpecimen<>(new SpecimenType<InterfaceWithoutImplementation>() {}, List.of(), context, specimenFactory);

                var actual = sut.create(noContext(), new Annotation[0]);

                assertThat(actual)
                        .isInstanceOf(Proxy.class)
                        .isInstanceOf(InterfaceWithoutImplementation.class);
                assertThat(actual.publicField)
                        .isInstanceOf(Integer.class)
                        .isEqualTo(1);
                assertThat(actual.toString()).isInstanceOf(String.class);
                assertThat(actual.getTestObject()).isInstanceOf(TestObject.class);
            }

            @Test
            @DisplayName("a proxy will be returned if implementation cannot be manufactured")
            void returnsProxyIfImplementationCannotBeManufactured() {
                var spyingSpecimenFactory = spy(specimenFactory);

                var sut = new ExperimentalAbstractSpecimen<>(
                        new SpecimenType<InterfaceWithImplementation>() {},
                        List.of(new SpecimenType<InterfaceWithImplementationImpl>() {}),
                        context, spyingSpecimenFactory);

                var unmanufacturableSpecimen = mock(new SpecimenType<ObjectSpecimen<InterfaceWithImplementationImpl>>() {}.asClass());
                when(unmanufacturableSpecimen.create(any(), any())).thenThrow(new RuntimeException("expected for test"));

                when(spyingSpecimenFactory.build(new SpecimenType<InterfaceWithImplementationImpl>() {})).thenReturn(unmanufacturableSpecimen);

                var actual = sut.create(noContext(), new Annotation[0]);

                assertThat(actual)
                        .isInstanceOf(Proxy.class)
                        .isInstanceOf(InterfaceWithImplementation.class);
                assertThat(actual.publicField)
                        .isInstanceOf(Integer.class)
                        .isEqualTo(1);
                assertThat(actual.toString()).isInstanceOf(String.class);
                assertThat(actual.getTestObject()).isInstanceOf(TestObject.class);
            }

            @Test
            @DisplayName("an instance of an implementing class will be returned")
            void returnsInstanceOfImplementingClass() {
                var sut = new ExperimentalAbstractSpecimen<>(
                        new SpecimenType<InterfaceWithImplementation>() {},
                        List.of(new SpecimenType<InterfaceWithImplementationImpl>() {}),
                        context, specimenFactory);

                var actual = sut.create(noContext(), new Annotation[0]);

                assertThat(actual)
                        .isNotInstanceOf(Proxy.class)
                        .isExactlyInstanceOf(InterfaceWithImplementationImpl.class);
                assertThat(actual.publicField)
                        .isInstanceOf(Integer.class)
                        .isEqualTo(1);
                assertThat(actual.toString()).isInstanceOf(String.class);
                assertThat(actual.getTestObject()).isInstanceOf(TestObject.class);
            }

            @Test
            @DisplayName("an instance of an implementing generic class will be returned")
            void returnsInstanceOfImplementingGenericClass() {
                var sut = new ExperimentalAbstractSpecimen<>(
                        new SpecimenType<GenericInterfaceTUWithGenericImplementationU<String, Integer>>() {},
                        List.of(new SpecimenType<GenericInterfaceTUWithGenericImplementationUImpl<Integer>>() {}),
                        context, specimenFactory);

                var actual = sut.create(noContext(), new Annotation[0]);

                assertThat(actual)
                        .isNotInstanceOf(Proxy.class)
                        .isExactlyInstanceOf(GenericInterfaceTUWithGenericImplementationUImpl.class);
                assertThat(actual.publicField)
                        .isInstanceOf(Integer.class)
                        .isEqualTo(1);
                assertThat(actual.toString()).isInstanceOf(String.class);
                assertThat(actual.getT()).isInstanceOf(String.class);
                assertThat(actual.getU()).isInstanceOf(Integer.class);
            }
        }

        @Nested
        @DisplayName("an abstract class")
        class ForAbstractClassType {

            @Test
            @DisplayName("a proxy will be returned if no subclass was found")
            void returnsProxyIfNoSubclassWasFound() {

                var sut = new ExperimentalAbstractSpecimen<>(new SpecimenType<AbstractClassWithoutImplementation>() {}, List.of(), context, specimenFactory);

                var actual = sut.create(noContext(), new Annotation[0]);

                assertThat(actual)
                        .isInstanceOf(javassist.util.proxy.Proxy.class)
                        .isInstanceOf(AbstractClassWithoutImplementation.class);
                assertThat(actual.toString()).isInstanceOf(String.class);
                assertThat(actual.getTestObject()).isInstanceOf(TestObject.class);
            }

            @Test
            @DisplayName("a proxy will be returned if subclass cannot be manufactured")
            void returnsProxyIfSubclassCannotBeManufactured() {
                var spyingSpecimenFactory = spy(specimenFactory);

                var sut = new ExperimentalAbstractSpecimen<>(
                        new SpecimenType<AbstractClassWithImplementation>() {},
                        List.of(new SpecimenType<AbstractClassWithImplementationImpl>() {}),
                        context, spyingSpecimenFactory);

                var unmanufacturableSpecimen = mock(new SpecimenType<ObjectSpecimen<AbstractClassWithImplementationImpl>>() {}.asClass());
                when(unmanufacturableSpecimen.create(any(), any())).thenThrow(new RuntimeException("expected for test"));

                when(spyingSpecimenFactory.build(new SpecimenType<AbstractClassWithImplementationImpl>() {})).thenReturn(unmanufacturableSpecimen);

                var actual = sut.create(noContext(), new Annotation[0]);

                assertThat(actual)
                        .isInstanceOf(javassist.util.proxy.Proxy.class)
                        .isInstanceOf(AbstractClassWithImplementation.class);
                assertThat(actual.toString()).isInstanceOf(String.class);
                assertThat(actual.getTestObject()).isInstanceOf(TestObject.class);
            }

            @Test
            @DisplayName("an instance of a subclass will be returned")
            void returnsInstanceOfImplementingClass() {

                var sut = new ExperimentalAbstractSpecimen<>(
                        new SpecimenType<AbstractClassWithImplementation>() {},
                        List.of(new SpecimenType<AbstractClassWithImplementationImpl>() {}),
                        context, specimenFactory);

                var actual = sut.create(noContext(), new Annotation[0]);

                assertThat(actual)
                        .isNotInstanceOf(javassist.util.proxy.Proxy.class)
                        .isExactlyInstanceOf(AbstractClassWithImplementationImpl.class);
                assertThat(actual.toString()).isInstanceOf(String.class);
                assertThat(actual.getTestObject()).isInstanceOf(TestObject.class);
            }

            @Test
            @DisplayName("an instance of a generic subclass will be returned")
            void returnsInstanceOfImplementingGenericClass() {
                var sut = new ExperimentalAbstractSpecimen<>(
                        new SpecimenType<GenericAbstractClassTUWithGenericImplementationU<String, Integer>>() {},
                        List.of(new SpecimenType<GenericAbstractClassTUWithGenericImplementationUImpl<Integer>>() {}),
                        context, specimenFactory);

                var actual = sut.create(noContext(), new Annotation[0]);

                assertThat(actual)
                        .isNotInstanceOf(javassist.util.proxy.Proxy.class)
                        .isExactlyInstanceOf(GenericAbstractClassTUWithGenericImplementationUImpl.class);
                assertThat(actual.toString()).isInstanceOf(String.class);
                assertThat(actual.getT()).isInstanceOf(String.class);
                assertThat(actual.getU()).isInstanceOf(Integer.class);
            }
        }
    }
}
