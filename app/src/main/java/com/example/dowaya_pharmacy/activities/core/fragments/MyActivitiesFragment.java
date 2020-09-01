package com.example.dowaya_pharmacy.activities.core.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dowaya_pharmacy.R;
import com.example.dowaya_pharmacy.adapters.CreatedMedicineHistoryAdapter;
import com.example.dowaya_pharmacy.adapters.RequestHistoryAdapter;
import com.example.dowaya_pharmacy.adapters.UserHistoryAdapter;
import com.example.dowaya_pharmacy.daos.CreatedMedicineHistoryDAO;
import com.example.dowaya_pharmacy.daos.RequestHistoryDAO;
import com.example.dowaya_pharmacy.daos.UserHistoryDAO;
import com.example.dowaya_pharmacy.models.Medicine;
import com.example.dowaya_pharmacy.models.User;

import java.util.ArrayList;

public class MyActivitiesFragment extends Fragment {

    private View fragmentView;
    private Context context;
    private TextView requestTV, userTV, createdMedicineTV, emptyListTV;
    private RecyclerView requestRV, userRV, createdMedicineRV;
    private ArrayList<Medicine> requestList, createdMedicineList;
    private ArrayList<User> userList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_my_activites, container, false);
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
        createdMedicineTV = fragmentView.findViewById(R.id.createdMedicineTV);
        createdMedicineRV = fragmentView.findViewById(R.id.createdMedicineRV);
        emptyListTV = fragmentView.findViewById(R.id.emptyListTV);
    }
    private void initializeLists(){
        requestList = new RequestHistoryDAO(context).getAllRequestHistory();
        userList = new UserHistoryDAO(context).getAllUserHistory();
        createdMedicineList = new CreatedMedicineHistoryDAO(context).getAllCreateMedicineHistory();
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

        CreatedMedicineHistoryAdapter createdMedicineAdapter =
                new CreatedMedicineHistoryAdapter(context, createdMedicineList);
        createdMedicineRV.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        createdMedicineRV.setAdapter(createdMedicineAdapter);
    }
    private void tabRequest(){
        requestRV.setVisibility(requestList.isEmpty() ? View.GONE : View.VISIBLE);
        emptyListTV.setVisibility(requestList.isEmpty() ? View.VISIBLE : View.GONE);
        userRV.setVisibility(View.GONE);
        createdMedicineRV.setVisibility(View.GONE);
        requestTV.setTextColor(context.getColor(R.color.white));
        requestTV.setBackground(context.getDrawable(R.drawable.green_background_rounded_border));
        userTV.setTextColor(context.getColor(R.color.dark_grey));
        userTV.setBackgroundColor(context.getColor(R.color.white));
        createdMedicineTV.setTextColor(context.getColor(R.color.dark_grey));
        createdMedicineTV.setBackgroundColor(context.getColor(R.color.white));
    }
    private void tabUser(){
        userRV.setVisibility(requestList.isEmpty() ? View.GONE : View.VISIBLE);
        emptyListTV.setVisibility(requestList.isEmpty() ? View.VISIBLE : View.GONE);
        requestRV.setVisibility(View.GONE);
        createdMedicineRV.setVisibility(View.GONE);
        userTV.setTextColor(context.getColor(R.color.white));
        userTV.setBackground(context.getDrawable(R.drawable.green_background_rounded_border));
        requestTV.setTextColor(context.getColor(R.color.dark_grey));
        requestTV.setBackgroundColor(context.getColor(R.color.white));
        createdMedicineTV.setTextColor(context.getColor(R.color.dark_grey));
        createdMedicineTV.setBackgroundColor(context.getColor(R.color.white));
    }
    private void tabCreatedMedicine(){
        createdMedicineRV.setVisibility(createdMedicineList.isEmpty() ? View.GONE : View.VISIBLE);
        emptyListTV.setVisibility(createdMedicineList.isEmpty() ? View.VISIBLE : View.GONE);
        requestRV.setVisibility(View.GONE);
        userRV.setVisibility(View.GONE);
        requestTV.setTextColor(context.getColor(R.color.dark_grey));
        requestTV.setBackgroundColor(context.getColor(R.color.white));
        userTV.setTextColor(context.getColor(R.color.dark_grey));
        userTV.setBackgroundColor(context.getColor(R.color.white));
        createdMedicineTV.setTextColor(context.getColor(R.color.white));
        createdMedicineTV.setBackground(context.getDrawable(R.drawable.green_background_rounded_border));
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
        createdMedicineTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabCreatedMedicine();
            }
        });
    }
}








