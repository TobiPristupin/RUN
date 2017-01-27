package com.example;

import org.joda.time.DateTime;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MyClass {

    public static void main(String[] args){
        long firstDayOfYearTimestamp = new DateTime().dayOfYear().withMinimumValue().getMillis() / 1000;
        long lastDayOfYearTimestamp = new DateTime().dayOfYear().withMaximumValue().getMillis() / 1000;
        System.out.println(lastDayOfYearTimestamp);
    }


}
