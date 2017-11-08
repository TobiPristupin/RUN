package com.example.tobias.run.utils;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Utils class to handel conversions and formatting
 */
public class ConversionManager {

    /**
     *
     * @param distance in "5.5(f)"
     * @param unit
     * @return distance in the format of "1 km/mi"
     */
    public static String distanceToString(double distance, String unit){
        DecimalFormat df = new DecimalFormat("0.00");
        String distanceText = df.format(distance) + " " + unit;
        return distanceText;
    }

    /**
     *
     * @param time in unix timestamp format
     * @return time in format hh:mm:ss
     */
    public static String timeToString(long time){
        //Create period to transform time in millis to formatted time.
        Period period = new Period(time);

        String timeText = new StringBuilder()
                .append(String.format(Locale.getDefault(), "%02d", period.getHours()))
                .append(":")
                .append(String.format(Locale.getDefault(), "%02d", period.getMinutes()))
                .append(":")
                .append(String.format(Locale.getDefault(), "%02d", period.getSeconds())).toString();

        return timeText;
    }

    /**
     *
     * @param date in unix timestamp
     * @return date in format "E, d/M/y"
     */
    public static String dateToString(long date){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("E, d/M/y");
        //Unix time is in milli, DateTime is in sec, so multiply by 1000 to convert
        String dateText = formatter.print(new DateTime(date * 1000L));
        return dateText;
    }

    /**
     *
     * @param distance in format "5.5 km/mi"
     * @return distance in format "5.5(f)"
     */
    public static float distanceToFloat(String distance){
        //Double.ValueOf wont parse comma, but will parse dot.
        distance = distance.replace(",", ".");
        //Remove km or mi
        distance = distance.replace("km", "").replace("mi", "").trim();
        return Float.parseFloat(distance);
    }

    /**
     *
     * @param time in hh:mm:ss
     * @return time in unix timestamp
     */
    public static long timeToUnix(String time){
        String[] timeUnits = new String[3];
        //Split time into hours seconds minutes
        timeUnits = time.split(":");
        Period timePeriod = new Period()
                .withHours(Integer.valueOf(timeUnits[0]))
                .withMinutes(Integer.valueOf(timeUnits[1]))
                .withSeconds(Integer.valueOf(timeUnits[2]));
        //Return time in millis
        return timePeriod.toStandardDuration().getMillis();
    }

    /**
     *
     * @param date in format "E, e/MMM/YYYY"
     * @return date in unix timestamp
     */
    public static long dateToUnix(String date){
        //Remove irrelevant day information
        date = date.split(",")[1].trim();
        String[] dateUnits = new String[3];
        //Split into year month day
        dateUnits = date.split("/");
        DateTime dateTime = new DateTime()
                .withYear(Integer.valueOf(dateUnits[2]))
                .withMonthOfYear(Integer.valueOf(dateUnits[1]))
                .withDayOfMonth(Integer.valueOf(dateUnits[0]));

        return dateTime.getMillis() / 1000;
    }

    /**
     *
     * @param distance distance in any format.
     * @param unixTime time of run in unix timestamp format
     * @return pace per unit of distance in unix timestamp format
     */
    public static long calculatePace(float distance, long unixTime){
        //Period is inputted time in millis and converts it to hh:mm:ss
        Period period = new Period(unixTime);
        float timeInSeconds = period.getHours() * 3600f + period.getMinutes() * 60f + period.getSeconds();
        float pace = timeInSeconds / distance;
        //Multiply pace by 1000 to convert it to millis from seconds.
        return (long) pace * 1000;
    }

    /**
     *
     * @param paceTimeUnix pace in unix timestamp form
     * @param unit
     * @return pace in format mm:ss/unit or hh:mm:ss/unit if the pace contains hours.
     */
    public static String paceToString(long paceTimeUnix, String unit){
        Period period = new Period(paceTimeUnix);
        String paceText = new StringBuilder()
                .append(String.format(Locale.getDefault(), "%02d", period.getMinutes()))
                .append(":")
                .append(String.format(Locale.getDefault(), "%02d", period.getSeconds()))
                .append("/")
                .append(unit).toString();

        if (period.getHours() > 0){
            String hoursText = new StringBuilder()
                    .append(String.format(Locale.getDefault(), "%02d", period.getHours()))
                    .append(":")
                    .append(paceText).toString();
            return hoursText;
        }

        return paceText;
    }

    public static int roundNumber(float x, int decimalPlaces){
        BigDecimal bd = new BigDecimal(Float.toString(x));
        bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
        return bd.intValue();
    }

    public static int roundNumber(double x, int decimalPlaces){
        BigDecimal bd = new BigDecimal(Double.toString(x));
        bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
        return bd.intValue();
    }

    public static float kilometresToMiles(float km){
        return km * 0.621371f;
    }

    public static float milesToKilometers(float mi){
        return mi * 1.60934f;
    }



}
