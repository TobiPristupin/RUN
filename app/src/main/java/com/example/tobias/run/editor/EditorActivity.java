package com.example.tobias.run.editor;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tobias.run.R;
import com.example.tobias.run.data.FirebaseDatabaseManager;
import com.example.tobias.run.data.SharedPreferenceManager;
import com.example.tobias.run.data.SharedPreferenceRepository;
import com.example.tobias.run.data.TrackedRun;
import com.example.tobias.run.editor.dialog.DistanceDialog;
import com.example.tobias.run.editor.dialog.RatingDialog;
import com.example.tobias.run.editor.dialog.TimeDialog;
import com.example.tobias.run.utils.ConversionManager;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

/**
 * Activity that allows user to complete distance, time, rating and date fields when adding /
 * editing a run. This class shows different dialogs to input the data, and implements the onClickListener
 * for the positive button to retrieve the data
 */
public class EditorActivity extends AppCompatActivity implements EditorView {

    private final int DATE_DIALOG_ID = 999;
    private SharedPreferenceRepository preferenceManager;
    private EditorPresenter presenter;

    public static final String DISTANCE_KEY = "distance";
    public static final String RATING_KEY = "rating";
    public static final String TIME_KEY = "time";
    public static final String DATE_KEY = "date";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        initToolbar();

        SharedPreferenceRepository preferenceRepository = new SharedPreferenceManager(EditorActivity.this);
        presenter = new EditorPresenter(this, preferenceRepository);
        preferenceManager = new SharedPreferenceManager(EditorActivity.this);

        Intent intent = getIntent();
        TrackedRun trackedRun = intent.getParcelableExtra(getString(R.string.trackedrun_intent_key));

        presenter.onCreateView(trackedRun);

        initDistanceField();
        initTimeField();
        initDateField();
        initRatingField();
        animateViewsEntrance();
    }

    @Override
    public void setEditMode(TrackedRun tr) {
        getSupportActionBar().setTitle("Edit Run");

        String unit = preferenceManager.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY);
        String distanceText;
        if (unit.equals("km")){
            distanceText = ConversionManager.distanceToString(tr.getDistanceKilometres(), unit);
        } else {
            distanceText = ConversionManager.distanceToString(tr.getDistanceMiles(), unit);
        }

        ((TextView) findViewById(R.id.editor_distance_text)).setText(distanceText);

        String dateText = ConversionManager.dateToString(tr.getDate());
        ((TextView) findViewById(R.id.editor_date_text)).setText(dateText);

        String timeText = ConversionManager.timeToString(tr.getTime());
        ((TextView) findViewById(R.id.editor_time_text)).setText(timeText);

        ((TextView) findViewById(R.id.editor_rating_text)).setText(String.valueOf(tr.getRating()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //If exit  button pressed close activity with no result.
                supportFinishAfterTransition();
                break;
            case R.id.editor_save:
                presenter.onSaveButtonPressed();
        }

        return true;

    }

    @Override
    public void finishView() {
        supportFinishAfterTransition();
    }

    @Override
    public void showAddedRunSuccessfullyToast() {
        Toasty.success(EditorActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showInvalidFieldsToast() {
        Toasty.warning(EditorActivity.this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_toolbar_menu, menu);
        return true;
    }

    //Retrieves text from distance, time, ... TextViews
    @Override
    public HashMap<String, String> retrieveDataFromViews(){
        HashMap<String, String> data = new HashMap<>();
        data.put(DISTANCE_KEY, ((TextView) findViewById(R.id.editor_distance_text)).getText().toString());
        data.put(TIME_KEY, ((TextView) findViewById(R.id.editor_time_text)).getText().toString());
        data.put(RATING_KEY, ((TextView) findViewById(R.id.editor_rating_text)).getText().toString());
        data.put(DATE_KEY, ((TextView) findViewById(R.id.editor_date_text)).getText().toString());
        return data;
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.editor_toolbar);
        setSupportActionBar(toolbar);
        changeStatusBarColor();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
    }

    /**
     * AppTheme status bar color attr is set to transparent for the drawerLayout in main activity.
     * this activity uses the primary dark color as status bar color. This method sets it during runtime.
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }


    private void initDateField() {
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
        if (id == DATE_DIALOG_ID) {
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

                    TextView dateTextView = (TextView) findViewById(R.id.editor_date_text);
                    dateTextView.setText(formatter.print(dateText));
                }
            }, year, month, day);
        }
        return null;
    }

    private void initDistanceField() {
        RelativeLayout field = (RelativeLayout) findViewById(R.id.editor_distance_view);
        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DistanceDialog dialog = new DistanceDialog(EditorActivity.this, new DistanceDialog.onPositiveButtonListener() {
                    @Override
                    public void onClick(String distanceValue) {
                        TextView distanceTextView = (TextView) findViewById(R.id.editor_distance_text);
                        distanceTextView.setText(distanceValue);
                    }
                });
                dialog.makeDialog(EditorActivity.this).show();
            }
        });
        }

    private void initTimeField() {
        RelativeLayout field = (RelativeLayout) findViewById(R.id.editor_time_view);
        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeDialog timeDialog = new TimeDialog(EditorActivity.this, new TimeDialog.onPositiveButtonListener() {
                    @Override
                    public void onClick(String timeValue) {
                        TextView timeTextView = (TextView) findViewById(R.id.editor_time_text);
                        timeTextView.setText(timeValue);
                    }
                });
                timeDialog.makeDialog().show();
            }

        });
    }

    private void initRatingField() {
        RelativeLayout field = (RelativeLayout) findViewById(R.id.editor_rating_view);
        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingDialog ratingDialog = new RatingDialog(EditorActivity.this, new RatingDialog.onPositiveButtonListener() {
                    @Override
                    public void onClick(String ratingValue) {
                        TextView ratingTextView = (TextView) findViewById(R.id.editor_rating_text);
                        ratingTextView.setText(ratingValue);
                    }
                });
                ratingDialog.makeDialog().show();
            }
        });
    }


    private void animateViewsEntrance() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.content_main);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            child.animate()
                    .setDuration(750)
                    .alpha(1.0f);

        }
    }





}
