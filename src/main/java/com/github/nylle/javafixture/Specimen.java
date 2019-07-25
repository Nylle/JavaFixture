package com.github.nylle.javafixture;

import java.lang.reflect.Type;

public class Specimen<T, U> {

    private final SpecimenType type;

    public Specimen(final Class<T> type) {
        this(type, null);
    }

    public Specimen(final Class<T> type, final Type genericType) {
        this.type = new SpecimenType(type, genericType);
        //this.fieldTypes = stream(type.getDeclaredFields()).filter(x -> !Reflector.isStatic(x)).map(x -> new Specimen<>(x.getType(), x.getGenericType())).collect(toList());
    }

    public SpecimenType getType() {
        return type;
    }


}

