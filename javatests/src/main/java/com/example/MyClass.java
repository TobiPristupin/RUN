package com.example;

import org.joda.time.DateTime;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyClass {

    public static void main(String[] args){
        long date = formatDate("Thu, 2/3/2017");
        System.out.println(date);

    }

    private static long formatDate(String date){
        //Remove irrelevant day information
        date = date.split(",")[1].trim();
        String[] dateUnits = new String[3];
        //Split into year month day
        dateUnits = date.split("/");
        DateTime dateTime = new DateTime()
                .withYear(Integer.valueOf(dateUnits[2]))
                .withMonthOfYear(Integer.valueOf(dateUnits[1]))
                .withDayOfMonth(Integer.valueOf(dateUnits[0]));
        //Return time in seconds since epoch
        return dateTime.getMillis() / 1000;
    }

}
