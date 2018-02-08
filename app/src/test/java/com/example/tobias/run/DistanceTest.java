package com.example.tobias.run;

import com.example.tobias.run.data.model.Distance;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Tobi on 12/19/2017.
 */

public class DistanceTest {


    private final Distance distanceKm = new Distance(1, Distance.Unit.KM);
    private final Distance distance2Km = new Distance(2, Distance.Unit.KM);


    @Test public void testEqualsDistance(){
        Assert.assertEquals(true, new Distance(42, Distance.Unit.KM).equalsDistance(42, 26.2));

        Assert.assertEquals(true, new Distance(26.2, Distance.Unit.MILE).equalsDistance(42, 26.2));

        Assert.assertEquals(false, new Distance(17, Distance.Unit.KM).equalsDistance(20, 17));
    }

    @Test public void testHashCode(){
        Assert.assertEquals(true, distanceKm.hashCode() == distanceKm.hashCode());

        Assert.assertEquals(true, distanceKm.hashCode() != distance2Km.hashCode());
    }

}
