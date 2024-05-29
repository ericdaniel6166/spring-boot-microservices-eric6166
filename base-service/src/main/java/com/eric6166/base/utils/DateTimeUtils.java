package com.eric6166.base.utils;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

public final class DateTimeUtils {

    public static final String DEFAULT_LOCAL_DATE_PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter DEFAULT_LOCAL_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static final String DEFAULT_LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";
    public static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSVV";
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME ;
    public static final DateTimeFormatter DEFAULT_OFFSET_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    public static final DateTimeFormatter DEFAULT_LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static final String DEFAULT_LOCAL_TIME_PATTERN = "HH:mm:ss.SSSSSS";
    public static final DateTimeFormatter DEFAULT_LOCAL_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

    public static final ZoneId DEFAULT_ZONE_ID = ZoneOffset.UTC;
    public static final DateTimeFormatter DEFAULT_OFFSET_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_TIME;

    private DateTimeUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static OffsetTime toOffsetTime(String text, DateTimeFormatter formatter) {
        try {
            return OffsetTime.parse(text, formatter);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the text cannot be parsed, text '%s'", text));
        }
    }

    public static String toString(OffsetTime time, DateTimeFormatter formatter) {
        try {
            return time.format(formatter);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the time cannot be formatted, time '%s'", time));
        }
    }

    public static OffsetDateTime toOffsetDateTime(String text, DateTimeFormatter formatter) {
        try {
            return OffsetDateTime.parse(text, formatter);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the text cannot be parsed, text '%s'", text));
        }
    }

    public static String toString(OffsetDateTime dateTime, DateTimeFormatter formatter) {
        try {
            return dateTime.format(formatter);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the dateTime cannot be formatted, dateTime '%s'", dateTime));
        }
    }

    public static LocalDateTime toLocalDateTime(String text, String pattern) {
        return toLocalDateTime(text, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * The text is parsed using the pattern, returning a date-time.
     *
     * @param text  the text to parse, not null
     * @param formatter the formatter to use, not null
     * @return the parsed local date-time, not null
     * @throws IllegalArgumentException if the text cannot be parsed
     */
    public static LocalDateTime toLocalDateTime(String text, DateTimeFormatter formatter) {
        try {
            return LocalDateTime.parse(text, formatter);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the text cannot be parsed, text '%s'", text));
        }
    }

    public static Optional<LocalDateTime> toOptionalLocalDateTime(String text, String pattern) {
        try {
            return Optional.of(toLocalDateTime(text, pattern));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<LocalDateTime> toOptionalLocalDateTime(String text, DateTimeFormatter formatter) {
        try {
            return Optional.of(toLocalDateTime(text, formatter));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static LocalDate toLocalDate(String text, String pattern) {
        return toLocalDate(text, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate toLocalDate(String text, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(text, formatter);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the text cannot be parsed, text '%s'"));
        }
    }

    public static Optional<LocalDate> toOptionalLocalDate(String text, String pattern) {
        try {
            return Optional.of(toLocalDate(text, pattern));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<LocalDate> toOptionalLocalDate(String text, DateTimeFormatter formatter) {
        try {
            return Optional.of(toLocalDate(text, formatter));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static LocalTime toLocalTime(String text, String pattern) {
        return toLocalTime(text, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalTime toLocalTime(String text, DateTimeFormatter formatter) {
        try {
            return LocalTime.parse(text, formatter);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the text cannot be parsed, text '%s'", text));
        }
    }

    public static Optional<LocalTime> toOptionalLocalTime(String text, DateTimeFormatter formatter) {
        try {
            return Optional.of(toLocalTime(text, formatter));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<LocalTime> toOptionalLocalTime(String text, String pattern) {
        try {
            return Optional.of(toLocalTime(text, pattern));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static ZonedDateTime toZonedDateTime(String text, String pattern) {
        return toZonedDateTime(text, DateTimeFormatter.ofPattern(pattern));
    }

    public static ZonedDateTime toZonedDateTime(String text, DateTimeFormatter formatter) {
        try {
            return ZonedDateTime.parse(text, formatter);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the text cannot be parsed, text '%s'", text));
        }
    }

    public static Optional<ZonedDateTime> toOptionalZonedDateTime(String text, DateTimeFormatter formatter) {
        try {
            return Optional.of(toZonedDateTime(text, formatter));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<ZonedDateTime> toOptionalZonedDateTime(String text, String pattern) {
        try {
            return Optional.of(toZonedDateTime(text, pattern));
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

    public static TemporalAccessor toTemporalAccessor(String text, String pattern) {
        return toTemporalAccessor(text, DateTimeFormatter.ofPattern(pattern));

    }

    public static Optional<TemporalAccessor> toOptionalTemporalAccessor(String text, String pattern) {
        try {
            return Optional.of(toTemporalAccessor(text, pattern));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }

    }

    public static TemporalAccessor toTemporalAccessor(String text, DateTimeFormatter formatter) {
        try {
            return formatter.parse(text);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the text cannot be parsed, text '%s'", text));
        }

    }

    public static Optional<TemporalAccessor> toOptionalTemporalAccessor(String text, DateTimeFormatter formatter) {
        try {
            return Optional.of(toTemporalAccessor(text, formatter));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }

    }

}
