package com.tobipristupin.simplerun.model;



import com.tobipristupin.simplerun.data.model.Distance;
import com.tobipristupin.simplerun.data.model.DistanceUnit;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Tobi on 12/19/2017.
 */

public class DistanceTest {


    private final Distance distanceKm = new Distance(1, DistanceUnit.KM);
    private final Distance distance2Km = new Distance(2, DistanceUnit.KM);


    @Test public void testEqualsDistance(){
        Assert.assertEquals(true, new Distance(42, DistanceUnit.KM).equalsDistance(42, 26.2));

        Assert.assertEquals(true, new Distance(26.2, DistanceUnit.MILE).equalsDistance(42, 26.2));

        Assert.assertEquals(false, new Distance(17, DistanceUnit.KM).equalsDistance(20, 17));
    }

    @Test public void testHashCode(){
        Assert.assertEquals(true, distanceKm.hashCode() == distanceKm.hashCode());

        Assert.assertEquals(true, distanceKm.hashCode() != distance2Km.hashCode());
    }

}
