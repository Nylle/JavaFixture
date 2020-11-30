package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.CustomizationContext;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.InstanceFactory;
import com.github.nylle.javafixture.Reflector;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.SpecimenType;

import static com.github.nylle.javafixture.CustomizationContext.noContext;

public class ObjectSpecimen<T> implements ISpecimen<T> {

    private final SpecimenType<T> type;
    private final Context context;
    private final SpecimenFactory specimenFactory;
    private final InstanceFactory instanceFactory;

    public ObjectSpecimen(SpecimenType<T> type, Context context, SpecimenFactory specimenFactory) {

        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        if (specimenFactory == null) {
            throw new IllegalArgumentException("specimenFactory: null");
        }

        if (type.isPrimitive() || type.isEnum() || type.isBoxed() || type.asClass() == String.class || type.isMap() || type.isCollection() || type.isInterface()) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        this.type = type;
        this.context = context;
        this.specimenFactory = specimenFactory;
        this.instanceFactory = new InstanceFactory(specimenFactory);
    }

    @Override
    public T create() {
        return create(noContext());
    }

    @Override
    public T create(CustomizationContext customizationContext) {
        if (context.isCached(type)) {
            return context.cached(type);
        }

        if (customizationContext.useRandomConstructor()) {
            return context.cached(type, instanceFactory.construct(type));
        }

        return new Reflector<>(context.cached(type, instanceFactory.instantiate(type)), specimenFactory)
                .validateCustomization(customizationContext, type)
                .populate(customizationContext);
    }
}

