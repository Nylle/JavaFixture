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
    @DisplayName("trying to resolve a random implementation of an interface")
    class FindRandomClassForInterface {

        @Test
        @DisplayName("returns an empty Optional if none was found")
        void returnsAnEmptyOptionalIfNoneWasFound() {
            var actual = sut.findRandomClassFor(SpecimenType.fromClass(InterfaceWithoutImplementation.class));

            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("returns an empty Optional if not an interface nor abstract")
        void returnsAnEmptyOptionalIfNotAnInterfaceNorAbstract() {
            var actual = sut.findRandomClassFor(SpecimenType.fromClass(String.class));

            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("returns an empty Optional if exception was thrown")
        void returnsAnEmptyOptionalIfExceptionWasThrown() {
            var throwingType = Mockito.mock(SpecimenType.class);
            doThrow(new IllegalArgumentException("expected for test")).when(throwingType).isInterface();

            var actual = sut.findRandomClassFor(throwingType);

            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("returns a SpecimenType representing an implementing class")
        void returnsASpecimenTypeRepresentingAnImplementingClass() {
            var actual = sut.findRandomClassFor(SpecimenType.fromClass(InterfaceWithImplementation.class));

            assertThat(actual).isNotEmpty();
            assertThat(actual.get().asClass()).isEqualTo(InterfaceWithImplementationImpl.class);
        }

        @Test
        @DisplayName("returns a SpecimenType representing an implementing generic class")
        void returnsASpecimenTypeRepresentingAnImplementingGenericClass() {
            var actual = sut.findRandomClassFor(new SpecimenType<GenericInterfaceTUWithGenericImplementationU<String, Integer>>() {});

            assertThat(actual).isNotEmpty();
            assertThat(actual.get().asClass()).isEqualTo(GenericInterfaceTUWithGenericImplementationUImpl.class);
            assertThat(actual.get().getGenericTypeArgument(0).asClass()).isEqualTo(Integer.class);
        }
    }

    @Nested
    @DisplayName("trying to resolve a random implementation of an abstract class")
    class FindRandomClassForAbstractClass {

        @Test
        @DisplayName("returns an empty Optional if none was found")
        void returnsAnEmptyOptionalIfNoneWasFound() {
            var actual = sut.findRandomClassFor(SpecimenType.fromClass(AbstractClassWithoutImplementation.class));

            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("returns an empty Optional if not an interface nor abstract")
        void returnsAnEmptyOptionalIfNotAnInterfaceNorAbstract() {
            var actual = sut.findRandomClassFor(SpecimenType.fromClass(String.class));

            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("returns an empty Optional if exception was thrown")
        void returnsAnEmptyOptionalIfExceptionWasThrown() {
            var throwingType = Mockito.mock(SpecimenType.class);
            doThrow(new IllegalArgumentException("expected for test")).when(throwingType).isInterface();

            var actual = sut.findRandomClassFor(throwingType);

            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("returns a SpecimenType representing an extending class")
        void returnsASpecimenTypeRepresentingAnImplementingClass() {
            var actual = sut.findRandomClassFor(SpecimenType.fromClass(AbstractClassWithImplementation.class));

            assertThat(actual).isNotEmpty();
            assertThat(actual.get().asClass()).isEqualTo(AbstractClassWithImplementationImpl.class);
        }

        @Test
        @DisplayName("returns a SpecimenType representing an implementing generic class")
        void returnsASpecimenTypeRepresentingAnImplementingGenericClass() {
            var actual = sut.findRandomClassFor(new SpecimenType<GenericAbstractClassTUWithGenericImplementationU<String, Integer>>() {});

            assertThat(actual).isNotEmpty();
            assertThat(actual.get().asClass()).isEqualTo(GenericAbstractClassTUWithGenericImplementationUImpl.class);
            assertThat(actual.get().getGenericTypeArgument(0).asClass()).isEqualTo(Integer.class);
        }
    }
}
