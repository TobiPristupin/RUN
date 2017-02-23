package com.example;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class MyClass {

    public static void main(String[] args){
        System.out.println(dateToString(dateToUnix("feb, 2/2/2017")));
    }

    public static long dateToUnix(String date){
        //Remove irrelevant day information
        date = date.split(",")[1].trim();
        String[] dateUnits = new String[3];
        //Split into year month day
        dateUnits = date.split("/");
        DateTime dateTime = new DateTime(Integer.valueOf(dateUnits[2]), Integer.valueOf(dateUnits[1]),
                Integer.valueOf(dateUnits[0]), 0, 0);

        return dateTime.getMillis();
    }


    public static String dateToString(long date){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("E, d/M/y");
        return formatter.print(date);
        /**
         * IMPORTANT!
         * Using dateTimeFormatter resulted in obscure bugs with dates being always
         * a couple of days off. Resulted in building the string manually.
         */


    }


}
