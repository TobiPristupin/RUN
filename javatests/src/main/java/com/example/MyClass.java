package com.example;


import org.joda.time.LocalDate;

public class MyClass {

    public static void main(String[] args){
        LocalDate today = new LocalDate();
        long firstDay = today.dayOfWeek().withMinimumValue().toDateTimeAtStartOfDay().getMillis() / 1000;
        long lastDay = today.dayOfWeek().withMaximumValue().toDateTimeAtMidnight().getMillis() / 1000;
        System.out.println(firstDay);
        System.out.println(lastDay);
    }





}
