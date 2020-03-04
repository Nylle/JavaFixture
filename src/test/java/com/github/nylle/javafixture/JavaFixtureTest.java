package com.github.nylle.javafixture;

import com.github.nylle.javafixture.testobjects.TestObjectWithGenericCollection;
import com.github.nylle.javafixture.testobjects.TestObjectWithoutDefaultConstructor;
import com.github.nylle.javafixture.testobjects.TestPrimitive;
import com.github.nylle.javafixture.testobjects.complex.AccountManager;
import com.github.nylle.javafixture.testobjects.complex.ClassWithNestedMapsAndLists;
import com.github.nylle.javafixture.testobjects.complex.Contract;
import com.github.nylle.javafixture.testobjects.complex.ContractCategory;
import com.github.nylle.javafixture.testobjects.complex.ContractPosition;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


class JavaFixtureTest {

    private final Configuration configuration = new Configuration();

    @Test
    void canCreatePrimitives() {
        JavaFixture fixture = new JavaFixture(configuration);

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
        JavaFixture fixture = new JavaFixture(configuration);

        TestPrimitive result = fixture.create(TestPrimitive.class);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(TestPrimitive.class);
    }

    @Test
    void canCreateInstanceWithoutDefaultConstructor() {
        JavaFixture fixture = new JavaFixture(configuration);

        TestObjectWithoutDefaultConstructor result = fixture.create(TestObjectWithoutDefaultConstructor.class);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(TestObjectWithoutDefaultConstructor.class);
    }

    @Test
    void canCreateMany() {
        JavaFixture fixture = new JavaFixture(configuration);

        List<TestPrimitive> result = fixture.createMany(TestPrimitive.class).collect(Collectors.toList());

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0)).isInstanceOf(TestPrimitive.class);
        assertThat(result.get(1)).isInstanceOf(TestPrimitive.class);
        assertThat(result.get(2)).isInstanceOf(TestPrimitive.class);
    }

    @Test
    void canCreateManyWithCustomization() {
        JavaFixture fixture = new JavaFixture(configuration);

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
    void canAddManyTo() {
        JavaFixture fixture = new JavaFixture(configuration);

        List<TestPrimitive> result = new ArrayList<>();
        result.add(new TestPrimitive());

        fixture.addManyTo(result, TestPrimitive.class);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(4);
        assertThat(result.get(1)).isInstanceOf(TestPrimitive.class);
    }

    @Test
    void canOverrideBySetter() {
        JavaFixture fixture = new JavaFixture(configuration);

        TestPrimitive result = fixture.build(TestPrimitive.class).with(x -> x.setHello("world")).create();

        assertThat(result.getHello()).isEqualTo("world");
    }

    @Test
    void canOverridePublicField() {
        JavaFixture fixture = new JavaFixture(configuration);

        TestPrimitive result = fixture.build(TestPrimitive.class).with(x -> x.publicField = "world").create();

        assertThat(result.publicField).isEqualTo("world");
    }

    @Test
    void canOverridePrivateField() {
        JavaFixture fixture = new JavaFixture(configuration);

        TestPrimitive result = fixture.build(TestPrimitive.class).with("hello", "world").create();

        assertThat(result.getHello()).isEqualTo("world");
    }

    @Test
    void canOmitPrivateField() {
        JavaFixture fixture = new JavaFixture(configuration);

        TestPrimitive result = fixture.build(TestPrimitive.class).without("hello").create();

        assertThat(result.getHello()).isNull();
    }

    @Test
    void canOmitPrivatePrimitiveFieldAndInitializesDefaultValue() {
        JavaFixture fixture = new JavaFixture(configuration);

        TestPrimitive result = fixture.build(TestPrimitive.class).without("primitive").create();

        assertThat(result.getPrimitive()).isEqualTo(0);
    }

    @Test
    void canCreateComplexModel() {
        JavaFixture fixture = new JavaFixture(configuration);

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
    void canCreateThrowable() {
        JavaFixture fixture = new JavaFixture(configuration);

        Throwable result = fixture.create(Throwable.class);
        assertThat(result).isInstanceOf(Throwable.class);
        assertThat(result.getMessage().length()).isGreaterThan(0);
        assertThat(result.getLocalizedMessage().length()).isGreaterThan(0);
        assertThat(result.getStackTrace().length).isGreaterThan(0);
        assertThat(result.getStackTrace()[0]).isInstanceOf(StackTraceElement.class);
        assertThat(result.getCause()).isNull(); //if cause == this, the getter returns null
    }

    @Test
    void xxx() {

        final JavaFixture fixture = new JavaFixture(configuration);

        final TestObjectWithGenericCollection result = fixture.create(TestObjectWithGenericCollection.class);

    }

    @Test
    void canPerformAction() {
        JavaFixture fixture = new JavaFixture(configuration);

        ContractPosition cp = fixture.create(ContractPosition.class);
        Contract contract = fixture.build(Contract.class).with(x -> x.addContractPosition(cp)).with(x -> x.setBaseContractPosition(cp)).create();

        assertThat(contract.getContractPositions().contains(contract.getBaseContractPosition())).isTrue();
    }

    @Test
    void canCreatedNestedParameterizeObject() {
        JavaFixture fixture = new JavaFixture(configuration);

        var classWithMapWithList = fixture.create(ClassWithNestedMapsAndLists.class);


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
        assertThat( deeplyNestedList ).isNotEmpty();
        var firstEntry = deeplyNestedList.get(0);
        assertThat( firstEntry ).isNotEmpty();
        var firstMapEntry = firstEntry.values().iterator().next();
        assertThat( firstMapEntry ).isNotEmpty();
        assertThat( firstMapEntry.get(0)).isNotEmpty();
    }


}
