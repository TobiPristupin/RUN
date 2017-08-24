package com.example;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class MyClass {

    public static void main(String[] args){
        LocalDate today = new LocalDate();
        long firstDay = new DateTime().dayOfMonth().withMinimumValue().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).getMillis() / 1000;
        long lastDay = new DateTime().dayOfWeek().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).getMillis() / 1000;
        System.out.println(firstDay);
        System.out.println(lastDay);
    }





}
