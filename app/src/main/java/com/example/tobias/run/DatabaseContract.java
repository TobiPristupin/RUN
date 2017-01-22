package com.example.tobias.run;


import android.provider.BaseColumns;

public class DatabaseContract {

    public static abstract class RunsContract implements BaseColumns {

        public static final String TABLE_NAME = "runs";

        public static final String ID = "id";
        public static final String TIME = "time";
        public static final String DISTANCE = "distance";
        public static final String RATING = "rating";
        public static final String DATE = "date";
    }
}
