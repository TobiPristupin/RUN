package com.tobipristupin.simplerun.ui.editor;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.app.BaseAppCompatActivity;
import com.tobipristupin.simplerun.data.repository.Repository;
import com.tobipristupin.simplerun.data.repository.FirebaseRepository;
import com.tobipristupin.simplerun.data.repository.SharedPrefRepository;
import com.tobipristupin.simplerun.data.model.Run;
import com.tobipristupin.simplerun.ui.ToastyWrapper;
import com.tobipristupin.simplerun.ui.editor.dialog.DistanceDialog;
import com.tobipristupin.simplerun.ui.editor.dialog.RatingDialog;
import com.tobipristupin.simplerun.ui.editor.dialog.TimeDialog;

import java.util.Calendar;

/**
 * Activity to create and edit runs.
 */
public class EditorActivityView extends BaseAppCompatActivity implements EditorView {

    public static final String INTENT_KEY = "editor_activity_intent";
    private Repository<Run> repo;
    private EditorPresenter presenter;
    private ToastyWrapper addedRunToasty = new ToastyWrapper();
    private ToastyWrapper invalidFieldToasty = new ToastyWrapper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        setSupportActionBar(R.id.editor_toolbar, R.drawable.ic_close_white_24dp);
        changeStatusBarColor(R.color.colorPrimaryDark);

        repo = FirebaseRepository.getInstance();
        presenter = new EditorPresenter(this, repo, new SharedPrefRepository(EditorActivityView.this));

        Intent intent = getIntent();
        Run runFromIntent = intent.getParcelableExtra(INTENT_KEY);

        presenter.onCreateView(runFromIntent);

        initDistanceField();
        initTimeField();
        initDateField();
        initRatingField();
        animateViewsEntrance();
    }

    @Override
    public void showAddedRunSuccessfullyToast() {
        addedRunToasty.showSuccess(EditorActivityView.this, getString(R.string.editor_activityview_added), Toast.LENGTH_SHORT);
    }

    @Override
    public void showInvalidFieldsToast() {
        invalidFieldToasty.showWarning(EditorActivityView.this, getString(R.string.editor_activityview_fillin), Toast.LENGTH_SHORT);
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
    public void setActionBarEditTitle() {
        getSupportActionBar().setTitle(R.string.editor_activity_view_toolbar_edit);
    }

    @Override
    public String getEmptyFieldText() {
        return getString(R.string.all_empty_field);
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
                //Add one to month because date dialog uses a 0-11 range while presenter accepts 1-12
                presenter.onDateDialogPositiveButton(year, month + 1, dayOfMonth);
            }
        }, year, month, dayOfMonth);

        dateDialog.show();
    }

    private void showDistanceDialog(){
        DistanceDialog distanceDialog = new DistanceDialog(
                new SharedPrefRepository(EditorActivityView.this),

                new DistanceDialog.onPositiveButtonListener() {
                    @Override
                    public void onClick(int wholeNum, int fractionalNum) {
                        presenter.onDistanceDialogPositiveButton(wholeNum, fractionalNum);
                    }
                });

        distanceDialog.makeDialog(EditorActivityView.this).show();
    }

    private void showTimeDialog(){
        TimeDialog dialog = new TimeDialog(new TimeDialog.onPositiveButtonListener() {
            @Override
            public void onClick(int hour, int minute, int second) {
                presenter.onTimeDialogPositiveButton(hour, minute, second);
            }
        });

        dialog.makeDialog(EditorActivityView.this).show();
    }

    private void showRatingDialog(){
        RatingDialog ratingDialog = new RatingDialog(new RatingDialog.onPositiveButtonListener() {
            @Override
            public void onClick(int ratingValue) {
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
