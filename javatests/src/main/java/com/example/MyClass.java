package com.example;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Iterator;

public class MyClass {

    public static void main(String[] args){
        ArrayList<String> list = new ArrayList<>();
        list.add("hey");
        list.add("hey1");
        list.add("hey2");
        System.out.println(containsRun(list, "hey"));
        System.out.println(containsRun(list, "hy"));
        System.out.println(containsRun(list, "hey2"));
    }

    private static boolean containsRun(ArrayList<String> arrayList, String string){
        Iterator<String> iterator = arrayList.iterator();
        while (iterator.hasNext()){
            String iteratorString = iterator.next();
            if (iteratorString.equals(string)){
                return true;
            }
        }
        return false;
    }





}
