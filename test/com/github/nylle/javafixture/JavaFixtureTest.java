package com.github.nylle.javafixture;

import com.github.nylle.javafixture.testobjects.AnotherTestDto;
import com.github.nylle.javafixture.testobjects.TestDto;
import com.github.nylle.javafixture.testobjects.complex.AccountManager;
import com.github.nylle.javafixture.testobjects.complex.Contract;
import com.github.nylle.javafixture.testobjects.complex.ContractCategory;
import com.github.nylle.javafixture.testobjects.complex.ContractPosition;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;


public class JavaFixtureTest {
  @Test
  public void canCreateInstance() {
    JavaFixture fixture = new JavaFixture();

    TestDto result = fixture.create(TestDto.class);

    assertThat(result, notNullValue());
    assertThat(result, instanceOf(TestDto.class));
  }

  @Test
  public void canCreateInstanceWithoutDefaultConstructor() {
    JavaFixture fixture = new JavaFixture();

    AnotherTestDto result = fixture.create(AnotherTestDto.class);

    assertThat(result, notNullValue());
    assertThat(result, instanceOf(AnotherTestDto.class));
  }

  @Test
  public void canCreateMany() {
    JavaFixture fixture = new JavaFixture();

    List<TestDto> result = fixture.createMany(TestDto.class).collect(Collectors.toList());

    assertThat(result, notNullValue());
    assertThat(result.size(), is(3));
    assertThat(result.get(0), instanceOf(TestDto.class));
    assertThat(result.get(1), instanceOf(TestDto.class));
    assertThat(result.get(2), instanceOf(TestDto.class));
  }

  @Test
  public void canAddManyTo() {
    JavaFixture fixture = new JavaFixture();

    List<TestDto> result = new ArrayList<>();
    result.add(new TestDto());

    fixture.addManyTo(result, TestDto.class);

    assertThat(result, notNullValue());
    assertThat(result.size(), is(4));
    assertThat(result.get(1), instanceOf(TestDto.class));
  }

  @Test
  public void canOverrideBySetter() {
    JavaFixture fixture = new JavaFixture();

    TestDto result = fixture.build(TestDto.class).with(x -> x.setHello("world")).create();

    assertThat(result.getHello(), is("world"));
  }

  @Test
  public void canOverridePublicField() {
    JavaFixture fixture = new JavaFixture();

    TestDto result = fixture.build(TestDto.class).with(x -> x.publicField = "world").create();

    assertThat(result.publicField, is("world"));
  }

  @Test
  public void canOverridePrivateField() {
    JavaFixture fixture = new JavaFixture();

    TestDto result = fixture.build(TestDto.class).with("hello", "world").create();

    assertThat(result.getHello(), is("world"));
  }

  @Test
  public void canOmitPrivateField() {
    JavaFixture fixture = new JavaFixture();

    TestDto result = fixture.build(TestDto.class).without("hello").create();

    assertThat(result.getHello(), is(nullValue()));
  }

  @Test
  public void canOmitPrivatePrimitiveFieldAndInitializesDefaultValue() {
    JavaFixture fixture = new JavaFixture();

    TestDto result = fixture.build(TestDto.class).without("primitive").create();

    assertThat(result.getPrimitive(), is(0));
  }

  @Test
  public void canCreateComplexModel() {
    JavaFixture fixture = new JavaFixture();

    Contract result = fixture.create(Contract.class);
    assertThat(result, instanceOf(Contract.class));
    assertThat(result.getBaseContractPosition(), instanceOf(ContractPosition.class));
    assertThat(result.getAccountManager(), instanceOf(AccountManager.class));
    assertThat(result.getContractPositions(), instanceOf(Set.class));
    assertThat(result.getContractPositions().size(), greaterThan(0));
    assertThat(result.getCategory(), instanceOf(ContractCategory.class));
    assertThat(result.getCreationDate(), instanceOf(LocalDateTime.class));

    ContractPosition firstContractPosition = result.getContractPositions().iterator().next();
    assertThat(firstContractPosition, instanceOf(ContractPosition.class));
    assertThat(firstContractPosition.getContract(), instanceOf(Contract.class));
    assertThat(firstContractPosition.getStartDate(), instanceOf(LocalDate.class));
    assertThat(firstContractPosition.getRemainingPeriod(), instanceOf(Period.class));
  }

  @Test
  public void canPerformAction() {
    JavaFixture fixture = new JavaFixture();

    ContractPosition cp = fixture.create(ContractPosition.class);
    Contract contract = fixture.build(Contract.class).with(x -> x.addContractPosition(cp)).with(x ->
      x.setBaseContractPosition(cp)).create();

    assertThat(contract.getContractPositions().contains(contract.getBaseContractPosition()), is(true));
  }


}
