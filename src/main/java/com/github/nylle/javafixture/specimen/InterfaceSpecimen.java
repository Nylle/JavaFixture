package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.ProxyFactory;
import com.github.nylle.javafixture.ReflectionHelper;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import static com.github.nylle.javafixture.CustomizationContext.noContext;

public class InterfaceSpecimen<T> implements ISpecimen<T> {

    private final Class<T> type;
    private final Context context;
    private final SpecimenFactory specimenFactory;
    private final SpecimenType specimenType;

    public InterfaceSpecimen(final Class<T> type, final Context context, final SpecimenFactory specimenFactory) {

        if(type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if(!type.isInterface() || ReflectionHelper.isMap(type) || ReflectionHelper.isCollection(type)) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.context = context;
        this.specimenFactory = specimenFactory;
        this.specimenType = SpecimenType.forObject(type);
    }

    @Override
    public T create() {
        return create(noContext());
    }

    @Override
    public T create(final CustomizationContext customizationContext) {
        if(context.isCached(specimenType)){
            return (T) context.cached(specimenType);
        }

        return (T) context.cached(specimenType, ProxyFactory.create(type, specimenFactory));
    }



}

