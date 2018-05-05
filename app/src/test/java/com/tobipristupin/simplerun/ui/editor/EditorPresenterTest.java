package com.tobipristupin.simplerun.ui.editor;

import com.tobipristupin.simplerun.data.interfaces.PreferencesRepository;
import com.tobipristupin.simplerun.data.interfaces.Repository;
import com.tobipristupin.simplerun.data.model.DistanceUnit;
import com.tobipristupin.simplerun.data.model.Run;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class EditorPresenterTest {


    @Mock
    EditorActivityView view;
    @Mock
    Repository<Run> runRepo;
    @Mock
    PreferencesRepository preferencesRepo;
    Run run = Run.fromKilometers("4.3 km", "01:00:00", "Mon, 4/30/2018", "3");
    EditorPresenter presenter;

    @Before
    public void setup(){
        presenter = new EditorPresenter(view, runRepo, preferencesRepo);
        when(preferencesRepo.getDistanceUnit()).thenReturn(DistanceUnit.KM);
        //Manually set text fields equal to the run field.
        when(view.getDateText()).thenReturn("Mon, 4/30/2018");
        when(view.getDistanceText()).thenReturn("4.3 km");
        when(view.getRatingText()).thenReturn("3");
        when(view.getTimeText()).thenReturn("01:00:00");
    }

    @Test
    public void shouldSetEditModeIfNoRunWasPassed(){
        presenter.onCreateView(run);
        verify(view).setRatingText(String.valueOf(run.getRating()));
        verify(view).setActionBarEditTitle();
    }

    @Test
    public void shouldAddToDatabaseIfCorrectData(){
        presenter.onSaveButtonPressed();
        verify(runRepo).add(run);
    }

    @Test
    public void shouldNotAddToDatabaseIfInvalidData(){
        String emptyFieldFlag = "N/A";
        when(view.getEmptyFieldText()).thenReturn(emptyFieldFlag);
        when(view.getDateText()).thenReturn(emptyFieldFlag);

        presenter.onSaveButtonPressed();
        verify(runRepo, never()).add(any());
    }

    @Test
    public void shouldUpdateIfIsInEditMode(){
        int newRating = run.getRating() + 1;
        when(view.getRatingText()).thenReturn(String.valueOf(newRating));
        presenter.onCreateView(run);
        presenter.onSaveButtonPressed();
        run.setRating(newRating);
        verify(runRepo).update(run);
    }

    @Test
    public void shouldUpdateViewsDate(){
        presenter.onDateDialogPositiveButton(2018, 5, 1);
        verify(view).setDateText("Tue, 5/1/2018");
    }

}

