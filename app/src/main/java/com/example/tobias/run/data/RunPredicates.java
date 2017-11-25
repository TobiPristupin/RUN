package com.example.tobias.run.data;

import com.example.tobias.run.utils.DateUtils;

import org.apache.commons.collections.Predicate;

/**
 * Created by Tobi on 9/21/2017.
 */

public class RunPredicates {

    public static Predicate isRunBetween(final long start, final long end){
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Run tr = (Run) object;
                return tr.getDate() >= start && tr.getDate() <= end;
            };
        };
    }

    public static Predicate isRunFromWeek(){
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Run tr = (Run) object;
                return tr.getDate() >= DateUtils.getStartOfWeek()
                        && tr.getDate() <= DateUtils.getEndOfWeek();
            }
        };
    }

    public static Predicate isRunFromMonth(){
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Run tr = (Run) object;
                return tr.getDate() >= DateUtils.getStartOfMonth()
                        && tr.getDate() <= DateUtils.getEndOfMonth();
            }
        };
    }

    public static Predicate isRunFromYear(){
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Run tr = (Run) object;
                return tr.getDate() >= DateUtils.getStartOfYear()
                        && tr.getDate() <= DateUtils.getEndOfYear();
            }
        };
    }

    public static Predicate isRunFromPast3Months(){
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Run tr = (Run) object;
                return tr.getDate() >= DateUtils.getStartOfMonth(-2)
                        && tr.getDate() <= DateUtils.getEndOfMonth();
            }
        };
    }

    public static Predicate isRunFromMonthMinusMonth(final int minus){
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Run tr = (Run) object;
                return tr.getDate() >= DateUtils.getStartOfMonth(-minus)
                        && tr.getDate() <= DateUtils.getEndOfMonth(-minus);
            }
        };
    }

    public static Predicate isRunFromPast6Months(){
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Run tr = (Run) object;
                return tr.getDate() >= DateUtils.getStartOfMonth(-5)
                        && tr.getDate() <= DateUtils.getEndOfMonth();
            }
        };
    }
}
