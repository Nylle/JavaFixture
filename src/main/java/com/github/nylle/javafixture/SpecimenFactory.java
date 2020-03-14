package com.github.nylle.javafixture;

import com.github.nylle.javafixture.generic.FixtureType;
import com.github.nylle.javafixture.specimen.ArraySpecimen;
import com.github.nylle.javafixture.specimen.CollectionSpecimen;
import com.github.nylle.javafixture.specimen.EnumSpecimen;
import com.github.nylle.javafixture.specimen.GenericSpecimen;
import com.github.nylle.javafixture.specimen.InterfaceSpecimen;
import com.github.nylle.javafixture.specimen.MapSpecimen;
import com.github.nylle.javafixture.specimen.ObjectSpecimen;
import com.github.nylle.javafixture.specimen.PrimitiveSpecimen;
import com.github.nylle.javafixture.specimen.TimeSpecimen;

public class SpecimenFactory {

    private final Context context;

    public SpecimenFactory(Context context) {
        this.context = context;
    }

    public <T> ISpecimen<T> build(final FixtureType<T> type) {

        if (type.isPrimitive() || type.isBoxed() || type.asClass() == String.class) {
            return new PrimitiveSpecimen<>(type);
        }

        if (type.isEnum()) {
            return new EnumSpecimen<>(type);
        }

        if (type.isCollection()) {
            return new CollectionSpecimen<>(type, context, this);
        }

        if (type.isMap()) {
            return new MapSpecimen<>(type, context, this);
        }

        if (type.isParameterized()) {
            return new GenericSpecimen<>(type, context, this);
        }

        if (type.isArray()) {
            return new ArraySpecimen<>(type, context, this);
        }

        if (type.isInterface()) {
            return new InterfaceSpecimen<>(type, context, this);
        }

        if (type.isTimeType()) {
            return new TimeSpecimen<>(type, context);
        }

        return new ObjectSpecimen<>(type, context, this);
    }
}

