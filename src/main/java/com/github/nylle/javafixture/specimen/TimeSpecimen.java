package com.github.nylle.javafixture.specimen;

import com.github.nylle.javafixture.Context;
import com.github.nylle.javafixture.ISpecimen;
import com.github.nylle.javafixture.ReflectionHelper;
import com.github.nylle.javafixture.SpecimenException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Clock;
import java.time.Duration;
import java.time.MonthDay;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.JapaneseEra;
import java.time.temporal.Temporal;
import java.util.Random;

public class TimeSpecimen<T> implements ISpecimen<T> {

    private final Class<T> type;
    private final Random random;
    private final Context context;

    public TimeSpecimen(Class<T> type, Context context) {
        this.context = context;
        if (type == null) {
            throw new IllegalArgumentException("type: null");
        }

        if (!ReflectionHelper.isTimeType(type)) {
            throw new IllegalArgumentException("type: " + type.getName());
        }

        if (context == null) {
            throw new IllegalArgumentException("context: null");
        }

        this.type = type;
        this.random = new Random();
    }

    @Override
    public T create() {
        if (Temporal.class.isAssignableFrom(type)) {
            try {
                Method now = type.getMethod("now", Clock.class);
                return (T) now.invoke(null, context.getConfiguration().getClock());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new SpecimenException("Unsupported type: " + type);
            }
        }
        if (type.equals(MonthDay.class)) {
            return (T) MonthDay.now(context.getConfiguration().getClock());
        }

        if (type.equals(JapaneseEra.class)) {
            return (T) JapaneseEra.values()[random.nextInt(JapaneseEra.values().length)];
        }

        if (type.equals(ZoneOffset.class)) {
            return (T) ZoneOffset.ofHours(new Random().nextInt(19));
        }
        if (type.equals(Duration.class)) {
            return (T) Duration.ofDays(random.nextInt());
        }

        if (type.equals(Period.class)) {
            return (T) Period.ofDays(random.nextInt());
        }

        if (type.equals(ZoneId.class)) {
            return (T) ZoneId.of(ZoneId.getAvailableZoneIds().iterator().next());
        }

        throw new SpecimenException("Unsupported type: " + type);
    }
}
