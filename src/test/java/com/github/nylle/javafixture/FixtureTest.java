package com.github.nylle.javafixture;

import com.github.nylle.javafixture.testobjects.ITestGeneric;
import com.github.nylle.javafixture.testobjects.ITestGenericInside;
import com.github.nylle.javafixture.testobjects.TestCollectionExtension;
import com.github.nylle.javafixture.testobjects.TestEnum;
import com.github.nylle.javafixture.testobjects.TestObject;
import com.github.nylle.javafixture.testobjects.TestObjectGeneric;
import com.github.nylle.javafixture.testobjects.TestObjectWithDeepNesting;
import com.github.nylle.javafixture.testobjects.TestObjectWithEnumMap;
import com.github.nylle.javafixture.testobjects.TestObjectWithEnumSet;
import com.github.nylle.javafixture.testobjects.TestObjectWithGenerics;
import com.github.nylle.javafixture.testobjects.TestObjectWithNestedGenericInterfaces;
import com.github.nylle.javafixture.testobjects.TestObjectWithNestedGenerics;
import com.github.nylle.javafixture.testobjects.TestObjectWithNestedMapsAndLists;
import com.github.nylle.javafixture.testobjects.TestPrimitive;
import com.github.nylle.javafixture.testobjects.example.AccountManager;
import com.github.nylle.javafixture.testobjects.example.Contract;
import com.github.nylle.javafixture.testobjects.example.ContractCategory;
import com.github.nylle.javafixture.testobjects.example.ContractPosition;
import com.github.nylle.javafixture.testobjects.interfaces.InterfaceWithoutImplementation;
import com.github.nylle.javafixture.testobjects.withconstructor.TestObjectWithGenericConstructor;
import com.github.nylle.javafixture.testobjects.withconstructor.TestObjectWithoutDefaultConstructor;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javassist.util.proxy.Proxy;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.nylle.javafixture.Fixture.fixture;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;


class FixtureTest {

    private final Configuration configuration = new Configuration();

    @Test
    void canCreateDefaultConfiguration() {

        var result = Fixture.configuration();

        assertThat(result.getMaxCollectionSize()).isEqualTo(10);
        assertThat(result.getMinCollectionSize()).isEqualTo(2);
        assertThat(result.getStreamSize()).isEqualTo(3);
        assertThat(result.usePositiveNumbersOnly()).isFalse();
        assertThat(result.getClock().instant()).isBeforeOrEqualTo(Instant.now());
        assertThat(result.getClock().getZone()).isEqualTo(ZoneOffset.UTC);
    }

    @Nested
    @DisplayName("when using Class<T>")
    class WhenClass {

        @Test
        void canCreatePrimitives() {
            Fixture fixture = new Fixture(configuration);

            int integerResult = fixture.create(int.class);

            assertThat(integerResult).isNotNull();
            assertThat(integerResult).isInstanceOf(Integer.class);

            boolean booleanResult = fixture.create(boolean.class);

            assertThat(booleanResult).isNotNull();
            assertThat(booleanResult).isInstanceOf(Boolean.class);

            char charResult = fixture.create(char.class);

            assertThat(charResult).isNotNull();
            assertThat(charResult).isInstanceOf(Character.class);
        }

        @Test
        void canCreateInstance() {
            Fixture fixture = new Fixture(configuration);

            TestPrimitive result = fixture.create(TestPrimitive.class);

            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(TestPrimitive.class);
        }

        @Test
        void canCreateOptional() {
            Fixture fixture = new Fixture(configuration);

            var result = fixture.createOptional(TestPrimitive.class);

            assertThat(result).isInstanceOf(Optional.class);
            assertThat(result).isPresent();
            assertThat(result.get()).isInstanceOf(TestPrimitive.class);
        }

        @Test
        void canCreateInstanceWithoutDefaultConstructor() {
            Fixture fixture = new Fixture(configuration);

            TestObjectWithoutDefaultConstructor result = fixture.create(TestObjectWithoutDefaultConstructor.class);

            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(TestObjectWithoutDefaultConstructor.class);
        }

        @Test
        void canCreateMany() {
            Fixture fixture = new Fixture(configuration);

            List<TestPrimitive> result = fixture.createMany(TestPrimitive.class).collect(Collectors.toList());

            assertThat(result).isNotNull();
            assertThat(result.size()).isEqualTo(3);
            assertThat(result.get(0)).isInstanceOf(TestPrimitive.class);
            assertThat(result.get(1)).isInstanceOf(TestPrimitive.class);
            assertThat(result.get(2)).isInstanceOf(TestPrimitive.class);
        }

