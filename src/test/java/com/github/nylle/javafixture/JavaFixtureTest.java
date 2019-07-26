package com.github.nylle.javafixture;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.github.nylle.javafixture.testobjects.TestObjectWithoutDefaultConstructor;
import com.github.nylle.javafixture.testobjects.TestPrimitive;
import com.github.nylle.javafixture.testobjects.complex.AccountManager;
import com.github.nylle.javafixture.testobjects.complex.Contract;
import com.github.nylle.javafixture.testobjects.complex.ContractCategory;
import com.github.nylle.javafixture.testobjects.complex.ContractPosition;


public class JavaFixtureTest {

    private final Configuration configuration = new Configuration();

    @Test
    public void canCreatePrimitives() {
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
    public void canCreateInstance() {
        JavaFixture fixture = new JavaFixture(configuration);

        TestPrimitive result = fixture.create(TestPrimitive.class);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(TestPrimitive.class);
    }

    @Test
    public void canCreateInstanceWithoutDefaultConstructor() {
        JavaFixture fixture = new JavaFixture(configuration);

        TestObjectWithoutDefaultConstructor result = fixture.create(TestObjectWithoutDefaultConstructor.class);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(TestObjectWithoutDefaultConstructor.class);
    }

    @Test
    public void canCreateMany() {
        JavaFixture fixture = new JavaFixture(configuration);

        List<TestPrimitive> result = fixture.createMany(TestPrimitive.class).collect(Collectors.toList());

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0)).isInstanceOf(TestPrimitive.class);
        assertThat(result.get(1)).isInstanceOf(TestPrimitive.class);
        assertThat(result.get(2)).isInstanceOf(TestPrimitive.class);
    }

    @Test
    public void canCreateManyWithCustomization() {
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
    public void canAddManyTo() {
        JavaFixture fixture = new JavaFixture(configuration);

        List<TestPrimitive> result = new ArrayList<>();
        result.add(new TestPrimitive());

        fixture.addManyTo(result, TestPrimitive.class);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(4);
        assertThat(result.get(1)).isInstanceOf(TestPrimitive.class);
    }

    @Test
    public void canOverrideBySetter() {
        JavaFixture fixture = new JavaFixture(configuration);

        TestPrimitive result = fixture.build(TestPrimitive.class).with(x -> x.setHello("world")).create();

        assertThat(result.getHello()).isEqualTo("world");
    }

    @Test
    public void canOverridePublicField() {
        JavaFixture fixture = new JavaFixture(configuration);

        TestPrimitive result = fixture.build(TestPrimitive.class).with(x -> x.publicField = "world").create();

        assertThat(result.publicField).isEqualTo("world");
    }

    @Test
    public void canOverridePrivateField() {
        JavaFixture fixture = new JavaFixture(configuration);

        TestPrimitive result = fixture.build(TestPrimitive.class).with("hello", "world").create();

        assertThat(result.getHello()).isEqualTo("world");
    }

    @Test
    public void canOmitPrivateField() {
        JavaFixture fixture = new JavaFixture(configuration);

        TestPrimitive result = fixture.build(TestPrimitive.class).without("hello").create();

        assertThat(result.getHello()).isNull();
    }

    @Test
    public void canOmitPrivatePrimitiveFieldAndInitializesDefaultValue() {
        JavaFixture fixture = new JavaFixture(configuration);

        TestPrimitive result = fixture.build(TestPrimitive.class).without("primitive").create();

        assertThat(result.getPrimitive()).isEqualTo(0);
    }

    @Test
    public void canCreateComplexModel() {
        JavaFixture fixture = new JavaFixture(configuration);

        Contract result = fixture.create(Contract.class);
        assertThat(result).isInstanceOf(Contract.class);
        assertThat(result.getBaseContractPosition()).isInstanceOf(ContractPosition.class);
        assertThat(result.getAccountManager()).isInstanceOf(AccountManager.class);
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
    public void canPerformAction() {
        JavaFixture fixture = new JavaFixture(configuration);

        ContractPosition cp = fixture.create(ContractPosition.class);
        Contract contract = fixture.build(Contract.class).with(x -> x.addContractPosition(cp)).with(x -> x.setBaseContractPosition(cp)).create();

        assertThat(contract.getContractPositions().contains(contract.getBaseContractPosition())).isTrue();
    }


}
