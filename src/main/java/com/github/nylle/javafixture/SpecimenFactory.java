package com.github.nylle.javafixture;

import com.github.nylle.javafixture.specimen.ArraySpecimen;
import com.github.nylle.javafixture.specimen.CollectionSpecimen;
import com.github.nylle.javafixture.specimen.EnumSpecimen;
import com.github.nylle.javafixture.specimen.GenericSpecimen;
import com.github.nylle.javafixture.specimen.InterfaceSpecimen;
import com.github.nylle.javafixture.specimen.MapSpecimen;
import com.github.nylle.javafixture.specimen.ObjectSpecimen;
import com.github.nylle.javafixture.specimen.PrimitiveSpecimen;
import com.github.nylle.javafixture.specimen.TimeSpecimen;

import java.lang.reflect.Type;

import static com.github.nylle.javafixture.ReflectionHelper.isParameterizedType;
import static java.util.Arrays.stream;

public class SpecimenFactory {

    private final Context context;

    public SpecimenFactory(Context context) {
        this.context = context;
    }

    public <T> ISpecimen<T> build(final Class<T> type) {

        if (type.isPrimitive() || ReflectionHelper.isBoxedOrString(type)) {
            return new PrimitiveSpecimen<>(type);
        }

        if (type.isEnum()) {
            return new EnumSpecimen<>(type);
        }

        if (ReflectionHelper.isMap(type)) {
            return new MapSpecimen<>(type, null, null, context, this);
        }

        if (ReflectionHelper.isCollection(type)) {
            return new CollectionSpecimen<>(type, null, context, this);
        }

        if (type.isArray()) {
            return new ArraySpecimen<>(type, context, this);
        }

        if (type.isInterface()) {
            return new InterfaceSpecimen<>(type, context, this);
        }
        if (ReflectionHelper.isTimeType(type)) {
            return new TimeSpecimen<>(type, context);
        }

        return new ObjectSpecimen<>(type, context, this);
    }

    public <T> ISpecimen<T> build(final Class<T> type, final Type genericType) {

        if (ReflectionHelper.isCollection(type)) {
            return ReflectionHelper.getRawType(genericType, 0)
                    .map(rawType -> new CollectionSpecimen<>(type, rawType, context, this, asSpecimen(ReflectionHelper.getGenericType(genericType, 0))))
                    .orElseGet(() -> new CollectionSpecimen<>(type, ReflectionHelper.getGenericTypeClass(genericType, 0), context, this));
        }

        if (ReflectionHelper.isMap(type)) {
            return ReflectionHelper.getRawType(genericType, 1)
                    .map(rawType -> new MapSpecimen<>(type, ReflectionHelper.getGenericTypeClass(genericType, 0), rawType, context, this, asSpecimen(ReflectionHelper.getGenericType(genericType, 1))))
                    .orElseGet(() -> new MapSpecimen<>(type, ReflectionHelper.getGenericTypeClass(genericType, 0), ReflectionHelper.getGenericTypeClass(genericType, 1), context, this));
        }

        if (isParameterizedType(genericType)) {
            return new GenericSpecimen<>(type, context, this, stream(ReflectionHelper.getGenericTypes(genericType)).map(t -> asSpecimen(t)).toArray(size -> new ISpecimen<?>[size]));
        }

        return new GenericSpecimen<>(type, context, this, asSpecimen(genericType));
    }

    private ISpecimen<?> asSpecimen(Type t) {
        if (isParameterizedType(t)) {
            return build(ReflectionHelper.castToClass(t), t);
        } else {
            return build(ReflectionHelper.castToClass(t));
        }
    }
}