        @Test
        void canAddManyTo() {
            Fixture fixture = new Fixture(configuration);

            List<TestPrimitive> result = new ArrayList<>();
            result.add(new TestPrimitive());

            fixture.addManyTo(result, TestPrimitive.class);

            assertThat(result).isNotNull();
            assertThat(result.size()).isEqualTo(4);
            assertThat(result.get(1)).isInstanceOf(TestPrimitive.class);
        }

        @Test
        void canCreateManyWithCustomization() {
            Fixture fixture = new Fixture(configuration);

            List<TestPrimitive> result = fixture.build(TestPrimitive.class)
                    .with(x -> x.setHello("world"))
                    .with("integer", 3)
                    .without("publicField")
                    .createMany(3)
                    .collect(Collectors.toList());

            TestPrimitive first = result.get(0);
            assertThat(first).isInstanceOf(TestPrimitive.class);
            assertThat(first.getHello()).isEqualTo("world");
            assertThat(first.getInteger()).isEqualTo(3);
            assertThat(first.publicField).isNull();

            TestPrimitive second = result.get(1);
            assertThat(second).isInstanceOf(TestPrimitive.class);
            assertThat(second.getHello()).isEqualTo("world");
            assertThat(second.getInteger()).isEqualTo(3);
            assertThat(second.publicField).isNull();

            TestPrimitive third = result.get(2);
            assertThat(third).isInstanceOf(TestPrimitive.class);
            assertThat(third.getHello()).isEqualTo("world");
            assertThat(third.getInteger()).isEqualTo(3);
            assertThat(third.publicField).isNull();

            assertThat(first.getPrimitive()).isNotEqualTo(second.getPrimitive());
            assertThat(second.getPrimitive()).isNotEqualTo(third.getPrimitive());
        }

        @Test
        void canOverrideBySetter() {
            Fixture fixture = new Fixture(configuration);

            TestPrimitive result = fixture.build(TestPrimitive.class).with(x -> x.setHello("world")).create();

            assertThat(result.getHello()).isEqualTo("world");
        }

        @Test
        void canOverridePublicField() {
            Fixture fixture = new Fixture(configuration);

            TestPrimitive result = fixture.build(TestPrimitive.class).with(x -> x.publicField = "world").create();

            assertThat(result.publicField).isEqualTo("world");
        }

        @Test
        void canOverridePrivateField() {
            Fixture fixture = new Fixture(configuration);

            TestPrimitive result = fixture.build(TestPrimitive.class).with("hello", "world").create();

            assertThat(result.getHello()).isEqualTo("world");
        }

        @Test
        void canOmitPrivateField() {
            Fixture fixture = new Fixture(configuration);

            TestPrimitive result = fixture.build(TestPrimitive.class).without("hello").create();

            assertThat(result.getHello()).isNull();
        }

        @Test
        void canCustomizeOptional() {
            Fixture fixture = new Fixture(configuration);

            var result = fixture.build(TestPrimitive.class)
                    .without("hello")
                    .with("integer", 999)
                    .with(x -> x.setPrimitive(888))
                    .createOptional();

            assertThat(result).isInstanceOf(Optional.class);
            assertThat(result).isPresent();
            assertThat(result.get()).isInstanceOf(TestPrimitive.class);
            assertThat(result.get().getHello()).isNull();
            assertThat(result.get().getInteger()).isEqualTo(999);
            assertThat(result.get().getPrimitive()).isEqualTo(888);
        }

        @Test
        void canOmitPrivatePrimitiveFieldAndInitializesDefaultValue() {
            Fixture fixture = new Fixture(configuration);

            TestPrimitive result = fixture.build(TestPrimitive.class).without("primitive").create();

            assertThat(result.getPrimitive()).isEqualTo(0);
        }

        @Test
        void objectCanBeCustomizedWithType() {
            var expected = Period.ofDays(42);
            var expectedFile = new File("expected-file-name");
            var result = fixture().build(Contract.class)
                    .with(Period.class, expected)
                    .with(File.class, expectedFile)
                    .create();

            var actual = result.getBaseContractPosition().getRemainingPeriod();

            var softly = new SoftAssertions();
            softly.assertThat(actual).isSameAs(expected);
            softly.assertThat(result.getBaseContractPosition().getFile()).isEqualTo(expectedFile);
            softly.assertAll();
        }

