package com.example.tobias.run.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tobias.run.data.DatabaseContract.RunsContract;

import org.joda.time.DateTime;

import java.util.ArrayList;


/**
 * Helper class to manage database
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "run.db";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = new StringBuilder().append("CREATE TABLE IF NOT EXISTS ")
                .append(RunsContract.TABLE_NAME)
                .append("(")
                .append(RunsContract.ID).append(" INTEGER PRIMARY KEY, ")
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

    public void addRun(TrackedRun run) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(RunsContract.DATE, run.getDate());
        values.put(RunsContract.DISTANCE, run.getDistance());
        values.put(RunsContract.RATING, run.getRating());
        values.put(RunsContract.UNIT, run.getUnit());
        values.put(RunsContract.TIME, run.getTime());

        db.insert(RunsContract.TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<TrackedRun> getAllTrackedRuns() {
        SQLiteDatabase db = getReadableDatabase();

        String getAllQuery = "SELECT * FROM " + RunsContract.TABLE_NAME;
        Cursor cursor = db.rawQuery(getAllQuery, null);

        ArrayList<TrackedRun> trackedRuns = retrieveAllCursorData(cursor);
        db.close();

        return trackedRuns;
    }

    public ArrayList<TrackedRun> getYearTrackedRuns() {
        long firstDayOfYearTimestamp = new DateTime().dayOfYear().withMinimumValue().getMillis() / 1000;
        long lastDayOfYearTimestamp = new DateTime().dayOfYear().withMaximumValue().getMillis() / 1000;

        String query = new StringBuilder()
                .append("SELECT * FROM ")
                .append(RunsContract.TABLE_NAME)
                .append(" WHERE ")
                .append(RunsContract.DATE)
                .append(" BETWEEN ")
                .append(firstDayOfYearTimestamp)
                .append(" AND ")
                .append(lastDayOfYearTimestamp).toString();

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);

        ArrayList<TrackedRun> trackedRuns = retrieveAllCursorData(cursor);
        database.close();
        return trackedRuns;
    }

    /**
     * Receives a Cursor object and retrieves its data into an Arraylist of TrackedRun.
     * @param cursor
     * @return Arraylist<TrackedRun>
     */
    private ArrayList<TrackedRun> retrieveAllCursorData(Cursor cursor) {
        ArrayList<TrackedRun> trackedRuns = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                trackedRuns.add(new TrackedRun(
                        cursor.getInt(cursor.getColumnIndex(RunsContract.ID)),
                        cursor.getLong(cursor.getColumnIndex(RunsContract.DATE)),
                        cursor.getDouble(cursor.getColumnIndex(RunsContract.DISTANCE)),
                        cursor.getLong(cursor.getColumnIndex(RunsContract.TIME)),
                        cursor.getInt(cursor.getColumnIndex(RunsContract.RATING)),
                        cursor.getString(cursor.getColumnIndex(RunsContract.UNIT))
                ));
            } while (cursor.moveToNext());

        }

        return trackedRuns;
    }

    public void deleteItem(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(RunsContract.TABLE_NAME, RunsContract.ID + " = ?", new String[] { String.valueOf(id) } );
        db.close();
    }

    public int getAmountOfRecords(){
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM " + RunsContract.TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);
        return cursor.getCount();
    }

    public boolean checkExists(int id){
        SQLiteDatabase database = getReadableDatabase();
        String query = new StringBuilder().append("SELECT * FROM ")
                .append(RunsContract.TABLE_NAME)
                .append(" WHERE ")
                .append(RunsContract.ID)
                .append(" = ")
                .append(id).toString();

        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() <= 0){
            database.close();
            return false;
        }

        database.close();
        return true;
    }

    public void updateRun(TrackedRun run){
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RunsContract.DATE, run.getDate());
        values.put(RunsContract.DISTANCE, run.getDistance());
        values.put(RunsContract.RATING, run.getRating());
        values.put(RunsContract.UNIT, run.getUnit());
        values.put(RunsContract.TIME, run.getTime());

        database.update(RunsContract.TABLE_NAME, values, RunsContract.ID + " = ?",
                new String[]{ String.valueOf(run.getId())});
    }

    public ArrayList<TrackedRun> getTrackedRunsBetween(long dateStartPoint, long dateEndPoint){
        SQLiteDatabase database = getReadableDatabase();
        String query = new StringBuilder().append("SELECT * FROM ")
                .append(RunsContract.TABLE_NAME)
                .append(" WHERE ")
                .append(RunsContract.DATE)
                .append(" BETWEEN ")
                .append(dateStartPoint)
                .append(" AND ")
                .append(dateEndPoint).toString();

        Cursor cursor = database.rawQuery(query, null);
        ArrayList<TrackedRun> values = retrieveAllCursorData(cursor);
        database.close();
        return values;
    }

}

