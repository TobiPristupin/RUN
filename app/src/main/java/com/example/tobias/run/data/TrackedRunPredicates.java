package com.example.tobias.run.data;

import com.example.tobias.run.utils.ConversionManager;

import org.apache.commons.collections.Predicate;

/**
 * Created by Tobi on 9/21/2017.
 */

public class TrackedRunPredicates {

    public static Predicate isRunFromWeek(){
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                TrackedRun tr = (TrackedRun) object;
                return tr.getDate() >= ConversionManager.getStartOfWeek()
                        && tr.getDate() <= ConversionManager.getEndOfWeek();
            }
        };
    }

    public static Predicate isRunFromMonth(){
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                TrackedRun tr = (TrackedRun) object;
                return tr.getDate() >= ConversionManager.getStartOfCurrentMonth()
                        && tr.getDate() <= ConversionManager.getEndOfMonth();
            }
        };
    }

    public static Predicate isRunFromYear(){
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                TrackedRun tr = (TrackedRun) object;
                return tr.getDate() >= ConversionManager.getStartOfYear()
                        && tr.getDate() <= ConversionManager.getEndOfYear();
            }
        };
    }

    public static Predicate isRunFromPast2Months(){
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                TrackedRun tr = (TrackedRun) object;
                return tr.getDate() >= ConversionManager.getStartOfMonthMinusMonths(2)
                        && tr.getDate() <= ConversionManager.getEndOfMonth();
            }
        };
    }

    public static Predicate isRunFromMonthMinusMonth(final int minus){
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                TrackedRun tr = (TrackedRun) object;
                return tr.getDate() >= ConversionManager.getStartOfMonthMinusMonths(minus)
                        && tr.getDate() <= ConversionManager.getEndOfMonthMinusMonths(minus);
            }
        };
    }
}
