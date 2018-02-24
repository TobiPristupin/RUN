package com.tobipristupin.simplerun.data;

import com.tobipristupin.simplerun.data.model.Run;

import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;

/**
 * Apache commons predicates to test conditions for Runs
 */

public class RunPredicates {

    public static Predicate isRunBetween(final long start, final long end) {
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Run run = (Run) object;
                return run.getDate() >= start && run.getDate() <= end;
            }
        };
    }

    public static Predicate isRunFromDistance(final double distanceKm, final double distanceMi) {
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Run run = (Run) object;
                return run.getDistance().equalsDistance(distanceKm, distanceMi) && run.getTime() != 0;
            }
        };
    }

    /**
     * Checks if run's date is in certain weekday
     * @param weekDay day of week in range [1, 7]. 1 = Monday, 7 = Sunday
     */
    public static Predicate isRunFromWeekDay(final int weekDay){
        if(weekDay < 1 || weekDay > 7){
            throw new IllegalArgumentException("week day must be in range [1, 7]");
        }

        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Run run = (Run) object;
                return new DateTime(run.getDate() * 1000L).getDayOfWeek() == weekDay;
            }
        };
    }
}