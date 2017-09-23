package com.example;


import static java.lang.System.out;
import static java.util.Arrays.asList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class MyClass {
    public static void main( String[] args ) {
        List<String> names = asList( "Ted", "Fred", "Jed", "Ned" );
        out.println( names );
        ArrayList<String> shortNames = new ArrayList<String>();
        shortNames = filterRun(names, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return ((String) object).length() < 4;
            }
        });

        out.println( shortNames.size() );
        out.println(shortNames);
    }

    public static ArrayList<String> filterRun(List<String> trackedRuns, Predicate predicate){
        ArrayList<String> filteredList = new ArrayList<>();
        for (String tr : trackedRuns){
            if (predicate.evaluate(tr)){
                filteredList.add(tr);
            }
        }

        return filteredList;
    }
}