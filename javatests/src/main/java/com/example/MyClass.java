package com.example;

import org.joda.time.DateTime;

public class MyClass {

    public static void main(String[] args){
        long firstDayOfWeekTimestamp = new DateTime().withDayOfWeek(1).getMillis() / 1000;
        long lastDayOfWeekTimestamp = new DateTime().withDayOfWeek(7).getMillis() / 1000;
        System.out.println(firstDayOfWeekTimestamp);
        System.out.println(lastDayOfWeekTimestamp);
    }


}
