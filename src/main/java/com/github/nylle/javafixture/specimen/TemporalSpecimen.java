package com.github.nylle.javafixture.specimen;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.temporal.Temporal;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.SpecimenException;


public class TemporalSpecimen<T> implements ISpecimen<T> {

    private final Class<T> type;
    private final Context context;

    public TemporalSpecimen( Class<T> type, Context context ) {
        if ( type == null ) {
            throw new IllegalArgumentException( "type: null" );
        }

        if ( !Temporal.class.isAssignableFrom( type ) ) {
            throw new IllegalArgumentException( "type: " + type.getName() );
        }

        if ( context == null ) {
            throw new IllegalArgumentException( "context: null" );
        }

        this.type = type;
        this.context = context;
    }

    @Override
    public T create() {
        try {
            Method now = type.getMethod( "now" );
            return (T) now.invoke( null );
        } catch ( NoSuchMethodException | IllegalAccessException | InvocationTargetException e ) {
            throw new SpecimenException( "Unsupported type: " + type );
        }

    }
}
