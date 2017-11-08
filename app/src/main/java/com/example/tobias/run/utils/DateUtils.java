package com.example.tobias.run.utils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Created by Tobi on 11/5/2017.
 */

public class DateUtils {


    public static long getStartOfWeek(){
        LocalDate today = new LocalDate();
        long firstDayOfWeekTimestamp = new DateTime().
                dayOfWeek().withMinimumValue()
                .withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).getMillis() / 1000;
        return firstDayOfWeekTimestamp;
    }

    public static long getEndOfWeek(){
        long lastDayOfWeekTimestamp =  new DateTime().
                dayOfWeek().withMaximumValue().withHourOfDay(23)
                .withMinuteOfHour(59).withSecondOfMinute(59).getMillis() / 1000;
        return lastDayOfWeekTimestamp;
    }

    public static long getStartOfCurrentMonth(){
        long firstDayOfMonthTimestamp = new DateTime().
                dayOfMonth().withMinimumValue()
                .withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).getMillis() / 1000;
        return firstDayOfMonthTimestamp;
    }

    public static long getStartOfMonthMinusMonths(int minus){
        long timestamp = new DateTime().minusMonths(minus).dayOfMonth().withMinimumValue()
                .withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).getMillis() / 1000;

        return timestamp;
    }

    public static long getStartOfMonthPlusMonths(int plus){
        long timestamp = new DateTime().plusMonths(plus).dayOfMonth().withMinimumValue()
                .withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).getMillis() / 1000;

        return timestamp;
    }

    public static long getEndOfMonthPlusMonths(int plus){
        long timestamp = new DateTime().plusMonths(plus).dayOfMonth().withMaximumValue()
                .withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).getMillis() / 1000;

        return timestamp;
    }

    public static long getEndOfMonthMinusMonths(int minus){
        long timestamp = new DateTime().minusMonths(minus).dayOfMonth().withMaximumValue()
                .withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).getMillis() / 1000;

        return timestamp;
    }

    public static long getEndOfCurrentMonth(){
        long lastDayOfMonthTimestamp = new DateTime().
                dayOfMonth().withMaximumValue()
                .withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).getMillis() / 1000;
        return lastDayOfMonthTimestamp;
    }

    public static long getStartOfYear(){
        long firstDayOfYearTimestamp = new DateTime().
                dayOfYear().withMinimumValue()
                .withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).getMillis() / 1000;
        return firstDayOfYearTimestamp;
    }

    public static long getStartOfYearPlusMonths(int plus){
        long firstDayOfYearTimestamp = new DateTime().
                dayOfYear().withMinimumValue()
                .withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).plusMonths(plus).getMillis() / 1000;
        return firstDayOfYearTimestamp;
    }

    public static long getEndOfYear(){
        long lastDayOfYearTimestamp = new DateTime().
                dayOfYear().withMaximumValue()
        .withHourOfDay(23).withMinuteOfHour(23).withSecondOfMinute(23).getMillis() / 1000;
        return lastDayOfYearTimestamp;
    }
}
