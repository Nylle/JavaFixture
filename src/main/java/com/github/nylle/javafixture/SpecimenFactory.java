package com.github.nylle.javafixture;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.MonthDay;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.Temporal;

import com.github.nylle.javafixture.specimen.ArraySpecimen;
import com.github.nylle.javafixture.specimen.CollectionSpecimen;
import com.github.nylle.javafixture.specimen.EnumSpecimen;
import com.github.nylle.javafixture.specimen.InterfaceSpecimen;
import com.github.nylle.javafixture.specimen.MapSpecimen;
import com.github.nylle.javafixture.specimen.ObjectSpecimen;
import com.github.nylle.javafixture.specimen.PrimitiveSpecimen;
import com.github.nylle.javafixture.specimen.TemporalSpecimen;
import com.github.nylle.javafixture.specimen.TimeSpecimen;

public class SpecimenFactory {

    private final Context context;

    public SpecimenFactory(Context context) {
        this.context = context;
    }

    public <T> ISpecimen<T> build(final Class<T> type) {

        if(type.isPrimitive() || ReflectionHelper.isBoxedOrString(type)) {
            return new PrimitiveSpecimen<>(type);
        }

        if(type.isEnum()) {
            return new EnumSpecimen<>(type);
        }

        if(ReflectionHelper.isMap(type)) {
            return new MapSpecimen<>(type, null, null, context, this);
        }

        if(ReflectionHelper.isCollection(type)) {
            return new CollectionSpecimen<>(type, null, context, this);
        }

        if(type.isArray()) {
            return new ArraySpecimen<>(type, context, this);
        }

        if(type.isInterface()) {
            return new InterfaceSpecimen<>(type, context, this);
        }
        if(Temporal.class.isAssignableFrom(type)) {
            return new TemporalSpecimen<>( type, context );
        }
        if(ReflectionHelper.isTimeType( type)) {
            return new TimeSpecimen<>( type );
        }

        return new ObjectSpecimen<>(type, context, this);
    }

    private static boolean isTimeSpecimen( Class type ) {
        if(type.equals(Duration.class)) {
            return true;
        }
        if(type.equals(Period.class)) {
            return true;
        }

        if(type.equals(MonthDay.class)) {
            return true;
        }

        if(type.equals(ZoneId.class)) {
            return true;
        }

        if(type.equals(ZoneOffset.class)) {
            return true;
        }
        return false;
    }

    public <T> ISpecimen<T> build(final Class<T> type, final Type genericType) {

        if(ReflectionHelper.isCollection(type)) {
            return new CollectionSpecimen<>(type, ReflectionHelper.getGenericType(genericType, 0), context, this);
        }

        if(ReflectionHelper.isMap(type)) {
            return new MapSpecimen<>(type, ReflectionHelper.getGenericType(genericType, 0), ReflectionHelper.getGenericType(genericType, 1), context, this);
        }

        throw new SpecimenException(String.format("Unsupported type for generic creation: %s", type));
    }

}

