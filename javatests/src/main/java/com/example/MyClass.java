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
        DateTime dt = new DateTime(2017, 1, 22, 0, 0, 0);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("E, d/M/y");
        System.out.println(dtf.print(dt));

    }
}
