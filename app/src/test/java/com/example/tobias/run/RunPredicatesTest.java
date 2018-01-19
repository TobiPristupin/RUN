package com.example.tobias.run;

import com.example.tobias.run.data.Run;
import com.example.tobias.run.data.RunPredicates;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Tobi on 1/17/2018.
 */

public class RunPredicatesTest {

    @Test public void testIsRunFromWeekday(){
        Run run = Run.withKilometers(1, 1, 1516228192, 1);
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

        Run run1 = Run.withKilometers(1, 1, 1516048993, 1);
        Assert.assertEquals(true, RunPredicates.isRunFromWeekDay(1).evaluate(run1));
    }
}
