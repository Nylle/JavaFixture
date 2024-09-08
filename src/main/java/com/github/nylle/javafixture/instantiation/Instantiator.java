package com.github.nylle.javafixture.instantiation;

import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.SpecimenFactory;

public interface Instantiator<T> {

    T invoke(SpecimenFactory specimenFactory, CustomizationContext customizationContext);

}
