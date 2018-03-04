package com.tobipristupin.simplerun.data;

import com.tobipristupin.simplerun.data.model.Run;
import com.tobipristupin.simplerun.interfaces.Predicate;

import org.joda.time.DateTime;

/**
 *Predicates to test conditions for Runs
 */

public class RunPredicates {

    public static Predicate<Run> isRunBetween(final long start, final long end) {
        return new Predicate<Run>() {
            @Override
            public boolean evaluate(Run run) {
                return run.getDate() >= start && run.getDate() <= end;
            }
        };
    }

    public static Predicate<Run> isRunFromDistance(final double distanceKm, final double distanceMi) {
        return new Predicate<Run>() {
            @Override
            public boolean evaluate(Run run) {
                return run.getDistance().equalsDistance(distanceKm, distanceMi) && run.getTime() != 0;
            }
        };
    }

    /**
     * Checks if run's date is in certain weekday
     * @param weekDay day of week in range [1, 7]. 1 = Monday, 7 = Sunday
     */
    public static Predicate<Run> isRunFromWeekDay(final int weekDay){
        if(weekDay < 1 || weekDay > 7){
            throw new IllegalArgumentException("week day must be in range [1, 7]");
        }

        return new Predicate<Run>() {
            @Override
            public boolean evaluate(Run run) {
                return new DateTime(run.getDate() * 1000L).getDayOfWeek() == weekDay;
            }
        };
    }

}