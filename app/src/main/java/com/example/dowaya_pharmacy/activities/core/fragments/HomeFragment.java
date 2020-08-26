package com.example.dowaya_pharmacy.activities.core.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dowaya_pharmacy.R;
import com.example.dowaya_pharmacy.activities.entry.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {

    private View fragmentView;
    private Button signOutButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        signOutButton = fragmentView.findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(fragmentView.getContext(), LoginActivity.class));
            }
        });

        return fragmentView;
    }
}