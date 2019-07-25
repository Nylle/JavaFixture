package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

class CollectionSpecimenIT {

    @Test
    void xxx() {
        Configuration config = new Configuration(2, 2);
        Context context = new Context(config);
        SpecimenFactory specimenFactory = new SpecimenFactory(context);

        var sut = new CollectionSpecimen<>(List.class, List.class, context, specimenFactory);

        List result = sut.create();

    }

}