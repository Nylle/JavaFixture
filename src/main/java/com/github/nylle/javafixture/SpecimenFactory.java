package com.github.nylle.javafixture;

import com.github.nylle.javafixture.specimen.AbstractSpecimen;
import com.github.nylle.javafixture.specimen.ArraySpecimen;
import com.github.nylle.javafixture.specimen.CollectionSpecimen;
import com.github.nylle.javafixture.specimen.EnumSpecimen;
import com.github.nylle.javafixture.specimen.ExperimentalAbstractSpecimen;
import com.github.nylle.javafixture.specimen.GenericSpecimen;
import com.github.nylle.javafixture.specimen.InterfaceSpecimen;
import com.github.nylle.javafixture.specimen.MapSpecimen;
import com.github.nylle.javafixture.specimen.ObjectSpecimen;
import com.github.nylle.javafixture.specimen.PredefinedSpecimen;
import com.github.nylle.javafixture.specimen.PrimitiveSpecimen;
import com.github.nylle.javafixture.specimen.SpecialSpecimen;
import com.github.nylle.javafixture.specimen.TimeSpecimen;

public class SpecimenFactory {

    private final Context context;

    public SpecimenFactory(Context context) {
        this.context = context;
    }

    public <T> ISpecimen<T> build(final SpecimenType<T> type) {

        if (context.isCached(type)) {
            return new PredefinedSpecimen<>(type, context);
        }

        if (type.isPrimitive() || type.isBoxed() || type.asClass() == String.class) {
            return new PrimitiveSpecimen<>(type, context);
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

        if (type.isParameterized() && !type.isInterface() && !type.isAbstract()) {
            return new GenericSpecimen<>(type, context, this);
        }

        if (type.isParameterized() && (type.isInterface() || type.isAbstract())) {
            if (context.getConfiguration().experimentalInterfaces()) {
                return experimentalAbstract(type);
            }

            return new GenericSpecimen<>(type, context, this);
        }

        if (type.isArray()) {
            return new ArraySpecimen<>(type, context, this);
        }

        if (type.isTimeType()) {
            return new TimeSpecimen<>(type, context);
        }

        if (type.isInterface()) {
            if (context.getConfiguration().experimentalInterfaces()) {
                return experimentalAbstract(type);
            }

            return new InterfaceSpecimen<>(type, context, this);
        }

        if (type.isAbstract()) {
            if (context.getConfiguration().experimentalInterfaces()) {
                return experimentalAbstract(type);
            }
            return new AbstractSpecimen<>(type, context, this);
        }

        if (type.isSpecialType()) {
            return new SpecialSpecimen<>(type, context);
        }

        return new ObjectSpecimen<>(type, context, this);
    }

    private <T> ISpecimen<T> experimentalAbstract(SpecimenType<T> interfaceType) {
        return new ExperimentalAbstractSpecimen<>(interfaceType, new ClassPathScanner().findAllClassesFor(interfaceType), context, this);
    }
}

