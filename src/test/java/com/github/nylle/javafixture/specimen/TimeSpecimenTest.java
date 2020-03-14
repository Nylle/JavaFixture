package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.generic.FixtureType;
import com.github.nylle.javafixture.parameterized.TestCase;
import com.github.nylle.javafixture.parameterized.TestWithCases;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.JapaneseEra;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.time.temporal.TemporalAmount;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class TimeSpecimenTest {

    private Context context;

    @BeforeEach
    void setup() {
        context = new Context(Configuration.configure());
    }

    @Test
    void onlyTemporalTypes() {
        assertThatThrownBy(() -> new TimeSpecimen<>(FixtureType.fromClass(Map.class), context))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: " + Map.class.getName());
    }

    @Test
    void typeIsRequired() {
        assertThatThrownBy(() -> new TimeSpecimen<>(null, context))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type: null");
    }

    @TestWithCases
    @TestCase(class1 = Duration.class)
    @TestCase(class1 = JapaneseEra.class)
    @TestCase(class1 = MonthDay.class)
    @TestCase(class1 = Period.class)
    @TestCase(class1 = ZoneId.class)
    @TestCase(class1 = ZoneOffset.class)
    void createDuration(Class type) {
        var sut = new TimeSpecimen<>(FixtureType.fromClass(type), context);

        var actual = sut.create();

        assertThat(actual).isInstanceOf(type);
        assertThat(actual.toString()).isNotEmpty();
    }

    @Test
    void throwsExceptionForUnknownType() {
        assertThatThrownBy(() -> new TimeSpecimen<>(FixtureType.fromClass(TemporalAmount.class), context).create())
                .isInstanceOf(SpecimenException.class)
                .hasMessageContaining("Unsupported type:");
    }

    @Test
    void contextIsRequired() {
        assertThatThrownBy(() -> new TimeSpecimen<>(FixtureType.fromClass(Instant.class), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("context: null");
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
    @DisplayName("create should return a valid object")
    void create(Class type) {
        var sut = new TimeSpecimen<>(FixtureType.fromClass(type), context).create();
        // if the object is not valid, the toString method will fail, is cannot print it
        assertThat(sut.toString()).isNotEmpty();
    }

    @Test
    void createWithClock() {
        Clock mockClock = Mockito.mock(Clock.class);
        when(mockClock.instant()).thenReturn(Instant.MIN);
        context = new Context(Configuration.configure().clock(mockClock));

        var sut = new TimeSpecimen<>(FixtureType.fromClass(Instant.class), context);

        var actual = sut.create();

        assertThat(actual).isEqualTo(Instant.MIN);
    }
}