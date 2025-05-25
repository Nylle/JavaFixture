package com.github.nylle.javafixture.testobjects;

import java.util.Collection;

public interface TestCollectionExtension<T> extends Collection<T> {
    int customMethod();
}
