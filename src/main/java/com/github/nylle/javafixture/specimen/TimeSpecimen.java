package com.github.nylle.javafixture.specimen;

import java.time.Duration;
import java.time.MonthDay;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.JapaneseEra;
import java.util.Random;

import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.ReflectionHelper;
import com.github.nylle.javafixture.SpecimenException;

public class TimeSpecimen<T> implements ISpecimen<T> {

    private final Class<T> type;
    private final Random random;

    public TimeSpecimen( Class<T> type ) {
        if ( type == null ) {
            throw new IllegalArgumentException( "type: null" );
        }

        if ( !ReflectionHelper.isTimeType( type ) ) {
            throw new IllegalArgumentException( "type: " + type.getName() );
        }

        this.type = type;
        this.random = new Random();
    }

    @Override
    public T create() {
        if ( type.equals( MonthDay.class ) ) {
            return (T) MonthDay.now();
        }

        if ( type.equals( JapaneseEra.class ) ) {
            return (T) JapaneseEra.values()[random.nextInt( JapaneseEra.values().length )];
        }

        if ( type.equals( ZoneOffset.class ) ) {
            return (T) ZoneOffset.ofHours( new Random().nextInt( 19 ) );
        }
        if ( type.equals( Duration.class ) ) {
            return (T) Duration.ofDays( random.nextInt() );
        }

        if ( type.equals( Period.class ) ) {
            return (T) Period.ofDays( random.nextInt() );
        }

        if ( type.equals( ZoneId.class ) ) {
            return (T) ZoneId.of( ZoneId.getAvailableZoneIds().iterator().next() );
        }

        throw new SpecimenException( "Unsupported type: " + type );
    }
}