        @Test
        void interfaceCanBeCustomizedWithType() {
            Fixture sut = new Fixture(configuration);

            var expected = new TestObject("", List.of(1), Map.of());
            var result = sut.build(InterfaceWithoutImplementation.class)
                    .with(TestObject.class, expected)
                    .create();

            var actual = result.getTestObject();

            assertThat(actual).isSameAs(expected);
        }

        @Test
        void canPerformAction() {
            Fixture fixture = new Fixture(configuration);

            ContractPosition cp = fixture.create(ContractPosition.class);
            Contract contract = fixture.build(Contract.class).with(x -> x.addContractPosition(cp)).with(x -> x.setBaseContractPosition(cp)).create();

            assertThat(contract.getContractPositions().contains(contract.getBaseContractPosition())).isTrue();
        }

        @Test
        void canCreateComplexModel() {
            Fixture fixture = new Fixture(configuration);

            Contract result = fixture.create(Contract.class);
            assertThat(result).isInstanceOf(Contract.class);
            assertThat(result.getBaseContractPosition()).isInstanceOf(ContractPosition.class);
            assertThat(result.getAccountManagers()).isInstanceOf(AccountManager[].class);
            assertThat(result.getContractPositions()).isInstanceOf(Set.class);
            assertThat(result.getContractPositions().size()).isGreaterThan(0);
            assertThat(result.getCategory()).isInstanceOf(ContractCategory.class);
            assertThat(result.getCreationDate()).isInstanceOf(LocalDateTime.class);

            ContractPosition firstContractPosition = result.getContractPositions().iterator().next();
            assertThat(firstContractPosition).isInstanceOf(ContractPosition.class);
            assertThat(firstContractPosition.getContract()).isInstanceOf(Contract.class);
            assertThat(firstContractPosition.getStartDate()).isInstanceOf(LocalDate.class);
            assertThat(firstContractPosition.getRemainingPeriod()).isInstanceOf(Period.class);
            assertThat(firstContractPosition.getFile()).isInstanceOf(File.class);
        }

        @Test
        void canCreateNestedParameterizedCollections() {
            Fixture fixture = new Fixture(configuration);

            var classWithMapWithList = fixture.create(TestObjectWithNestedMapsAndLists.class);

            var nestedList = classWithMapWithList.getNestedList();
            assertThat(nestedList).isNotEmpty();
            assertThat(nestedList).isNotEmpty();
            var innerList = nestedList.get(0);
            assertThat(innerList).isNotEmpty();
            assertThat(innerList.get(0)).isNotEmpty();

            var mapWithMap = classWithMapWithList.getMapWithMap();
            assertThat(mapWithMap).isNotEmpty();
            assertThat(mapWithMap.values()).isNotEmpty();
            var firstValue = mapWithMap.values().iterator().next();
            assertThat(firstValue).isNotEmpty();
            assertThat(firstValue.values()).isNotEmpty();

            var mapWithList = classWithMapWithList.getMapWithList();
            assertThat(mapWithList).isNotEmpty();
            assertThat(mapWithList.values()).isNotEmpty();
            var firstList = mapWithList.values().iterator().next();
            assertThat(firstList).isNotEmpty();

            var deeplyNestedList = classWithMapWithList.getDeeplyNestedList();
            assertThat(deeplyNestedList).isNotEmpty();
            var firstEntry = deeplyNestedList.get(0);
            assertThat(firstEntry).isNotEmpty();
            var firstMapEntry = firstEntry.values().iterator().next();
            assertThat(firstMapEntry).isNotEmpty();
            assertThat(firstMapEntry.get(0)).isNotEmpty();
        }

        @Test
        void canCreateParameterizedObject() {
            final Fixture fixture = new Fixture(new Configuration().collectionSizeRange(2, 2));

            final var result = fixture.create(TestObjectWithGenerics.class);

            assertThat(result).isInstanceOf(TestObjectWithGenerics.class);

            assertThat(result.getGenerics()).hasSize(2);
            assertThat(result.getGenerics().get(0).getT()).isInstanceOf(String.class);
            assertThat(result.getGenerics().get(0).getU()).isInstanceOf(Integer.class);
            assertThat(result.getGenerics().get(0).getString()).isInstanceOf(String.class);

            assertThat(result.getGeneric()).isInstanceOf(TestObjectGeneric.class);
            assertThat(result.getGeneric().getT()).isInstanceOf(String.class);
            assertThat(result.getGeneric().getU()).isInstanceOf(Integer.class);

            assertThat(result.getAClass()).isInstanceOf(Class.class);
            assertThat(result.getAClass()).isEqualTo(Object.class);

            assertThat(result.getOptional()).isInstanceOf(Optional.class);
        }

