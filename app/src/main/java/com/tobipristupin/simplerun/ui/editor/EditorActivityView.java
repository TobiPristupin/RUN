package com.tobipristupin.simplerun.ui.editor;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.data.interfaces.RunRepository;
import com.tobipristupin.simplerun.data.manager.FirebaseRepository;
import com.tobipristupin.simplerun.data.manager.SharedPrefRepository;
import com.tobipristupin.simplerun.data.model.Run;
import com.tobipristupin.simplerun.ui.editor.dialog.DistanceDialog;
import com.tobipristupin.simplerun.ui.editor.dialog.RatingDialog;
import com.tobipristupin.simplerun.ui.editor.dialog.TimeDialog;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

/**
 * Activity to create and edit runs.
 */
public class EditorActivityView extends AppCompatActivity implements EditorView {

    private RunRepository repo;
    private EditorPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        initToolbar();

        repo = new FirebaseRepository();
        presenter = new EditorPresenter(this, repo, new SharedPrefRepository(EditorActivityView.this));

        Intent intent = getIntent();
        Run runFromIntent = intent.getParcelableExtra(getString(R.string.run_intent_key));

        presenter.onCreateView(runFromIntent);

        initDistanceField();
        initTimeField();
        initDateField();
        initRatingField();
        animateViewsEntrance();
    }

    @Override
    public void showAddedRunSuccessfullyToast() {
        Toasty.success(EditorActivityView.this, "Successfully Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showInvalidFieldsToast() {
        Toasty.warning(EditorActivityView.this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishView() {
        supportFinishAfterTransition();
    }

    @Override
    public void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }

    @Override
    public String getDistanceText() {
        return ((TextView) findViewById(R.id.editor_distance_text)).getText().toString();
    }

    @Override
    public void setDistanceText(String text) {
        ((TextView) findViewById(R.id.editor_distance_text)).setText(text);
    }

    @Override
    public String getDateText() {
        return ((TextView) findViewById(R.id.editor_date_text)).getText().toString();
    }

    @Override
    public void setDateText(String text) {
        ((TextView) findViewById(R.id.editor_date_text)).setText(text);
    }

    @Override
    public String getTimeText() {
        return ((TextView) findViewById(R.id.editor_time_text)).getText().toString();
    }

    @Override
    public void setTimeText(String text) {
        ((TextView) findViewById(R.id.editor_time_text)).setText(text);
    }

    @Override
    public String getRatingText() {
        return ((TextView) findViewById(R.id.editor_rating_text)).getText().toString();
    }

    @Override
    public void setRatingText(String text) {
        ((TextView) findViewById(R.id.editor_rating_text)).setText(text);
    }

    @Override
    public void setSupportActionBarTitle(String text) {
        getSupportActionBar().setTitle(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_toolbar_menu, menu);
        return true;
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

    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.editor_toolbar);
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
        RelativeLayout dateView = findViewById(R.id.editor_date_view);

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void initDistanceField() {
        RelativeLayout distanceView = findViewById(R.id.editor_distance_view);

        distanceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDistanceDialog();
            }
        });
    }

    private void initTimeField() {
        RelativeLayout timeView = findViewById(R.id.editor_time_view);

        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }

        });
    }

    private void initRatingField() {
        RelativeLayout field = findViewById(R.id.editor_rating_view);

        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });
    }

    private void showDatePickerDialog(){
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog = new DatePickerDialog(EditorActivityView.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                presenter.onDateDialogPositiveButton(year, month, dayOfMonth);
            }
        }, year, month, dayOfMonth);

        dateDialog.show();
    }

    private void showDistanceDialog(){
        DistanceDialog distanceDialog = new DistanceDialog(new DistanceDialog.onPositiveButtonListener() {
                    @Override
                    public void onClick(String distanceValue) {
                        presenter.onDistanceDialogPositiveButton(distanceValue);
                    }
                }, new SharedPrefRepository(EditorActivityView.this));

        distanceDialog.makeDialog(EditorActivityView.this).show();
    }

    private void showTimeDialog(){
        TimeDialog dialog = new TimeDialog(new TimeDialog.onPositiveButtonListener() {
            @Override
            public void onClick(String timeValue) {
                presenter.onTimeDialogPositiveButton(timeValue);
            }
        });

        dialog.makeDialog(EditorActivityView.this).show();
    }

    private void showRatingDialog(){
        RatingDialog ratingDialog = new RatingDialog(new RatingDialog.onPositiveButtonListener() {
            @Override
            public void onClick(String ratingValue) {
                presenter.onRatingDialogPositiveButton(ratingValue);
            }
        });

        ratingDialog.makeDialog(EditorActivityView.this).show();
    }

    private void animateViewsEntrance() {
        LinearLayout linearLayout = findViewById(R.id.content_main);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            child.animate()
                    .setDuration(750)
                    .alpha(1.0f);

        }
    }




















}
