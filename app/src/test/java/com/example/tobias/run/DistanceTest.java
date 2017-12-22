package com.example.tobias.run;

import com.example.tobias.run.data.Distance;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Tobi on 12/19/2017.
 */

public class DistanceTest {


    private final Distance distanceKm = new Distance(1, Distance.Unit.KM);
    private final Distance distance2Km = new Distance(2, Distance.Unit.KM);
    private final Distance distanceMile = new Distance(1, Distance.Unit.MILE);

    @Test public void testSetters(){
        Distance distance = new Distance(1, Distance.Unit.KM);
        distance.setDistance(Distance.Unit.KM, 10);
        Assert.assertEquals(true, distance.getDistanceMi() == 6.2);
    }

    @Test public void testDistanceEquivalences() {
        Distance distanceMile = new Distance(1.6, Distance.Unit.KM);
        Assert.assertEquals(true, distanceMile.getDistanceMi() == 1);

        Distance distanceHM = new Distance(13.1, Distance.Unit.MILE);
        Assert.assertEquals(true, distanceHM.getDistanceKm() == 21);
    }


    @Test public void testEqualsDistance(){
        Assert.assertEquals(true, distanceKm.equalsDistance(1, Distance.Unit.KM));

        Assert.assertEquals(false, distanceKm.equalsDistance(1, Distance.Unit.MILE));

        Assert.assertEquals(true, distanceMile.equalsDistance(1.6, Distance.Unit.KM));

        Distance distance = new Distance(.39996, Distance.Unit.KM);
        Assert.assertEquals(true, distance.equalsDistance(.4, Distance.Unit.KM));
        Assert.assertEquals(true, distance.equalsDistance(.25, Distance.Unit.MILE));

        Distance distance1 = new Distance(42, Distance.Unit.KM);
        Assert.assertEquals(true, distance1.equalsDistance(26.2, Distance.Unit.MILE));

        distance1.setDistance(Distance.Unit.KM, 42.5);
        Assert.assertEquals(false, distance1.equalsDistance(26.2, Distance.Unit.MILE));
    }

    @Test public void testHashCode(){
        Assert.assertEquals(true, distanceKm.hashCode() == distanceKm.hashCode());

        Assert.assertEquals(true, distanceKm.hashCode() != distance2Km.hashCode());
    }

}
