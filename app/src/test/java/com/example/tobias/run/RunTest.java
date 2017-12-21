package com.example.tobias.run;

import com.example.tobias.run.data.Distance;
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
        Assert.assertEquals(similarRun.hashCode(), similarRun.hashCode());
        Assert.assertEquals(similarRun.hashCode(), similarRun2.hashCode());
        Assert.assertNotEquals(similarRun.hashCode(), differentRun.hashCode());
    }

    @Test public void testIsRunFromDistanceKm(){
        Run run = Run.withKilometers(0, 10, 0, 0);
        Assert.assertEquals(true, Run.Predicates.isRunFromDistanceKm(10).evaluate(run));
        Assert.assertEquals(false, Run.Predicates.isRunFromDistanceKm(50f).evaluate(run));
        Assert.assertEquals(false, Run.Predicates.isRunFromDistanceKm(10.1f).evaluate(run));

        Run runMile = Run.withMiles(0, 1, 0, 0);
        Assert.assertEquals(true, Run.Predicates.isRunFromDistanceKm(1.6f).evaluate(runMile));
        Assert.assertEquals(false, Run.Predicates.isRunFromDistanceKm(1.7f).evaluate(runMile));
        Assert.assertEquals(false, Run.Predicates.isRunFromDistanceKm(1.5f).evaluate(runMile));

        Run run5k = Run.withMiles(3.1f, 0, 0, 0);
        Assert.assertEquals(true, Run.Predicates.isRunFromDistanceKm(5f).evaluate(run5k));

        Run run10k = Run.withMiles(6.2f, 0, 0, 0);
        Assert.assertEquals(true, Run.Predicates.isRunFromDistanceKm(10f).evaluate(run10k));

        Run run21k = Run.withMiles(13.1f, 0, 0, 0);
        Assert.assertEquals(true, Run.Predicates.isRunFromDistanceKm(21f).evaluate(run21k));
    }

    @Test public void testIsRunFromDistanceMiles(){

    }

    @Test public void testPaceUpdate(){
        Run run = Run.withKilometers(1, 1000, 0, 0);
        long kmPace1 = run.getPace(Distance.Unit.KM);

        run.setTime(2000);
        long kmPace2 = run.getPace(Distance.Unit.KM);

        Assert.assertEquals(false, kmPace1 == kmPace2);

        run.setDistanceKilometres(5);
        long kmPace3 = run.getPace(Distance.Unit.KM);

        Assert.assertEquals(false, kmPace2 == kmPace3);
    }
}
