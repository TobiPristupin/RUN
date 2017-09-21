package com.example;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.Iterator;

public class MyClass {

    public static void main(String[] args) {
        Period period = new Period(372000);
        System.out.println(period.getHours());
        System.out.println(period.getMinutes());
        System.out.println(period.getSeconds());
    }

}
