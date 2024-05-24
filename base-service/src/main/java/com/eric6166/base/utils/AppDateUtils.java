package com.eric6166.base.utils;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public final class AppDateUtils {

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd"; //change
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE; //change

    public static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"; //change
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN);

    public static final String DEFAULT_TIME_ZONE_ID_STRING = "UTC"; //change
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of(DEFAULT_TIME_ZONE_ID_STRING); //change

    private AppDateUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static LocalDateTime toLocalDateTime(String dateTime, String pattern) {
        try {

            return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(String.format("the text cannot be parse, dateTime '%s', pattern '%s'", dateTime, pattern));
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
        } catch (DateTimeException e) {
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
        } catch (DateTimeException e) {
            return Optional.empty();
        }
    }


}
