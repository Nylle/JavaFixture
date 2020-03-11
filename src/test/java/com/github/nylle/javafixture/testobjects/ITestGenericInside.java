package com.github.nylle.javafixture.testobjects;

import java.util.Optional;

public interface ITestGenericInside<T> {

    Optional<Boolean> getOptionalBoolean();

    Optional<T> getOptionalT();

    TestObjectGeneric<String, T> getTestGeneric();

}
