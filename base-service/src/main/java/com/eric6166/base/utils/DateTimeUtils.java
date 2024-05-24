package com.eric6166.base.utils;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public final class DateTimeUtils {

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);

    public static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"; //change
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN);

    public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss.SSSSSS";
    public static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN);

    public static final String DEFAULT_ZONE_ID_STRING = "UTC"; //change
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of(DEFAULT_ZONE_ID_STRING);

    private DateTimeUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * The text is parsed using the pattern, returning a date-time.
     *
     * @param dateTime  the text to parse, not null
     * @param pattern the pattern to use, not null
     * @return the parsed local date-time, not null
     * @throws IllegalArgumentException if the text cannot be parsed
     */
    public static LocalDateTime toLocalDateTime(String dateTime, String pattern) {
        try {
            return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the text cannot be parsed, dateTime '%s', pattern '%s'", dateTime, pattern));
        }
    }

    public static Optional<LocalDateTime> toOptionalLocalDateTime(String dateTime, String pattern) {
        try {
            return Optional.of(toLocalDateTime(dateTime, pattern));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static LocalDate toLocalDate(String date, String pattern) {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the text cannot be parsed, date '%s', pattern '%s'", date, pattern));
        }
    }

    public static Optional<LocalDate> toOptionalLocalDate(String date, String pattern) {
        try {
            return Optional.of(toLocalDate(date, pattern));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static LocalTime toLocalTime(String time, String pattern) {
        try {
            return LocalTime.parse(time, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the text cannot be parsed, time '%s', pattern '%s'", time, pattern));
        }
    }

    public static Optional<LocalTime> toOptionalLocalTime(String time, String pattern) {
        try {
            return Optional.of(toLocalTime(time, pattern));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static String toString(LocalDateTime dateTime, String pattern) {
        try {
            return dateTime.format(DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the dateTime cannot be formatted, dateTime '%s', pattern '%s'", dateTime, pattern));
        }
    }

    public static Optional<String> toOptionalString(LocalDateTime dateTime, String pattern) {
        try {
            return Optional.of(toString(dateTime, pattern));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static String toString(LocalDate date, String pattern) {
        try {
            return date.format(DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the date cannot be formatted, date '%s', pattern '%s'", date, pattern));
        }
    }

    public static Optional<String> toOptionalString(LocalDate date, String pattern) {
        try {
            return Optional.of(toString(date, pattern));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }


}
