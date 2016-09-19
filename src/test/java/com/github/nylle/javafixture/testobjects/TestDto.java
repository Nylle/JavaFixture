package com.github.nylle.javafixture.testobjects;

public class TestDto {
  private String hello;
  private int primitive;
  private Integer integer;
  public String publicField;

  public String getHello() {
    return hello;
  }

  public void setHello(String hello) {
    this.hello = hello;
  }

  public int getPrimitive() {
    return primitive;
  }

  public void setPrimitive(int primitive) {
    this.primitive = primitive;
  }

  public Integer getInteger() {
    return integer;
  }

  public void setInteger(Integer integer) {
    this.integer = integer;
  }


}
