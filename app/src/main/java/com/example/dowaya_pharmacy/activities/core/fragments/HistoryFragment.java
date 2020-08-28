package com.example.dowaya_pharmacy.activities.core.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dowaya_pharmacy.R;
import com.example.dowaya_pharmacy.adapters.RequestHistoryAdapter;
import com.example.dowaya_pharmacy.adapters.UserHistoryAdapter;
import com.example.dowaya_pharmacy.daos.RequestHistoryDAO;
import com.example.dowaya_pharmacy.daos.UserHistoryDAO;
import com.example.dowaya_pharmacy.models.Medicine;
import com.example.dowaya_pharmacy.models.User;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private View fragmentView;
    private Context context;
    private TextView requestTV, userTV, emptyListTV;
    private RecyclerView requestRV, userRV;
    private ArrayList<Medicine> requestList;
    private ArrayList<User> userList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_history, container, false);
        context = fragmentView.getContext();
        findViewsByIds();
        initializeLists();
        setRecyclerViews();
        tabRequest();
        setClickListeners();
        return fragmentView;
    }
    private void findViewsByIds(){
        requestTV = fragmentView.findViewById(R.id.requestTV);
        requestRV = fragmentView.findViewById(R.id.requestRV);
        userTV = fragmentView.findViewById(R.id.userTV);
        userRV = fragmentView.findViewById(R.id.userRV);
        emptyListTV = fragmentView.findViewById(R.id.emptyListTV);
    }
    private void initializeLists(){
        requestList = new RequestHistoryDAO(context).getAllRequestHistory();
        userList = new UserHistoryDAO(context).getAllUserHistory();
    }
    private void setRecyclerViews(){
        RequestHistoryAdapter requestAdapter = new RequestHistoryAdapter(context, requestList);
        requestRV.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        requestRV.setAdapter(requestAdapter);

        UserHistoryAdapter userAdapter = new UserHistoryAdapter(context, userList);
        userRV.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        userRV.setAdapter(userAdapter);
    }
    private void tabRequest(){
        requestRV.setVisibility(requestList.isEmpty() ? View.GONE : View.VISIBLE);
        emptyListTV.setVisibility(requestList.isEmpty() ? View.VISIBLE : View.GONE);
        userRV.setVisibility(View.GONE);
        requestTV.setTextColor(context.getColor(R.color.white));
        requestTV.setBackgroundColor(context.getColor(R.color.green));
        userTV.setTextColor(context.getColor(R.color.dark_grey));
        userTV.setBackgroundColor(context.getColor(R.color.white));
    }
    private void tabUser(){
        userRV.setVisibility(requestList.isEmpty() ? View.GONE : View.VISIBLE);
        emptyListTV.setVisibility(requestList.isEmpty() ? View.VISIBLE : View.GONE);
        requestRV.setVisibility(View.GONE);
        userTV.setTextColor(context.getColor(R.color.white));
        userTV.setBackgroundColor(context.getColor(R.color.green));
        requestTV.setTextColor(context.getColor(R.color.dark_grey));
        requestTV.setBackgroundColor(context.getColor(R.color.white));
    }
    private void setClickListeners(){
        requestTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabRequest();
            }
        });
        userTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabUser();
            }
        });
    }
}








