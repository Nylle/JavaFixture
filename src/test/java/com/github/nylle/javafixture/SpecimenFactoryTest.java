package com.github.nylle.javafixture;

import com.github.nylle.javafixture.annotations.testcases.TestCase;
import com.github.nylle.javafixture.annotations.testcases.TestWithCases;
import com.github.nylle.javafixture.specimen.ArraySpecimen;
import com.github.nylle.javafixture.specimen.CollectionSpecimen;
import com.github.nylle.javafixture.specimen.EnumSpecimen;
import com.github.nylle.javafixture.specimen.GenericSpecimen;
import com.github.nylle.javafixture.specimen.InterfaceSpecimen;
import com.github.nylle.javafixture.specimen.MapSpecimen;
import com.github.nylle.javafixture.specimen.ObjectSpecimen;
import com.github.nylle.javafixture.specimen.PredefinedSpecimen;
import com.github.nylle.javafixture.specimen.PrimitiveSpecimen;
import com.github.nylle.javafixture.specimen.SpecialSpecimen;
import com.github.nylle.javafixture.specimen.TimeSpecimen;
import com.github.nylle.javafixture.testobjects.TestEnum;
import com.github.nylle.javafixture.testobjects.TestObjectGeneric;
import com.github.nylle.javafixture.testobjects.TestPrimitive;
import com.github.nylle.javafixture.testobjects.example.IContract;
import com.github.nylle.javafixture.testobjects.interfaces.GenericInterfaceTUWithGenericImplementationT;
import com.github.nylle.javafixture.testobjects.interfaces.GenericInterfaceTUWithGenericImplementationTU;
import com.github.nylle.javafixture.testobjects.interfaces.GenericInterfaceTUWithGenericImplementationU;
import com.github.nylle.javafixture.testobjects.interfaces.GenericInterfaceWithImplementation;
import com.github.nylle.javafixture.testobjects.interfaces.InterfaceWithGenericImplementation;
import com.github.nylle.javafixture.testobjects.interfaces.InterfaceWithImplementation;
import com.github.nylle.javafixture.testobjects.interfaces.InterfaceWithoutImplementation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.time.MonthDay;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SpecimenFactoryTest {

    @TestWithCases
    @TestCase(class1 = String.class, class2 = PrimitiveSpecimen.class)
    @TestCase(class1 = Integer.class, class2 = PrimitiveSpecimen.class)
    @TestCase(class1 = int.class, class2 = PrimitiveSpecimen.class)
    @TestCase(class1 = TestEnum.class, class2 = EnumSpecimen.class)
    @TestCase(class1 = Map.class, class2 = MapSpecimen.class)
    @TestCase(class1 = List.class, class2 = CollectionSpecimen.class)
    @TestCase(class1 = int[].class, class2 = ArraySpecimen.class)
    @TestCase(class1 = IContract.class, class2 = InterfaceSpecimen.class)
    @TestCase(class1 = Duration.class, class2 = TimeSpecimen.class)
    @TestCase(class1 = Period.class, class2 = TimeSpecimen.class)
    @TestCase(class1 = MonthDay.class, class2 = TimeSpecimen.class)
    @TestCase(class1 = ZoneId.class, class2 = TimeSpecimen.class)
    @TestCase(class1 = ZonedDateTime.class, class2 = TimeSpecimen.class)
    @TestCase(class1 = ZoneOffset.class, class2 = TimeSpecimen.class)
    @TestCase(class1 = Instant.class, class2 = TimeSpecimen.class)
    @TestCase(class1 = java.util.Date.class, class2 = TimeSpecimen.class)
    @TestCase(class1 = java.sql.Date.class, class2 = TimeSpecimen.class)
    @TestCase(class1 = File.class, class2 = SpecialSpecimen.class)
    @TestCase(class1 = URI.class, class2 = SpecialSpecimen.class)
    @TestCase(class1 = Object.class, class2 = ObjectSpecimen.class)
    void build(Class<?> value, Class<?> expected) {
        assertThat(new SpecimenFactory(new Context(new Configuration())).build(SpecimenType.fromClass(value))).isExactlyInstanceOf(expected);
    }

    @Test
    void buildGeneric() {
        var sut = new SpecimenFactory(new Context(new Configuration()));

        assertThat(sut.build(new SpecimenType<List<String>>() {})).isExactlyInstanceOf(CollectionSpecimen.class);
        assertThat(sut.build(new SpecimenType<Map<String, Integer>>() {})).isExactlyInstanceOf(MapSpecimen.class);
        assertThat(sut.build(new SpecimenType<Class<String>>() {})).isExactlyInstanceOf(GenericSpecimen.class);
        assertThat(sut.build(new SpecimenType<TestObjectGeneric<String, List<Integer>>>() {})).isExactlyInstanceOf(GenericSpecimen.class);
    }

    @Test
    @DisplayName("when cache contains a predefined value, return this")
    void buildReturnsCachedValue() {
        var context = new Context(new Configuration());
        var cachedValue = new TestPrimitive();
        var type = SpecimenType.fromClass(TestPrimitive.class);
        context.overwrite(type, cachedValue);
        var sut = new SpecimenFactory(context);

        assertThat(sut.build(type)).isExactlyInstanceOf(PredefinedSpecimen.class);
    }

    @Nested
    class Interfaces {

        Context context = new Context(Configuration.configure().experimentalInterfaces(true));

        @TestWithCases
        @TestCase(bool1 = true, class2 = ObjectSpecimen.class)
        @TestCase(bool1 = false, class2 = InterfaceSpecimen.class)
        void interfaceImplementationsAreOnlySupportedIfExperimentalInterfacesAreEnabled(boolean experimental, Class<?> expected) {
            var context = new Context(Configuration.configure().experimentalInterfaces(experimental));

            assertThat(new SpecimenFactory(context).build(SpecimenType.fromClass(InterfaceWithImplementation.class))).isExactlyInstanceOf(expected);
        }

        @Nested
        @DisplayName("creates InterfaceSpecimen if")
        class CreatesInterfaceSpecimen {

            @Test
            @DisplayName("no implementations found")
            void createsInterfaceSpecimenIfInterfaceHasNoImplementations() {
                assertThat(new SpecimenFactory(context).build(SpecimenType.fromClass(InterfaceWithoutImplementation.class)))
                        .isExactlyInstanceOf(InterfaceSpecimen.class);
            }

            @Test
            @DisplayName("implementation is generic and interface is not")
            void createsInterfaceSpecimenIfImplementationIsGenericAndInterfaceIsNot() {
                assertThat(new SpecimenFactory(context).build(SpecimenType.fromClass(InterfaceWithGenericImplementation.class)))
                        .isExactlyInstanceOf(InterfaceSpecimen.class);
            }
        }

        @Nested
        @DisplayName("creates ObjectSpecimen if")
        class CreatesObjectSpecimen {

            @Test
            @DisplayName("implementation is not generic and interface is not generic")
            void createsObjectSpecimenIfBothImplementationAndInterfaceAreNotGeneric() {
                assertThat(new SpecimenFactory(context).build(SpecimenType.fromClass(InterfaceWithImplementation.class)))
                        .isExactlyInstanceOf(ObjectSpecimen.class);
            }

            @Test
            @DisplayName("implementation is not generic and interface is generic")
            void createsObjectSpecimenIfImplementationIsNotGenericAndInterfaceIs() {
                assertThat(new SpecimenFactory(context).build(new SpecimenType<GenericInterfaceWithImplementation<Integer, String>>() {}))
                        .isExactlyInstanceOf(ObjectSpecimen.class);
            }
        }

        @Nested
        @DisplayName("creates GenericSpecimen if")
        class CreatesGenericSpecimen {

            @Test
            @DisplayName("implementation is generic and interface is generic")
            void createsGenericSpecimenIfImplementationAndInterfaceAreGeneric() {
                assertThat(new SpecimenFactory(context).build(new SpecimenType<GenericInterfaceTUWithGenericImplementationTU<String, Integer>>() {}))
                        .isExactlyInstanceOf(GenericSpecimen.class);
            }

            @Test
            @DisplayName("generic implementation only uses first type-argument of generic interface")
            void createsGenericSpecimenIfGenericImplementationOnlyUsesFirstTypeArgumentOfGenericInterface() {
                assertThat(new SpecimenFactory(context).build(new SpecimenType<GenericInterfaceTUWithGenericImplementationT<String, Integer>>() {}))
                        .isExactlyInstanceOf(GenericSpecimen.class);
            }

            @Test
            @DisplayName("generic implementation only uses second type-argument of generic interface")
            void createsGenericSpecimenIfGenericImplementationOnlyUsesSecondTypeArgumentOfGenericInterface() {
                assertThat(new SpecimenFactory(context).build(new SpecimenType<GenericInterfaceTUWithGenericImplementationU<String, Integer>>() {}))
                        .isExactlyInstanceOf(GenericSpecimen.class);
            }
        }
    }
}
