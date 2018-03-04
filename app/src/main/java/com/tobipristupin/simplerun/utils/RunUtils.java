package com.tobipristupin.simplerun.utils;

import android.support.annotation.Nullable;

import com.tobipristupin.simplerun.data.model.DistanceUnit;
import com.tobipristupin.simplerun.data.model.Run;
import com.tobipristupin.simplerun.interfaces.Predicate;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class RunUtils {

    private RunUtils(){
        //Private constructor to ensure non-instantiability
    }

    /**
     * Filters run given a predicate and a list of runs. Does not modify list of runs.
     * @param runs
     * @param predicate
     * @return filtered ArrayList
     */
    public static List<Run> filterList(List<Run> runs, Predicate<Run> predicate){
        List<Run> filteredList = new ArrayList<>();
        for (Run tr : runs){
            if (predicate.evaluate(tr)){
                filteredList.add(tr);
            }
        }

        return filteredList;
    }


    /**
     * Returns fastest run (run with less time). If two runs have same time, will return first one in list.
     * Ignores runs that have time = 0.
     * Precondition: Assumes all runs are of the same distance, will not check if runs are the same distances.
     * @param runs list of runs to evaluate
     * @return fastest run
     */
    public static @Nullable Run getFastestRun(List<Run> runs){
        if (runs.isEmpty()){
            return null;
        }

        Run fastest = runs.get(0);

        for (Run r : runs){
            if (r.getTime() != 0 && r.getTime() < fastest.getTime()){
                fastest = r;
            }

        }

        return fastest;
    }

    public static @Nullable Run getLongestRun(List<Run> runs){
        if (runs.isEmpty()){
            return null;
        }

        Run farthest = runs.get(0);

        for (Run r : runs){
            if (r.getDistance(DistanceUnit.KM) > farthest.getDistance(DistanceUnit.KM)){
                farthest = r;
            }
        }

        return farthest;
    }

    public static @Nullable Run getFastestPace(List<Run> runs){
        if (runs.isEmpty()){
            return null;
        }

        Run fastest = runs.get(0);

        for (Run r : runs){
            if (r.getPace(DistanceUnit.KM) < fastest.getPace(DistanceUnit.KM)){
                fastest = r;
            }
        }

        return fastest;
    }

    public static @Nullable Run getLongestDuration(List<Run> runs){
        if (runs.isEmpty()){
            return null;
        }

        Run longest = runs.get(0);

        for (Run r : runs){
            if (r.getTime() > longest.getTime()){
                longest = r;
            }
        }

        return longest;
    }

    public static double getAverageTime(List<Run> runs){
        long sum = 0;

        for (Run r : runs){
            sum += r.getTime();
        }

        return sum / runs.size();
    }

    public static double addArray(double[] mileage) {
        double sum = 0;
        for (double f : mileage) {
            sum += f;
        }

        return sum;
    }

    public static double addMileage(List<Run> runs, DistanceUnit unit) {
        double sum = 0;
        for (Run r : runs) {
            sum += r.getDistance(unit);
        }

        return sum;
    }

    /**
     *
     * @param distance in "5.5"
     * @param unit
     * @return distance in the format of "1 km/mi"
     */
    public static String distanceToString(double distance, DistanceUnit unit){
        distance = roundNearestTenth(distance);
        DecimalFormat df = new DecimalFormat("0.00");
        String distanceText = df.format(distance) + " " + unit.toString();
        return distanceText;
    }

    /**
     *
     * @param time in unix timestamp format
     * @param includeZeroHours should include hour part if hours are 0
     * @return time in format hh:mm:ss or mm:ss if includeZeroHours is false;
     */
    public static String timeToString(long time, boolean includeZeroHours){
        //Create period to transform time in millis to formatted time.
        Period period = new Period(time);

        String timeText = new StringBuilder()
                .append(String.format(Locale.getDefault(), "%02d", period.getMinutes()))
                .append(":")
                .append(String.format(Locale.getDefault(), "%02d", period.getSeconds())).toString();

        if (period.getHours() > 0 || includeZeroHours){
            timeText = String.format(Locale.getDefault(), "%02d", period.getHours()) + ":" + timeText;
        }

        return timeText;
    }

    /**
     *
     * @param date in unix timestamp
     * @return date in format "E, M/d/y" if Locale is US or format "E, d/M/y" if it isn't
     */
    public static String dateToString(long date){
        Locale locale = Locale.getDefault();
        DateTimeFormatter formatter;

        if (locale.equals(Locale.US)){
            formatter = DateTimeFormat.forPattern("E, M/d/y");
        } else {
            formatter = DateTimeFormat.forPattern("E, d/M/y");
        }

        //Unix time is in milli, DateTime is in sec, so multiply by 1000 to convert
        String dateText = formatter.print(new DateTime(date * 1000L));
        dateText = DateUtils.formatDateString(dateText);
        return dateText;
    }

    /**
     *
     * @param distance in format "5.5 km/mi"
     * @return distance in format "5.5(f)"
     */
    public static double distanceToDouble(String distance){
        //Double.ValueOf wont parse comma, but will parse dot.
        distance = distance.replace(",", ".");
        //Remove km or mi
        distance = distance.replace("km", "").replace("mi", "").trim();
        return Double.parseDouble(distance);
    }

    /**
     *
     * @param time in hh:mm:ss
     * @return time in unix timestamp
     */
    public static long timeToUnix(String time){
        String[] timeUnits;
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
        String[] dateUnits;
        //Split into year month day
        dateUnits = date.split("/");

        DateTime dateTime;

        if (Locale.getDefault().equals(Locale.US)){
            dateTime = new DateTime()
                    .withYear(Integer.valueOf(dateUnits[2]))
                    .withMonthOfYear(Integer.valueOf(dateUnits[0]))
                    .withDayOfMonth(Integer.valueOf(dateUnits[1]));
        } else {
            dateTime = new DateTime()
                    .withYear(Integer.valueOf(dateUnits[2]))
                    .withMonthOfYear(Integer.valueOf(dateUnits[1]))
                    .withDayOfMonth(Integer.valueOf(dateUnits[0]));
        }


        return dateTime.getMillis() / 1000;
    }

    /**
     *
     * @param paceTimeUnix pace in unix timestamp form
     * @param unit
     * @return pace in format mm:ss/unit or hh:mm:ss/unit if the pace contains hours.
     */
    public static String paceToString(long paceTimeUnix, DistanceUnit unit){
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

    public static int ratingToInt(String rating){
        return Integer.valueOf(rating);
    }

    public static double roundNearestTenth(double a){
        return (double) Math.round(a * 10) / 10;
    }

}
