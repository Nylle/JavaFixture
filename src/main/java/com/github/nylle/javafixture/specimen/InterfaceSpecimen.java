package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.FixtureType;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.ProxyFactory;
import com.github.nylle.javafixture.SpecimenFactory;

import static com.github.nylle.javafixture.CustomizationContext.noContext;

public class InterfaceSpecimen<T> implements ISpecimen<T> {

    private final FixtureType<T> type;
    private final Context context;
    private final SpecimenFactory specimenFactory;

    public InterfaceSpecimen(final FixtureType<T> type, final Context context, final SpecimenFactory specimenFactory) {

        if(type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if(!type.isInterface() || type.isMap() || type.isCollection()) {
            throw new IllegalArgumentException("type: " + type.asClass().getName());
        }

        this.type = type;
        this.context = context;
        this.specimenFactory = specimenFactory;
    }

    @Override
    public T create() {
        return create(noContext());
    }

    @Override
    public T create(final CustomizationContext customizationContext) {
        if(context.isCached(type)){
            return (T) context.cached(type);
        }

        return (T) context.cached(type, ProxyFactory.create(type, specimenFactory));
    }
}

