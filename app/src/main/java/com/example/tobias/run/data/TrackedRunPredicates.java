package com.example.tobias.run.data;

import com.example.tobias.run.utils.ConversionManager;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.ObjectArrayIterator;

/**
 * Created by Tobi on 9/21/2017.
 */

public class TrackedRunPredicates {

    public static Predicate isRunFromWeek(){
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                TrackedRun tr = (TrackedRun) object;
                return tr.getDate() >= ConversionManager.getStartOfWeek() && tr.getDate() <= ConversionManager.getEndOfWeek();
            }
        };
    }

    public static Predicate isRunFromMonth(){
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                TrackedRun tr = (TrackedRun) object;
                return tr.getDate() >= ConversionManager.getStartOfMonth() && tr.getDate() <= ConversionManager.getEndOfMonth();
            }
        };
    }

    public static Predicate isRunFromYear(){
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                TrackedRun tr = (TrackedRun) object;
                return tr.getDate() >= ConversionManager.getStartOfYear() && tr.getDate() <= ConversionManager.getEndOfYear();
            }
        };
    }
}
