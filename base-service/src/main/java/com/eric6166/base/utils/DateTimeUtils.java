package com.eric6166.base.utils;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

public final class DateTimeUtils {

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);

    public static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"; //change
    public static final String DEFAULT_ZONED_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSVV"; //change
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN);

    public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss.SSSSSS";
    public static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN);

    public static final ZoneId DEFAULT_ZONE_ID = ZoneOffset.UTC;

    private DateTimeUtils() {
        throw new IllegalStateException("Utility class");
    }


    public static LocalDateTime toLocalDateTime(String dateTime, String pattern) {
        return toLocalDateTime(dateTime, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * The text is parsed using the pattern, returning a date-time.
     *
     * @param dateTime  the text to parse, not null
     * @param formatter the formatter to use, not null
     * @return the parsed local date-time, not null
     * @throws IllegalArgumentException if the text cannot be parsed
     */
    public static LocalDateTime toLocalDateTime(String dateTime, DateTimeFormatter formatter) {
        try {
            return LocalDateTime.parse(dateTime, formatter);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the text cannot be parsed, dateTime '%s'", dateTime));
        }
    }

    public static Optional<LocalDateTime> toOptionalLocalDateTime(String dateTime, String pattern) {
        try {
            return Optional.of(toLocalDateTime(dateTime, pattern));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<LocalDateTime> toOptionalLocalDateTime(String dateTime, DateTimeFormatter formatter) {
        try {
            return Optional.of(toLocalDateTime(dateTime, formatter));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static LocalDate toLocalDate(String date, String pattern) {
        return toLocalDate(date, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate toLocalDate(String date, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the text cannot be parsed, date '%s'"));
        }
    }

    public static Optional<LocalDate> toOptionalLocalDate(String date, String pattern) {
        try {
            return Optional.of(toLocalDate(date, pattern));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<LocalDate> toOptionalLocalDate(String date, DateTimeFormatter formatter) {
        try {
            return Optional.of(toLocalDate(date, formatter));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static LocalTime toLocalTime(String time, String pattern) {
        return toLocalTime(time, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalTime toLocalTime(String time, DateTimeFormatter formatter) {
        try {
            return LocalTime.parse(time, formatter);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the text cannot be parsed, time '%s'", time));
        }
    }

    public static Optional<LocalTime> toOptionalLocalTime(String time, DateTimeFormatter formatter) {
        try {
            return Optional.of(toLocalTime(time, formatter));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<LocalTime> toOptionalLocalTime(String time, String pattern) {
        try {
            return Optional.of(toLocalTime(time, pattern));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static ZonedDateTime toZonedDateTime(String time, String pattern) {
        return toZonedDateTime(time, DateTimeFormatter.ofPattern(pattern));
    }

    public static ZonedDateTime toZonedDateTime(String time, DateTimeFormatter formatter) {
        try {
            return ZonedDateTime.parse(time, formatter);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the text cannot be parsed, time '%s'", time));
        }
    }

    public static Optional<ZonedDateTime> toOptionalZonedDateTime(String time, DateTimeFormatter formatter) {
        try {
            return Optional.of(toZonedDateTime(time, formatter));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<ZonedDateTime> toOptionalZonedDateTime(String time, String pattern) {
        try {
            return Optional.of(toZonedDateTime(time, pattern));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static String toString(ZonedDateTime dateTime, String pattern) {
        return toString(dateTime, DateTimeFormatter.ofPattern(pattern));
    }

    public static String toString(ZonedDateTime dateTime, DateTimeFormatter formatter) {
        try {
            return dateTime.format(formatter);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the dateTime cannot be formatted, dateTime '%s'", dateTime));
        }
    }

    public static Optional<String> toOptionalString(ZonedDateTime dateTime, DateTimeFormatter formatter) {
        try {
            return Optional.of(toString(dateTime, formatter));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> toOptionalString(ZonedDateTime dateTime, String pattern) {
        try {
            return Optional.of(toString(dateTime, pattern));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> toOptionalString(LocalDateTime dateTime, String pattern) {
        try {
            return Optional.of(toString(dateTime, pattern));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static String toString(LocalDateTime dateTime, DateTimeFormatter formatter) {
        try {
            return dateTime.format(formatter);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the dateTime cannot be formatted, dateTime '%s'", dateTime));
        }
    }

    public static Optional<String> toOptionalString(LocalDateTime dateTime, DateTimeFormatter formatter) {
        try {
            return Optional.of(toString(dateTime, formatter));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static String toString(LocalDateTime dateTime, String pattern) {
        return toString(dateTime, DateTimeFormatter.ofPattern(pattern));
    }


    public static String toString(LocalDate date, String pattern) {
        return toString(date, DateTimeFormatter.ofPattern(pattern));
    }

    public static String toString(LocalDate date, DateTimeFormatter formatter) {
        try {
            return date.format(formatter);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the date cannot be formatted, date '%s'", date));
        }
    }

    public static Optional<String> toOptionalString(LocalDate date, String pattern) {
        try {
            return Optional.of(toString(date, pattern));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> toOptionalString(LocalDate date, DateTimeFormatter formatter) {
        try {
            return Optional.of(toString(date, formatter));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static String toString(LocalTime time, String pattern) {
        return toString(time, DateTimeFormatter.ofPattern(pattern));
    }

    public static String toString(LocalTime time, DateTimeFormatter formatter) {
        try {
            return time.format(formatter);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the time cannot be formatted, time '%s'", time));
        }
    }

    public static Optional<String> toOptionalString(LocalTime time, DateTimeFormatter formatter) {
        try {
            return Optional.of(toString(time, formatter));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> toOptionalString(LocalTime time, String pattern) {
        try {
            return Optional.of(toString(time, pattern));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static TemporalAccessor toTemporalAccessor(String dateTime, String pattern) {
        return toTemporalAccessor(dateTime, DateTimeFormatter.ofPattern(pattern));

    }

    public static Optional<TemporalAccessor> toOptionalTemporalAccessor(String dateTime, String pattern) {
        try {
            return Optional.of(toTemporalAccessor(dateTime, pattern));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }

    }

    public static TemporalAccessor toTemporalAccessor(String dateTime, DateTimeFormatter formatter) {
        try {
            return formatter.parse(dateTime);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the dateTime cannot be parsed, dateTime '%s'", dateTime));
        }

    }

    public static Optional<TemporalAccessor> toOptionalTemporalAccessor(String dateTime, DateTimeFormatter formatter) {
        try {
            return Optional.of(toTemporalAccessor(dateTime, formatter));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }

    }


}
