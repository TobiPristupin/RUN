package com.tobipristupin.simplerun.ui.editor;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tobipristupin.simplerun.BR;
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.app.BaseAppCompatActivity;
import com.tobipristupin.simplerun.data.repository.FirebaseRepository;
import com.tobipristupin.simplerun.data.repository.SharedPrefRepository;
import com.tobipristupin.simplerun.data.model.Run;
import com.tobipristupin.simplerun.databinding.ActivityEditorBinding;
import com.tobipristupin.simplerun.ui.sharedui.ToastyWrapper;
import com.tobipristupin.simplerun.ui.editor.dialog.DistanceDialog;
import com.tobipristupin.simplerun.ui.editor.dialog.RatingDialog;
import com.tobipristupin.simplerun.ui.editor.dialog.TimeDialog;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

/**
 * Activity to create and edit runs.
 */
public class EditorActivityView extends BaseAppCompatActivity {

    public static final String RUN_TO_EDIT = "editor_run_to_edit";
    private ActivityEditorBinding binding;
    private EditorViewModel viewModel;
    private ToastyWrapper toastyWrapper = new ToastyWrapper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_editor);
        binding.setLifecycleOwner(this);
        binding.setModel(viewModel);

        setSupportActionBar(R.id.editor_toolbar, R.drawable.ic_close_white_24dp);
        changeStatusBarColor(R.color.colorPrimaryDark);


        initFields();
        animateViewsEntrance();

        subscribeUI();
    }

    private Run getRunFromIntent() {
        Intent intent = getIntent();
        return intent.getParcelableExtra(RUN_TO_EDIT);
    }

    private void initViewModel() {
        ViewModelProvider.Factory factory = new EditorViewModel.Factory(
                new SharedPrefRepository(this), new FirebaseRepository(), getString(R.string.all_empty_field)
        );
        viewModel = obtainViewModel(this, EditorViewModel.class, factory);

        Run runFromIntent = getRunFromIntent();
        if (runFromIntent != null){
            viewModel.setRunToEdit(runFromIntent);
        }
    }

    private void subscribeUI(){
        bindToasts();
        bindFinishView();
        bindVibration();
        bindActionBarTitle();
    }

    private void bindActionBarTitle() {
        viewModel.getActionBarTitle().observe(this, resId -> {
            getSupportActionBar().setTitle(resId);
        });
    }

    private void bindVibration() {
        viewModel.getVibrate().observe(this, aVoid -> {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(100);
        });
    }

    private void bindFinishView() {
        viewModel.getFinishView().observe(this, aVoid -> {
            supportFinishAfterTransition();
        });
    }

    private void bindToasts() {
        viewModel.getShowSuccessToast().observe(this, (resId) -> {
            toastyWrapper.show(Toasty.success(EditorActivityView.this, getString(resId)));
        });

        viewModel.getShowErrorToast().observe(this, (resId) -> {
            toastyWrapper.show(Toasty.warning(EditorActivityView.this, getString(resId)));
        });
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
                viewModel.onHomePressed();
                break;
            case R.id.editor_save:
                viewModel.onSaveButtonPressed();
        }

        return true;
    }

    private void initFields(){
        binding.editorDateView.setOnClickListener(v -> showDatePickerDialog());
        binding.editorDistanceView.setOnClickListener(v -> showDistanceDialog());
        binding.editorTimeView.setOnClickListener(v -> showTimeDialog());
        binding.editorRatingView.setOnClickListener(v -> showRatingDialog());
    }

    private void showDatePickerDialog(){
        Calendar calendar = Calendar.getInstance();

        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog = new DatePickerDialog(EditorActivityView.this, (view, year, month, dayOfMonth) -> {
            //Add one to month because date dialog uses a 0-11 range while viewmodel accepts 1-12
            viewModel.onDateDialogFinish(year, month + 1, dayOfMonth);
        }, y, m, d);

        dateDialog.show();
    }

    private void showDistanceDialog(){
        DistanceDialog distanceDialog = new DistanceDialog(
                new SharedPrefRepository(EditorActivityView.this),

                (wholeNum, fractionalNum) -> viewModel.onDistanceDialogFinish(wholeNum, fractionalNum));

        distanceDialog.makeDialog(EditorActivityView.this).show();
    }

    private void showTimeDialog(){
        TimeDialog dialog = new TimeDialog((hour, minute, second) -> viewModel.onTimeDialogFinish(hour, minute, second));

        dialog.makeDialog(EditorActivityView.this).show();
    }

    private void showRatingDialog(){
        RatingDialog ratingDialog = new RatingDialog(ratingValue -> viewModel.onRatingDialogFinish(ratingValue));

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
