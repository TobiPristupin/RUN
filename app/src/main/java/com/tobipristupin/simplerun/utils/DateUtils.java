package com.tobipristupin.simplerun.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;


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


    /**
     * Joda time incorrectly formats month names and weekdays when using locales other than english.
     * Joda time produces strings with period at the end and without capitalizing. This method correctly reformats
     * those strings. If the string is already formatted correctly, such as when using english locale, nothing will be done.
     * @return formatted month or day string
     */
    public static String formatDateString(String value){
        if (value.length() < 1){
            return value;
        }

        value = value.replace(".", "");
        value = value.substring(0, 1).toUpperCase() + value.substring(1);

        return value;
    }

    /**
     * Returns a given month as localized short text, so February would be Feb. This methods also
     * formats correctly the value when using specific locales. See {@link #formatDateString(String)} for a
     * more detailed explanation of the formatting.
     * @param dateTime
     * @return Month of datetime as short text
     */
    public static String getMonthShortText(DateTime dateTime){
        return formatDateString(dateTime.monthOfYear().getAsShortText(Locale.getDefault()));
    }

    /**
     * Returns a given month as localized short text, such as "February". This methods also
     * formats correctly the value when using specific locales. See {@link #formatDateString(String)} for a
     * more detailed explanation of the formatting.
     * @param dateTime
     * @return Month of datetime as text
     */
    public static String getMonthText(DateTime dateTime){
        return formatDateString(dateTime.monthOfYear().getAsText(Locale.getDefault()));
    }

    /**
     * Extracts the day of month from a given datetime.
     * @param dateTime
     * @return day of month such as 1 or 31
     */
    public static String getDayOfMonthString(DateTime dateTime){
        return dateTime.dayOfMonth().getAsString();
    }

    public static String datetimeToString(DateTime dateTime){
        DateTimeFormatter formatter;

        if (Locale.getDefault().equals(Locale.US)) {
            formatter = DateTimeFormat.forPattern("E, M/d/y");
        } else {
            formatter = DateTimeFormat.forPattern("E, d/M/y");
        }

        return formatDateString(formatter.print(dateTime));
    }
}

