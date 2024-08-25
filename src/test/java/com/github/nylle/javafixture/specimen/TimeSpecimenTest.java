package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Configuration;
import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.SpecimenException;
import com.github.nylle.javafixture.SpecimenType;
import com.github.nylle.javafixture.annotations.testcases.TestCase;
import com.github.nylle.javafixture.annotations.testcases.TestWithCases;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.annotation.Annotation;
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
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;
import java.util.Map;

import static com.github.nylle.javafixture.CustomizationContext.noContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
        assertThatThrownBy(() -> new TimeSpecimen<>(SpecimenType.fromClass(Map.class), context))
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
        var sut = new TimeSpecimen<>(SpecimenType.fromClass(type), context);

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isInstanceOf(type);
        assertThat(actual.toString()).isNotEmpty();
    }

    @Test
    void throwsExceptionForUnknownType() {
        assertThatThrownBy(() -> new TimeSpecimen<>(SpecimenType.fromClass(TemporalAmount.class), context).create(noContext(), new Annotation[0]))
                .isInstanceOf(SpecimenException.class)
                .hasMessageContaining("Unsupported type:");
    }

    @Test
    void contextIsRequired() {
        assertThatThrownBy(() -> new TimeSpecimen<>(SpecimenType.fromClass(Instant.class), null))
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
    @TestCase(class1 = java.sql.Date.class)
    @TestCase(class1 = java.util.Date.class)
    @DisplayName("create should return a valid object")
    void create(Class type) {
        var sut = new TimeSpecimen<>(SpecimenType.fromClass(type), context).create(noContext(), new Annotation[0]);
        // if the object is not valid, the toString method will fail, it cannot print it
        assertThat(sut.toString()).isNotEmpty();
    }

    @Test
    void createWithClock() {
        Clock mockClock = Mockito.mock(Clock.class);
        when(mockClock.instant()).thenReturn(Instant.MIN);
        var context = new Context(Configuration.configure().clock(mockClock));

        var sut = new TimeSpecimen<>(SpecimenType.fromClass(Instant.class), context);

        var actual = sut.create(noContext(), new Annotation[0]);

        assertThat(actual).isEqualTo(Instant.MIN);
    }

    @TestWithCases
    @TestCase(class1 = String.class, bool2 = false)
    @TestCase(class1 = Object.class, bool2 = false)
    @TestCase(class1 = Temporal.class, bool2 = true)
    @TestCase(class1 = TemporalAdjuster.class, bool2 = true)
    @TestCase(class1 = TemporalAmount.class, bool2 = true)
    @TestCase(class1 = ZoneId.class, bool2 = true)
    @TestCase(class1 = java.util.Date.class, bool2 = true)
    @TestCase(class1 = java.sql.Date.class, bool2 = true)
    void supportsType(Class<?> type, boolean expected) {
        assertThat(TimeSpecimen.supportsType(SpecimenType.fromClass(type))).isEqualTo(expected);
    }

    @Nested
    class SpecTest {

        @TestWithCases
        @TestCase(class1 = String.class, bool2 = false)
        @TestCase(class1 = Temporal.class, bool2 = true)
        @TestCase(class1 = TemporalAdjuster.class, bool2 = true)
        @TestCase(class1 = TemporalAmount.class, bool2 = true)
        @TestCase(class1 = ZoneId.class, bool2 = true)
        @TestCase(class1 = java.util.Date.class, bool2 = true)
        @TestCase(class1 = java.sql.Date.class, bool2 = true)
        @TestCase(class1 = Duration.class, bool2 = true)
        @TestCase(class1 = JapaneseEra.class, bool2 = true)
        @TestCase(class1 = MonthDay.class, bool2 = true)
        @TestCase(class1 = Period.class, bool2 = true)
        @TestCase(class1 = ZoneId.class, bool2 = true)
        @TestCase(class1 = ZoneOffset.class, bool2 = true)
        @TestCase(class1 = ZonedDateTime.class, bool2 = true)
        void supports(Class<?> type, boolean expected) {
            assertThat(TimeSpecimen.meta().supports(SpecimenType.fromClass(type))).isEqualTo(expected);
        }

        @TestWithCases
        @TestCase(class1 = Temporal.class)
        @TestCase(class1 = TemporalAdjuster.class)
        @TestCase(class1 = TemporalAmount.class)
        @TestCase(class1 = ZoneId.class)
        @TestCase(class1 = java.util.Date.class)
        @TestCase(class1 = java.sql.Date.class)
        void createReturnsNewSpecimen(Class<?> type) {
            assertThat(TimeSpecimen.meta().create(SpecimenType.fromClass(type), context, null))
                    .isInstanceOf(TimeSpecimen.class);
        }

        @Test
        void createThrows() {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> TimeSpecimen.meta().create(SpecimenType.fromClass(String.class), context, null))
                    .withMessageContaining("type: java.lang.String");
        }
    }
}