        @Test
        void canCreateNestedParameterizedObject() {
            final Fixture fixture = new Fixture(new Configuration().collectionSizeRange(2, 2));

            final var result = fixture.create(TestObjectWithNestedGenerics.class);

            assertThat(result).isInstanceOf(TestObjectWithNestedGenerics.class);

            assertThat(result.getOptionalGeneric()).isInstanceOf(Optional.class);

            assertThat(result.getGenericOptional()).isInstanceOf(TestObjectGeneric.class);
            assertThat(result.getGenericOptional().getT()).isInstanceOf(String.class);
            assertThat(result.getGenericOptional().getU()).isInstanceOf(Optional.class);
        }

        @Test
        void canCreateNestedParameterizedInterfaces() {
            final var result = fixture().create(TestObjectWithNestedGenericInterfaces.class);

            assertThat(result).isInstanceOf(TestObjectWithNestedGenericInterfaces.class);

            assertThat(result.getTestGeneric()).isInstanceOf(ITestGeneric.class);
            assertThat(result.getTestGeneric().publicField).isInstanceOf(Integer.class);

            assertThat(result.getTestGeneric().getT()).isInstanceOf(String.class);
            assertThat(result.getTestGeneric().getU()).isInstanceOf(ITestGenericInside.class);

            assertThat(result.getTestGeneric().getU().getOptionalBoolean()).isInstanceOf(Optional.class);

            assertThat(result.getTestGeneric().getU().getOptionalT()).isInstanceOf(Optional.class);

            assertThat(result.getTestGeneric().getU().getTestGeneric()).isInstanceOf(TestObjectGeneric.class);
            assertThat(result.getTestGeneric().getU().getTestGeneric().getT()).isInstanceOf(String.class);
            assertThat(result.getTestGeneric().getU().getTestGeneric().getU()).isInstanceOf(Integer.class);

        }

        @Test
        void createThroughRandomConstructor() {

            Fixture fixture = new Fixture(configuration);

            var result = fixture.construct(TestObjectWithGenericConstructor.class);

            assertThat(result).isInstanceOf(TestObjectWithGenericConstructor.class);
            assertThat(result.getValue()).isInstanceOf(String.class);
            assertThat(result.getInteger()).isInstanceOf(Optional.class);
            assertThat(result.getPrivateField()).isNull();
        }
    }

    @Nested
    @DisplayName("when using SpecimenType<T>")
    class WhenSpecimenType {

        @Test
        void canCreateGenericObject() {
            Fixture fixture = new Fixture(configuration);

            var result = fixture.create(new SpecimenType<TestObjectGeneric<String, Optional<Integer>>>() {});

            assertThat(result).isInstanceOf(TestObjectGeneric.class);
            assertThat(result.getT()).isInstanceOf(String.class);
            assertThat(result.getU()).isInstanceOf(Optional.class);
        }

        @Test
        void canCreateGenericInterface() {
            Fixture fixture = new Fixture(configuration);

            var result = fixture.create(new SpecimenType<ITestGeneric<String, ITestGenericInside<Integer>>>() {});

            assertThat(result).isInstanceOf(ITestGeneric.class);
            assertThat(result.publicField).isInstanceOf(Integer.class);
            assertThat(result.getT()).isInstanceOf(String.class);
            assertThat(result.getU()).isInstanceOf(ITestGenericInside.class);
            assertThat(result.getU().getOptionalBoolean()).isInstanceOf(Optional.class);
            assertThat(result.getU().getOptionalT()).isInstanceOf(Optional.class);
            assertThat(result.getU().getTestGeneric()).isInstanceOf(TestObjectGeneric.class);
            assertThat(result.getU().getTestGeneric().getT()).isInstanceOf(String.class);
            assertThat(result.getU().getTestGeneric().getU()).isInstanceOf(Integer.class);
        }

        @Test
        void canCreateMapsAndLists() {
            Fixture fixture = new Fixture(configuration);

            var result = fixture.create(new SpecimenType<Map<String, Map<String, List<Optional<String>>>>>() {});

            assertThat(result).isInstanceOf(Map.class);
            assertThat(result.values()).isNotEmpty();
            var subMap = result.values().iterator().next();
            assertThat(subMap).isInstanceOf(Map.class);
            assertThat(subMap.values()).isNotEmpty();
            var innerList = subMap.values().iterator().next();
            assertThat(innerList).isNotEmpty();
            assertThat(innerList.get(0)).isInstanceOf(Optional.class);
        }

