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
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println(df.format(2.19));

    }
}
