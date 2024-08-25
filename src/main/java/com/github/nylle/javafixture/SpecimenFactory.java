package com.github.nylle.javafixture;

import com.github.nylle.javafixture.specimen.AbstractSpecimen;
import com.github.nylle.javafixture.specimen.ArraySpecimen;
import com.github.nylle.javafixture.specimen.CollectionSpecimen;
import com.github.nylle.javafixture.specimen.EnumSpecimen;
import com.github.nylle.javafixture.specimen.GenericSpecimen;
import com.github.nylle.javafixture.specimen.InterfaceSpecimen;
import com.github.nylle.javafixture.specimen.MapSpecimen;
import com.github.nylle.javafixture.specimen.ObjectSpecimen;
import com.github.nylle.javafixture.specimen.PredefinedSpecimen;
import com.github.nylle.javafixture.specimen.PrimitiveSpecimen;
import com.github.nylle.javafixture.specimen.SpecialSpecimen;
import com.github.nylle.javafixture.specimen.TimeSpecimen;

import java.util.List;

public class SpecimenFactory {
    private final static List<ISpecimen.ISpec> specimenSpecs = List.of(
            new SpecialSpecimen.Spec(),
            new PrimitiveSpecimen.Spec(),
            new EnumSpecimen.Spec(),
            new CollectionSpecimen.Spec(),
            new MapSpecimen.Spec(),
            new GenericSpecimen.Spec(),
            new ArraySpecimen.Spec(),
            new TimeSpecimen.Spec(),
            new InterfaceSpecimen.Spec(),
            new AbstractSpecimen.Spec()
    );

    private final Context context;

    public SpecimenFactory(Context context) {
        this.context = context;
    }

    public <T> ISpecimen<T> build(SpecimenType<T> type) {

        if (context.isCached(type)) {
            return new PredefinedSpecimen<>(type, context);
        }

        return specimenSpecs.stream()
                .filter(x -> x.supports(type))
                .map(x -> x.create(type, context, this))
                .findFirst()
                .orElseGet(() -> new ObjectSpecimen<>(type, context, this));
    }
}