        @Test
        void canCreateEnumSets() {

            Fixture fixture = new Fixture(new Configuration().collectionSizeRange(2, 2));

            var result = fixture.create(TestObjectWithEnumSet.class);

            assertThat(result.getId()).isNotBlank();
            assertThat(result.getEnums()).isNotEmpty();
            assertThat(result.getEnums().iterator().next()).isInstanceOf(TestEnum.class);
        }

        @Test
        void canCreateEnumMaps() {

            Fixture fixture = new Fixture(new Configuration().collectionSizeRange(2, 2));

            var result = fixture.create(TestObjectWithEnumMap.class);

            assertThat(result.getId()).isNotBlank();
            assertThat(result.getEnums()).isNotEmpty();
            assertThat(result.getEnums().keySet().iterator().next()).isInstanceOf(TestEnum.class);
        }

        @Test
        void canCreateMany() {
            Fixture fixture = new Fixture(configuration);

            var result = fixture.createMany(new SpecimenType<Optional<Integer>>() {}).collect(toList());

            assertThat(result).isNotNull();
            assertThat(result).isNotEmpty();
            assertThat(result.get(0)).isInstanceOf(Optional.class);
        }

        @Test
        void canAddManyTo() {
            Fixture fixture = new Fixture(configuration);

            List<Optional<String>> result = new ArrayList<>();
            result.add(Optional.of("existing"));

            fixture.addManyTo(result, new SpecimenType<>() {});

            assertThat(result).isNotNull();
            assertThat(result.size()).isEqualTo(4);
            assertThat(result.get(0)).contains("existing");
            assertThat(result.get(1)).isInstanceOf(Optional.class);
        }

        @Test
        void canBeCustomized() {

            Fixture fixture = new Fixture(configuration);

            var result = fixture.build(new SpecimenType<TestObjectGeneric<List<String>, String>>() {})
                    .without("primitiveInt")
                    .without("string")
                    .with("u", "foo")
                    .with(x -> x.getT().add("bar"))
                    .create();

            assertThat(result.getT()).isInstanceOf(List.class);
            assertThat(result.getT().size()).isGreaterThan(1);
            assertThat(result.getT().get(result.getT().size() - 1)).isEqualTo("bar");
            assertThat(result.getU()).isEqualTo("foo");
            assertThat(result.getString()).isNull();
            assertThat(result.getPrimitiveInt()).isEqualTo(0);
        }

        @Test
        void objectCanBeCustomizedWithType() {
            Fixture sut = new Fixture(configuration);

            var expected = new TestObjectGeneric<String, Integer>();

            var result = sut.build(TestObjectWithDeepNesting.class)
                    .with(new SpecimenType<>() {}, expected)
                    .create();

            var actual = result.getNested().getGeneric();

            assertThat(actual).isSameAs(expected);
        }

        @Test
        void createThroughRandomConstructor() {

            Fixture fixture = new Fixture(configuration);

            var result = fixture.construct(new SpecimenType<TestObjectWithGenericConstructor>() {});

            assertThat(result).isInstanceOf(TestObjectWithGenericConstructor.class);
            assertThat(result.getValue()).isInstanceOf(String.class);
            assertThat(result.getInteger()).isInstanceOf(Optional.class);
        }
    }

    @Test
    @Disabled("This test will start working when it's run with JDK 17")
    void createExceptionWithLimitedTree() {

        var sut = new Fixture(configuration);

        var result = sut.create(Throwable.class);

        assertThatCode(() -> flattenRecursively(result).collect(toList()))
                .doesNotThrowAnyException();
    }

    @Test
    void createSubclassOfCollectionAsProxyBecauseFixtureCannotCreateAnInstanceOfANonVanillaCollection() {
        var sut = new Fixture(configuration);

        var result = sut.create(new SpecimenType<TestCollectionExtension<String>>() {});

        assertThat(result).isInstanceOf(TestCollectionExtension.class);
        assertThat(Proxy.class.isAssignableFrom(result.getClass())).isTrue();
    }

    private Stream<Throwable> flattenRecursively(Throwable throwable) {
        return Stream.of(throwable.getSuppressed()).flatMap(x -> flattenRecursively(x));
    }
}
