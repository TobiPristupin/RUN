package com.tobipristupin.simplerun.utils;

import org.joda.time.DateTime;

/**
 * Created by Tobi on 11/5/2017.
 */

public class DateUtils {


    /**
     *
     * @return first day of current week in seconds since epoch
     */
    public static long getStartOfWeek(){
        long timestamp = new DateTime().
                dayOfWeek().withMinimumValue()
                .withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).getMillis() / 1000;
        return timestamp;
    }

    /**
     *
     * @return last day of current week in seconds since epoch
     */
    public static long getEndOfWeek(){
        long timestamp =  new DateTime().
                dayOfWeek().withMaximumValue().withHourOfDay(23)
                .withMinuteOfHour(59).withSecondOfMinute(59).getMillis() / 1000;
        return timestamp;
    }

    /**
     *
     * @return First day of current month in seconds since epoch
     */
    public static long getStartOfMonth(){
        return getStartOfMonth(0);
    }

    /**
     * Starting from the current month, it adds plusMonths to it and returns the first day of the resulting month.
     * plusMonths may be negative, and instead of adding months, they will be subtracted.
     * @param plusMonths Months to add. Will subtract if negative
     */
    public static long getStartOfMonth(int plusMonths){
        long timestamp = new DateTime().plusMonths(plusMonths).dayOfMonth().withMinimumValue()
                .withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).getMillis() / 1000;

        return timestamp;
    }


    /**
     * Starting from the current month, it adds plusMonths to it and returns the last day of the resulting month.
     * plusMonths may be negative, and instead of adding months, they will be subtracted.
     * @param plusMonths Months to add. Will subtract if negative
     */
    public static long getEndOfMonth(int plusMonths){
        long timestamp = new DateTime().plusMonths(plusMonths).dayOfMonth().withMaximumValue()
                .withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).getMillis() / 1000;

        return timestamp;
    }

    /**
     *
     * @return Last day of current month in seconds since epoch
     */
    public static long getEndOfMonth(){
        return getEndOfMonth(0);
    }

    /**
     *
     * @return First day of current year
     */
    public static long getStartOfYear(){
        return getStartOfYearStartOfMonth(0);
    }


    /**
     * Starting from the first day of current year, it adds plusMonths months to it and returns the first day of the resulting month.
     * plusMonths may be negative, and instead of adding months, they will be subtracted.
     * @param plusMonths Months to add. Will subtract if negative
     */
    public static long getStartOfYearStartOfMonth(int plusMonths){
        long timestamp = new DateTime().
                dayOfYear().withMinimumValue()
                .withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).plusMonths(plusMonths).getMillis() / 1000;
        return timestamp;
    }

    /**
     * Starting from the first day of current year, it adds plusMonths months to it and returns the last day of the resulting month.
     * plusMonths may be negative, and instead of adding months, they will be subtracted.
     * @param plusMonths Months to add. Will subtract if negative
     */
    public static long getStartOfYearEndOfMonth(int plusMonths){
        long timestamp = new DateTime().
                dayOfYear().withMinimumValue().dayOfMonth().withMaximumValue()
                .withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).plusMonths(plusMonths).getMillis() / 1000;
        return timestamp;
    }


    /**
     *
     * @return Last day of current year in seconds since epoch
     */
    public static long getEndOfYear(){
        long timestamp = new DateTime().
                dayOfYear().withMaximumValue()
        .withHourOfDay(23).withMinuteOfHour(23).withSecondOfMinute(23).getMillis() / 1000;
        return timestamp;
    }

    /**
     *
     * @return First day of last year in seconds since epoch
     */
    public static long getStartOfLastYear(){
        long timestamp = new DateTime().
                dayOfYear().withMinimumValue()
                .withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).minusYears(1).getMillis() / 1000;
        return timestamp;
    }

    /**
     *
     * @return Last day of last year in seconds since epoch
     */
    public static long getEndOfLastYear(){
        long timestamp = new DateTime().
                dayOfYear().withMaximumValue()
                .withHourOfDay(23).withMinuteOfHour(23).withSecondOfMinute(23).minusYears(1).getMillis() / 1000;
        return timestamp;
    }

    /**
     * Starting from first day of last year, adds plusMonths months to it and returns the first day
     * of that given month. plusMonths may be negative, and instead of adding months, they will be subtracted.
     * @param plusMonths Months to add. Will subtract if negative
     * @return
     */
    public static long getLastYearStartOfMonth(int plusMonths){
        long timestamp = new DateTime().
                dayOfYear().withMinimumValue()
                .withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).minusYears(1).plusMonths(plusMonths).getMillis() / 1000;
        return timestamp;
    }

    /**
     * Starting from first day of last year, adds plusMonths months to it and returns the last day
     * of that given month. plusMonths may be negative, and instead of adding months, they will be subtracted.
     * @param plusMonths Months to add. Will subtract if negative
     * @return
     */
    public static long getLastYearEndOfMonth(int plusMonths){
        long timestamp = new DateTime().
                dayOfYear().withMinimumValue().minusYears(1).plusMonths(plusMonths).dayOfMonth().withMaximumValue()
                .withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).getMillis() / 1000;
        return timestamp;
    }
}
