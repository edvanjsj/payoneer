package com.payoneer.jobmanagement.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class DateUtilTest {
    @Test
    void toLocalDateTime_withNullString_shouldReturnNull() {
        LocalDateTime dateTime = DateUtil.toLocalDateTime(null);
        Assertions.assertThat(dateTime).isNull();
    }

    @Test
    void toLocalDateTime_withEmptyString_shouldReturnNull() {
        LocalDateTime dateTime = DateUtil.toLocalDateTime("");
        Assertions.assertThat(dateTime).isNull();
    }

    @Test
    void toLocalDateTime_withInvalidFormat_shouldReturnNull() {
        LocalDateTime dateTime = DateUtil.toLocalDateTime("12/03/2021");
        Assertions.assertThat(dateTime).isNull();
    }

    @Test
    void toLocalDateTime_withValidFormat_shouldReturnLocalDateTime() {
        LocalDateTime dateTime = DateUtil.toLocalDateTime("2021-03-12 22:23:15");
        Assertions.assertThat(dateTime).isNotNull();
    }

    @Test
    void toString_withNullParam_shouldReturnNull() {
        String date = DateUtil.toString(null);
        Assertions.assertThat(date).isNull();
    }

    @Test
    void toString_withValidParam_shouldReturnStringDate() {
        String date = DateUtil.toString(LocalDateTime.of(2021, 03, 12,22, 23));
        Assertions.assertThat(date).isEqualTo("2021-03-12 22:23:00");
    }
}