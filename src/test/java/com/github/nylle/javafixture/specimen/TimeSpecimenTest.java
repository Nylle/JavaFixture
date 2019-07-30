package com.github.nylle.javafixture.specimen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import java.time.MonthDay;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAmount;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.parameterized.TestCase;
import com.github.nylle.javafixture.parameterized.TestWithCases;

class TimeSpecimenTest {

    @Test
    void onlyTemporalTypes() {
        assertThatThrownBy( () -> new TimeSpecimen( Map.class ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "type: " + Map.class.getName() );
    }

    @Test
    void typeIsRequired() {
        assertThatThrownBy( () -> new TimeSpecimen<>( null ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "type: null" );
    }

    @TestWithCases
    @TestCase(class1 = Duration.class)
    @TestCase(class1 = Period.class)
    @TestCase(class1 = MonthDay.class)
    @TestCase(class1 = ZoneId.class)
    @TestCase(class1 = ZoneOffset.class)
    void createDuration(Class type) {
        var sut = new TimeSpecimen<>( type );

        var actual = sut.create();

        assertThat(actual).isInstanceOf(type);
        assertThat(actual.toString()).isNotEmpty();
    }

    @Test
    void throwsExceptionForUnknownType() {
        assertThatThrownBy( () -> new TimeSpecimen<>( Mockito.mock( TemporalAmount.class ).getClass() ).create() )
                .isInstanceOf( SpecimenException.class )
                .hasMessageContaining( "Unsupported type:" );
    }
}