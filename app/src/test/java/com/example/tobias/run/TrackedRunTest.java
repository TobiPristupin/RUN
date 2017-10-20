package com.example.tobias.run;

import com.example.tobias.run.data.TrackedRun;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Tobi on 10/20/2017.
 */

public class TrackedRunTest {

    @Test public void testEquality(){
        TrackedRun trackedRun = new TrackedRun(10, 23, 45, 104, 2432, "a");
        TrackedRun trackedRun2 = new TrackedRun(10, 23, 45, 104, 2432, "a");
        Assert.assertEquals(true, trackedRun.equals(trackedRun2));

        TrackedRun trackedRun3 = new TrackedRun(10, 23, 45, 104, 2432, "b");
        Assert.assertEquals(false, trackedRun.equals(trackedRun3));
    }
}
