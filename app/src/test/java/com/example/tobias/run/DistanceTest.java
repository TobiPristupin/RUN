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
        distance.setDistanceKm(10);
        System.out.println(distance.getDistanceMi());
        Assert.assertEquals(true, distance.getDistanceMi() == 6.2f);
    }

    @Test public void testDistanceEquivalences() {
        Distance distanceMile = new Distance(1.6f, Distance.Unit.KM);
        Assert.assertEquals(true, distanceMile.getDistanceMi() == 1);

        Distance distanceHM = new Distance(13.1f, Distance.Unit.MILE);
        Assert.assertEquals(true, distanceHM.getDistanceKm() == 21);
    }

    @Test public void testCompareTo(){
        Assert.assertEquals(true, distanceKm.compareTo(distance2Km) < 0);

        Assert.assertEquals(true, distanceMile.compareTo(distanceKm) > 0);

        Assert.assertEquals(true, distanceKm.compareTo(distanceKm) == 0);
    }

    @Test public void testEqualsDistance(){
        Assert.assertEquals(true, distanceKm.equalsDistance(1, Distance.Unit.KM));

        Assert.assertEquals(false, distanceKm.equalsDistance(1, Distance.Unit.MILE));

        Assert.assertEquals(true, distanceMile.equalsDistance(1.6f, Distance.Unit.KM));
    }

    @Test public void testHashCode(){
        Assert.assertEquals(true, distanceKm.hashCode() == distanceKm.hashCode());

        Assert.assertEquals(true, distanceKm.hashCode() != distance2Km.hashCode());
    }

}
