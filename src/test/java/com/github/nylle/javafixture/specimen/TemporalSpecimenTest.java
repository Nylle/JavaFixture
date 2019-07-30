package com.github.nylle.javafixture.specimen;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.time.temporal.Temporal;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenFactory;
import com.github.nylle.javafixture.parameterized.TestCase;
import com.github.nylle.javafixture.parameterized.TestWithCases;

class TemporalSpecimenTest {

    private Context context;
    private SpecimenFactory specimenFactory;

    @BeforeEach
    void setup() {
        context = new Context( new Configuration( 2, 2, 3 ) );
        specimenFactory = new SpecimenFactory( context );
    }

    @Test
    void onlyTemporalTypes() {
        assertThatThrownBy( () -> new TemporalSpecimen( Map.class, context ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "type: " + Map.class.getName() );
    }

    @Test
    void typeIsRequired() {
        assertThatThrownBy( () -> new TemporalSpecimen<>( null, context ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "type: null" );
    }

    @Test
    void contextIsRequired() {
        assertThatThrownBy( () -> new TemporalSpecimen<>( Instant.class, null ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "context: null" );
    }

    @Test
    void throwsExceptionForUnknownType() {
        assertThatThrownBy( () -> new TemporalSpecimen<>( Mockito.mock( Temporal.class ).getClass(), context ).create() )
                .isInstanceOf( SpecimenException.class )
                .hasMessageContaining( "Unsupported type:" );
    }

    @TestWithCases
    @TestCase(class1 = Instant.class)
    @TestCase(class1 = HijrahDate.class)
    @TestCase(class1 = JapaneseDate.class)
    @TestCase(class1 = LocalDate.class)
    @TestCase(class1 = LocalDateTime.class)
    @TestCase(class1 = LocalTime.class)
    @TestCase(class1 = MinguoDate.class)
    @TestCase(class1 = OffsetDateTime.class)
    @TestCase(class1 = OffsetTime.class)
    @TestCase(class1 = ThaiBuddhistDate.class)
    @TestCase(class1 = Year.class)
    @TestCase(class1 = YearMonth.class)
    @TestCase(class1 = ZonedDateTime.class)
    @DisplayName( "create should return a valid object" )
    void create( Class type ) {
        var sut = new TemporalSpecimen<>( type, context ).create();
        // if the object is not valid, the toString method will fail, is cannot print it
        assertThat( sut.toString()).isNotEmpty();
    }

}