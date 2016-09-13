package com.github.nylle.javafixture;

import com.github.nylle.javafixture.testobjects.AnotherTestDto;
import com.github.nylle.javafixture.testobjects.TestDto;
import com.github.nylle.javafixture.testobjects.complex.Contract;
import org.junit.Test;
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

    Contract result = fixture.build(Contract.class).create();

    assertThat(result, notNullValue());
    assertThat(result, instanceOf(Contract.class));
  }


}
