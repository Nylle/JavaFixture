package com.github.nylle.javafixture;

import com.github.nylle.javafixture.specimen.CollectionSpecimen;
import com.github.nylle.javafixture.specimen.EnumSpecimen;
import com.github.nylle.javafixture.specimen.PrimitiveSpecimen;

public class SpecimenFactory {

    private final Context context;

    public SpecimenFactory(Context context) {
        this.context = context;
    }

    public <T> Specimen<T> build(final Class<T> type) {

        if(type.isPrimitive() || Reflector.isBoxedOrString(type)) {
            return new PrimitiveSpecimen<>(type);
        }

        if(type.isEnum()) {
            return new EnumSpecimen<>(type);
        }

        if(Reflector.isMap(type)) {
            //TODO: non-generic empty map or exception?
            throw new SpecimenException("Unsupported type: "+ type);
        }

        if(Reflector.isCollection(type)) {
            return new CollectionSpecimen<>(type, Object.class, context, this);
        }

        if(type.isInterface()) {
            //return proxyFactory.create(type, this);
        }

        throw new SpecimenException("Unsupported type: "+ type);
    }

    public <T> Specimen<T> build(final Class<T> type, final Class<T> genericType) {
        if(Reflector.isCollection(type)) {
            return new CollectionSpecimen<>(type, genericType, context, this);
        }

        throw new SpecimenException("Unsupported type: "+ type);
    }

}

