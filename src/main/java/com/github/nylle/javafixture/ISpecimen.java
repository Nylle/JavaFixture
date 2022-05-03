package com.github.nylle.javafixture;

import java.lang.annotation.Annotation;

public interface ISpecimen<T> {

    T create(final CustomizationContext customizationContext, Annotation[] annotations);

}

