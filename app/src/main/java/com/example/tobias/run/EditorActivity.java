package com.example.tobias.run;

import android.app.*;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Activity that allows user to complete distance, time, rating and date fields when adding /
 * editing a run. This class shows different dialogs to input the data, which then return it.
 */
public class EditorActivity extends AppCompatActivity {

    String distanceValue = null;
    String timeValue = null;
    String dateValue = null;
    String ratingValue = null;
    Activity activity;
    private final int DATE_DIALOG_ID = 999;
    private SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        this.activity = this;
        this.sharedPref = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);

        initToolbar();
        initFields();

    }



    /**
     * Sets callbacks for all settings fields.
     */
    private void initFields(){
        initDistanceField();
        initTimeField();
        initDateField();
        initRatingField();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                //If home button pressed close activity with no result.
                finish();
                break;
            case R.id.editor_save :
                new DatabaseHandler(activity).addRun(
                        new TrackedRun("21,02", "02:22:03" , "Thu, 2/3/2017", "3", "km"));

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_toolbar_menu, menu);
        return true;
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.editor_toolbar);
        setSupportActionBar(toolbar);
        changeStatusBarColor();
        if (getIntent().getBooleanExtra("edit_mode", false)) {
            //If intent is sent with edit mode, change action bar title,
            getSupportActionBar().setTitle("Edit entry");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void setDistanceValue(String distanceText){
        TextView distanceTextView = (TextView) findViewById(R.id.editor_distance_text);
        distanceTextView.setText(distanceText);
        distanceValue = distanceText;
    }

    public void setTimeValue(String timeText){
        TextView timeTextView = (TextView) findViewById(R.id.editor_time_text);
        timeTextView.setText(timeText);
        timeValue = timeText;
    }

    public void setRatingValue(String ratingText){
        TextView ratingTextView = (TextView) findViewById(R.id.editor_rating_text);
        ratingTextView.setText(ratingText);
        ratingValue = ratingText;
    }

    public void setDateValue(String dateText){
        TextView dateTextView = (TextView) findViewById(R.id.editor_date_text);
        dateTextView.setText(dateText);
        dateValue = dateText;
    }

    /**
     * AppTheme status bar color attr is set to transparent for the drawerLayout in main activity.
     * this activity uses the primary dark color as status bar color. This method sets it during runtime.
     */
    private void changeStatusBarColor(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }


    private void initDateField(){
        RelativeLayout field = (RelativeLayout) findViewById(R.id.editor_date_view);
        field.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressWarnings("deprecation")
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    }

    @Override
    @SuppressWarnings("deprecation")
    /**
     * When showDialog is called, this method is called, which checks the id of the new dialog,
     * and if it corresponds with DATE_DIALOG_ID, it opens a new Date Picker dialog.
     */
    protected Dialog onCreateDialog(int id) {
        if (id == DATE_DIALOG_ID){
            //Get current year, month and day to pass it to date picker dialog.
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    DateTimeFormatter formatter = DateTimeFormat.forPattern("E, d/M/y");
                    DateTime dateText = new DateTime(year, month + 1, dayOfMonth, 0, 0);
                    setDateValue(formatter.print(dateText));
                }
            }, year, month, day );
        }
        return null;
    }

    private void initDistanceField(){
        RelativeLayout field = (RelativeLayout) findViewById(R.id.editor_distance_view);
        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create and show distance dialog
                //Inflate layout and set as custom dialog view
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater inflater = activity.getLayoutInflater();
                View rootView = inflater.inflate(R.layout.distance_dialog, null);
                builder.setView(rootView);

                //Init number pickers
                final NumberPicker numberPickerWhole = (NumberPicker)
                        rootView.findViewById(R.id.distance_picker_whole);
                numberPickerWhole.setMaxValue(99);
                numberPickerWhole.setMinValue(0);
                numberPickerWhole.setValue(1);

                final NumberPicker numberPickerDecimal = (NumberPicker)
                        rootView.findViewById(R.id.distance_picker_decimal);
                numberPickerDecimal.setMaxValue(99);
                numberPickerWhole.setMinValue(0);
                numberPickerWhole.setValue(1);

                TextView unitText = (TextView) rootView.findViewById(R.id.distance_unit);
                if(sharedPref.getString("distance_unit", null).equals("km")){
                    unitText.setText("km");
                } else {
                    unitText.setText("mi");
                }

                builder.setTitle("Distance");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Get values, format them into string and return to editor activity.
                        String distance = null;
                        if (sharedPref.getString("distance_unit", null).equals("km")){
                            distance = "" + numberPickerWhole.getValue() + ","
                                    + numberPickerDecimal.getValue() + " km";
                        } else {
                            distance = "" + numberPickerWhole.getValue() + ","
                                    + numberPickerDecimal.getValue() + " mi";
                        }
                        setDistanceValue(distance);
                        dialog.dismiss();
                    }
                });

                builder.create().show();

            }
        });
    }

    private void initTimeField(){
        RelativeLayout field = (RelativeLayout) findViewById(R.id.editor_time_view);
        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create new dialog and show
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater inflater = activity.getLayoutInflater();
                View rootView = inflater.inflate(R.layout.time_dialog, null);
                builder.setView(rootView);

                final NumberPicker numberPickerHour = (NumberPicker) rootView.findViewById(R.id.time_picker_hour);
                numberPickerHour.setMaxValue(24);
                numberPickerHour.setMinValue(0);
                numberPickerHour.setValue(0);

                final NumberPicker numberPickerMinute = (NumberPicker) rootView.findViewById(R.id.time_picker_minute);
                numberPickerMinute.setMaxValue(59);
                numberPickerMinute.setMinValue(0);
                numberPickerMinute.setValue(0);

                final NumberPicker numberPickerSecond = (NumberPicker) rootView.findViewById(R.id.time_picker_second);
                numberPickerSecond.setMaxValue(59);
                numberPickerSecond.setMinValue(0);
                numberPickerSecond.setValue(0);

                builder.setTitle("Time");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DecimalFormat df = new DecimalFormat("00");
                        String time = df.format(numberPickerHour.getValue()) + ":"
                                + df.format(numberPickerMinute.getValue()) + ":"
                                + df.format(numberPickerSecond.getValue());
                        setTimeValue(time);
                        dialog.dismiss();
                    }
                });

                builder.create().show();
            }
        });
    }

    private void initRatingField(){
        RelativeLayout field = (RelativeLayout) findViewById(R.id.editor_rating_view);
        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create and show rating dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater inflater = activity.getLayoutInflater();
                View rootView = inflater.inflate(R.layout.rating_dialog, null);
                builder.setView(rootView);

                builder.setTitle("Rating");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                final RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.rating_rating_bar);

                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        if(rating < 1){
                            ratingBar.setRating(1);
                        }
                    }
                });

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = "" + (int) ratingBar.getRating();
                        setRatingValue(value);
                        dialog.dismiss();
                    }
                });

                builder.create().show();
            }
        });
    }


}
