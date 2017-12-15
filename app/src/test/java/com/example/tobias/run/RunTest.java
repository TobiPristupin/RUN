package com.example.tobias.run;

import com.example.tobias.run.data.Run;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Tobi on 10/20/2017.
 */

public class RunTest {

    Run similarRun = Run.withKilometers(1, 2, 3, 4);
    Run similarRun2 = Run.withKilometers(1, 2, 3, 4);
    Run differentRun = Run.withMiles(45, 2, 567, 7);

    @Test public void testEquals(){
        Assert.assertEquals(true, similarRun.equals(similarRun2));
        Assert.assertEquals(false, similarRun.equals(differentRun));
        
        //Reflexive
        Assert.assertEquals(true, similarRun.equals(similarRun));
        
        //Symmetric
        Assert.assertEquals(true, similarRun.equals(similarRun2) && similarRun2.equals(similarRun));
       
        Assert.assertEquals(false, similarRun.equals(null));
    }

    @Test public void testHashCode(){
        System.out.println(similarRun);

        Assert.assertEquals(similarRun.hashCode(), similarRun.hashCode());
        Assert.assertEquals(similarRun.hashCode(), similarRun2.hashCode());
        Assert.assertNotEquals(similarRun.hashCode(), differentRun.hashCode());
    }

    @Test public void testIsRunFromDistanceKm(){

    }

    @Test public void testIsRunFromDistanceMiles(){

    }
}
