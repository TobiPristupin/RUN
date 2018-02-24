package com.tobipristupin.simplerun.data.model;


public enum DistanceUnit {
    KM("km"), MILE("mi");

    private String value;

    DistanceUnit(String value){
        this.value = value;
    }


    public static DistanceUnit fromString(String value) {
        for (DistanceUnit unit : DistanceUnit.values()){
            if (unit.toString().equalsIgnoreCase(value)){
                return unit;
            }
        }

        throw new IllegalArgumentException("Value doesn't exist");
    }

    @Override
    public String toString() {
        return value;
    }
}
