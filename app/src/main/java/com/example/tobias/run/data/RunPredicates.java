package com.example.tobias.run.data;

import org.apache.commons.collections.Predicate;

/**
 * Created by Tobi on 12/21/2017.
 */

public class RunPredicates {

    public static Predicate isRunBetween(final long start, final long end) {
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Run tr = (Run) object;
                return tr.getDate() >= start && tr.getDate() <= end;
            }
        };
    }

    public static Predicate isRunFromDistance(final double distance, final Distance.Unit unit) {
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Run tr = (Run) object;
                return tr.getDistance().equalsDistance(distance, unit) && tr.getTime() != 0;
            }
        };
    }
}
