package com.github.nylle.javafixture;

public interface ISpecimen<T> {

    T create();

    T create(final CustomizationContext customizationContext);

}

