package com.banchango.tools;

import java.time.LocalDateTime;

public class DateConverter {

    public static String convertDateWithoutTime(LocalDateTime localDateTime) {
        return localDateTime.getYear() + "년 " +
                localDateTime.getMonthValue() + "월 " +
                localDateTime.getDayOfMonth() + "일 ";
    }

    public static String convertDateWithTime(LocalDateTime localDateTime) {
        return localDateTime.getYear() + "-" +
            localDateTime.getMonthValue() + "-" +
            localDateTime.getDayOfMonth() + " " +
            localDateTime.getHour() + ":" +
            localDateTime.getMinute();
    }
}
