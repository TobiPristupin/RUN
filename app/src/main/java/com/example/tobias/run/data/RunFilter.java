package com.example.tobias.run.data;


public enum RunFilter {
    WEEK, MONTH, YEAR, ALL;


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
}
