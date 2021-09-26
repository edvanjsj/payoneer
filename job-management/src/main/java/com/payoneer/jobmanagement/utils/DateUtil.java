package com.payoneer.jobmanagement.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String toString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.format(FORMATTER);
    }

    public static LocalDateTime toLocalDateTime(String dateTime) {
        if (dateTime == null || dateTime.isBlank()) {
            return null;
        }

        return LocalDateTime.parse(dateTime, FORMATTER);
    }
}
