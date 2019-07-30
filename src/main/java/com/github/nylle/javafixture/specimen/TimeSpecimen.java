package com.github.nylle.javafixture.specimen;

import java.time.Duration;
import java.time.MonthDay;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAmount;
import java.util.Random;

import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.SpecimenException;

public class TimeSpecimen<T> implements ISpecimen<T> {

    private final Class<T> type;
    private final Random random;

    public TimeSpecimen( Class<T> type ) {
        if ( type == null ) {
            throw new IllegalArgumentException( "type: null" );
        }

        if ( !isTimeClass( type ) ) {
            throw new IllegalArgumentException( "type: " + type.getName() );
        }

        this.type = type;
        this.random = new Random();
    }

    private static boolean isTimeClass( Class type ) {
        if ( TemporalAmount.class.isAssignableFrom( type ) ) {
            return true;
        }
        if ( type.equals( MonthDay.class ) ) {
            return true;
        }
        if ( type.equals( ZoneId.class ) ) {
            return true;
        }
        if ( type.equals( ZoneOffset.class ) ) {
            return true;
        }
        return false;
    }

    @Override
    public T create() {
        if ( type.equals( Duration.class ) ) {
            return (T) Duration.ofDays( random.nextInt() );
        }

        if ( type.equals( Period.class ) ) {
            return (T) Period.ofDays( random.nextInt() );
        }

        if ( type.equals( MonthDay.class ) ) {
            return (T) MonthDay.now();
        }

        if ( type.equals( ZoneId.class ) ) {
            return (T) ZoneId.of( ZoneId.getAvailableZoneIds().iterator().next() );
        }

        if ( type.equals( ZoneOffset.class ) ) {
            return (T) ZoneOffset.ofHours( new Random().nextInt( 19 ) );
        }

        throw new SpecimenException( "Unsupported type: " + type );
    }
}
