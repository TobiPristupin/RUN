package com.example.tobias.run;

import com.example.tobias.run.data.TrackedRun;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Tobi on 10/20/2017.
 */

public class TrackedRunTest {

    @Test public void testEquals(){
        TrackedRun trackedRun = new TrackedRun(10, 23, 45, 104, 2432, "a");
        TrackedRun trackedRun2 = new TrackedRun(10, 23, 45, 104, 2432, "a");
        TrackedRun trackedRun3 = new TrackedRun(1, 2, 4, 1, 2, "b");
        
        Assert.assertEquals(true, trackedRun.equals(trackedRun2));
        Assert.assertEquals(false, trackedRun.equals(trackedRun3));
        
        //Reflexive
        Assert.assertEquals(true, trackedRun.equals(trackedRun));
        
        //Symmetric
        Assert.assertEquals(true, trackedRun.equals(trackedRun2) && trackedRun2.equals(trackedRun));
       
        Assert.assertEquals(false, trackedRun.equals(null));
    }
}
