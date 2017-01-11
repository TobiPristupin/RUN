package com.example.tobias.run;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment that displays actvities tracked, and can sort them by different criteria. Accesed via the DrawerLayout
 * in MainActivity as History.
 */
public class HistoryFragment extends Fragment {

    private View rootView;

    public HistoryFragment(){
        //Required empty constuctor.
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history, container, false);
        return rootView;
    }
}
