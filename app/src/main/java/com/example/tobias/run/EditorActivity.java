package com.example.tobias.run;

import android.app.*;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import org.joda.time.DateTime;

import java.util.Calendar;

/**
 * Activity that allows user to complete distance, time, rating and date fields when adding /
 * editing a run. This class opens different dialogs to input the data, which then call
 * setDateValue (in the case of the date popup) to pass the inputted data into this class.
 */
public class EditorActivity extends AppCompatActivity {

    String distanceText = null;
    String timeText = null;
    DateTime dateValue = null;
    String ratingText = null;
    Activity activity;
    private final int DATE_DIALOG_ID = 999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        activity = this;
        initToolbar();
        initFields();
        if (getIntent().getBooleanExtra("edit_mode", false)){
            //If intent is sent with edit_mode true, get data of corresponding tracked runs and add
            //to fields.
            //TODO: get data and set
        }



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

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.editor_toolbar);
        setSupportActionBar(toolbar);
        changeStatusBarColor();
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

    private void initDistanceField(){
        RelativeLayout field = (RelativeLayout) findViewById(R.id.editor_distance_view);
        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DistancePickerDialog(activity).make().show();
            }
        });
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
                    dateValue = new DateTime(year, month, dayOfMonth, 0, 0);
                }
            }, year, month, day );
        }
        return null;
    }

    private void initTimeField(){
        RelativeLayout field = (RelativeLayout) findViewById(R.id.editor_time_view);
        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(activity).make().show();
            }
        });
    }

    private void initRatingField(){
        RelativeLayout field = (RelativeLayout) findViewById(R.id.editor_rating_view);
        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RatingPickerDialog(activity).make().show();
            }
        });
    }


}
