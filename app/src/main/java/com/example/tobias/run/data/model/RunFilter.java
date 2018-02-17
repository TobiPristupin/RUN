package com.example.tobias.run.data.model;


/**
 * Enum to represent different date filters that can be applied to sort Runs
 */

public enum RunFilter {
    WEEK("Week"), MONTH("Month"), YEAR("Year"), ALL("All");

    private String value;

    RunFilter(String value) {
        this.value = value;
    }

    public static RunFilter get(String value) {
        switch (value.toLowerCase()) {
            case "week":
                return WEEK;
            case "month":
                return MONTH;
            case "year":
                return YEAR;
            case "all":
                return ALL;
            default:
                throw new RuntimeException();
        }
    }


    @Override
    public String toString() {
        return value;
    }
}
