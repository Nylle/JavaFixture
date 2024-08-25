package com.github.nylle.javafixture;

import java.lang.annotation.Annotation;

public interface ISpecimen<T> {

    T create(final CustomizationContext customizationContext, Annotation[] annotations);

    interface IMeta {

        <T> boolean supports(SpecimenType<T> type);

        <T> ISpecimen<T> create(SpecimenType<T> type, Context context, SpecimenFactory specimenFactory);
    }
}

