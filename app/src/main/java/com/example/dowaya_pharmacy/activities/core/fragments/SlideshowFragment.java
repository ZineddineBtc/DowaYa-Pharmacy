package com.example.dowaya_pharmacy.activities.core.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.dowaya_pharmacy.R;

public class SlideshowFragment extends Fragment {

    private View fragmentView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_slideshow, container, false);


        return fragmentView;
    }
}