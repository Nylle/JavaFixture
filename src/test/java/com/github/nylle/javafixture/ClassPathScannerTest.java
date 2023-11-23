package com.github.nylle.javafixture;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;

class ClassPathScannerTest {

    private ClassPathScanner sut = new ClassPathScanner();

    @Nested
    @DisplayName("trying to resolve all implementations of an interface class")
    class FindAllClassesForInterface {

        @Test
        @DisplayName("returns an empty list if none was found")
        void returnsAnEmptyListIfNoneWasFound() {
            var actual = sut.findAllClassesFor(SpecimenType.fromClass(InterfaceWithoutImplementation.class));

            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("returns an empty list if not an interface nor abstract")
        void returnsAnEmptyListIfNotAnInterfaceNorAbstract() {
            var actual = sut.findAllClassesFor(SpecimenType.fromClass(String.class));

            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("returns an empty list if exception was thrown")
        void returnsAnEmptyListIfExceptionWasThrown() {
            var throwingType = Mockito.mock(SpecimenType.class);
            doThrow(new IllegalArgumentException("expected for test")).when(throwingType).isInterface();

            var actual = sut.findAllClassesFor(throwingType);

            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("returns a list of SpecimenType all representing an implementing class")
        void returnsSpecimenTypesRepresentingImplementingClasses() {
            var actual = sut.findAllClassesFor(SpecimenType.fromClass(InterfaceWithImplementation.class));

            assertThat(actual).hasSize(1);
            assertThat(actual.get(0).asClass()).isEqualTo(InterfaceWithImplementationImpl.class);
        }

        @Test
        @DisplayName("returns a list of SpecimenType all representing an implementing generic class")
        void returnsSpecimenTypesRepresentingGenericImplementations() {
            var actual = sut.findAllClassesFor(new SpecimenType<GenericInterfaceTUWithGenericImplementationU<String, Integer>>() {});

            assertThat(actual).hasSize(1);
            assertThat(actual.get(0).asClass()).isEqualTo(GenericInterfaceTUWithGenericImplementationUImpl.class);
            assertThat(actual.get(0).getGenericTypeArgument(0).asClass()).isEqualTo(Integer.class);
        }
    }

    @Nested
    @DisplayName("trying to resolve all subclasses of an abstract class")
    class FindAllClassesForAbstractClass {

        @Test
        @DisplayName("returns an empty list if none was found")
        void returnsAnEmptyListIfNoneWasFound() {
            var actual = sut.findAllClassesFor(SpecimenType.fromClass(AbstractClassWithoutImplementation.class));

            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("returns an empty list if not an interface nor abstract")
        void returnsAnEmptyListIfNotAnInterfaceNorAbstract() {
            var actual = sut.findAllClassesFor(SpecimenType.fromClass(String.class));

            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("returns an empty list if exception was thrown")
        void returnsAnEmptyListIfExceptionWasThrown() {
            var throwingType = Mockito.mock(SpecimenType.class);
            doThrow(new IllegalArgumentException("expected for test")).when(throwingType).isInterface();

            var actual = sut.findAllClassesFor(throwingType);

            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("returns SpecimenTypes all representing a subclass")
        void returnsSpecimenTypesRepresentingSubclasses() {
            var actual = sut.findAllClassesFor(SpecimenType.fromClass(AbstractClassWithImplementation.class));

            assertThat(actual).hasSize(1);
            assertThat(actual.get(0).asClass()).isEqualTo(AbstractClassWithImplementationImpl.class);
        }

        @Test
        @DisplayName("returns SpecimenTypes all representing an implementing generic class")
        void returnsSpecimenTypesRepresentingGenericSubclasses() {
            var actual = sut.findAllClassesFor(new SpecimenType<GenericAbstractClassTUWithGenericImplementationU<String, Integer>>() {});

            assertThat(actual).hasSize(1);
            assertThat(actual.get(0).asClass()).isEqualTo(GenericAbstractClassTUWithGenericImplementationUImpl.class);
            assertThat(actual.get(0).getGenericTypeArgument(0).asClass()).isEqualTo(Integer.class);
        }
    }
}
