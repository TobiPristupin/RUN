package com.tobipristupin.simplerun.model;

import com.tobipristupin.simplerun.data.RunPredicates;
import com.tobipristupin.simplerun.data.model.Run;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Tobi on 1/17/2018.
 */

public class RunPredicatesTest {

    @Test public void testIsRunFromWeekday(){
        Run run = Run.fromKilometers(1, 1, 1516228192, 1);
        Assert.assertEquals(true, RunPredicates.isRunFromWeekDay(3).evaluate(run));
        Assert.assertEquals(false, RunPredicates.isRunFromWeekDay(4).evaluate(run));

        //Junit v4 has no built-in support for exceptions
        boolean exceptionThrown = false;
        try {
            RunPredicates.isRunFromWeekDay(8).evaluate(run);
        } catch (IllegalArgumentException e){
            exceptionThrown = true;
        }
        Assert.assertEquals(true, exceptionThrown);

        Run run1 = Run.fromKilometers(1, 1, 1516048993, 1);
        Assert.assertEquals(true, RunPredicates.isRunFromWeekDay(1).evaluate(run1));
    }
}
