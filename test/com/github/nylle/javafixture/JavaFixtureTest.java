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
import java.util.Set;
import static org.hamcrest.Matchers.greaterThan;
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
  public void canOverrideBySetter() {
    JavaFixture fixture = new JavaFixture();

    TestDto result = fixture.build(TestDto.class).with(x -> x.setHello("world")).create();

    assertThat(result.getHello(), is("world"));
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


}
