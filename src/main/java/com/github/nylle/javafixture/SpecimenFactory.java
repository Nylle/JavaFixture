package com.github.nylle.javafixture;

import com.github.nylle.javafixture.specimen.CollectionSpecimen;
import com.github.nylle.javafixture.specimen.EnumSpecimen;
import com.github.nylle.javafixture.specimen.MapSpecimen;
import com.github.nylle.javafixture.specimen.ObjectSpecimen;
import com.github.nylle.javafixture.specimen.PrimitiveSpecimen;

import java.lang.reflect.Type;

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
            return new MapSpecimen<>(type, null, null, context, this);
        }

        if(Reflector.isCollection(type)) {
            return new CollectionSpecimen<>(type, null, context, this);
        }

        if(type.isInterface()) {
            //return proxyFactory.create(type, this);
        }

        return new ObjectSpecimen(type, context, this);
    }

    public <T, G> Specimen<T> build(final Class<T> type, final Type genericType) {

        if(Reflector.isCollection(type)) {
            return new CollectionSpecimen<>(type, Reflector.getGenericType(genericType, 0), context, this);
        }

        if(Reflector.isMap(type)) {
            return new MapSpecimen<>(type, Reflector.getGenericType(genericType, 0), Reflector.getGenericType(genericType, 1), context, this);
        }

        throw new SpecimenException("Unsupported type: "+ type);
    }

}

