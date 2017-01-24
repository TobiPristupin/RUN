package com.example.tobias.run;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.tobias.run.DatabaseContract.RunsContract;


/**
 * Helper class to manage database
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "run.db";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = new StringBuilder().append("CREATE TABLE IF NOT EXISTS ")
                .append(RunsContract.TABLE_NAME)
                .append("(")
                .append(RunsContract.ID).append(" INTEGER PRIMARY KEY AUTO_INCREMENT, ")
                .append(RunsContract.DATE).append(" INTEGER, ")
                .append(RunsContract.DISTANCE).append(" INTEGER, ")
                .append(RunsContract.TIME).append(" INTEGER, ")
                .append(RunsContract.RATING).append(" INTEGER, ")
                .append(RunsContract.UNIT).append(" TEXT").append(")")
                .toString();
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + RunsContract.TABLE_NAME;
        db.execSQL(dropTableQuery);
        //Create table again
        onCreate(db);
    }

    public void addRun(TrackedRun run){

    }
}

