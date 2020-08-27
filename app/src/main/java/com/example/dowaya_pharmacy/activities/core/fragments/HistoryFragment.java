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
import com.example.dowaya_pharmacy.daos.RequestHistoryDAO;
import com.example.dowaya_pharmacy.models.Medicine;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private View fragmentView;
    private Context context;
    private TextView requestTV;
    private RecyclerView requestRV;
    private RequestHistoryAdapter requestAdapter;
    private ArrayList<Medicine> requestList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_history, container, false);
        context = fragmentView.getContext();
        findViewsByIds();
        initializeLists();
        setRecyclerViews();

        return fragmentView;
    }
    private void findViewsByIds(){
        requestTV = fragmentView.findViewById(R.id.requestTV);
        requestTV = fragmentView.findViewById(R.id.requestTV);
        requestRV = fragmentView.findViewById(R.id.requestRV);
    }
    private void initializeLists(){
        requestList = new RequestHistoryDAO(context).getAllRequestHistory();
        Toast.makeText(context, String.valueOf(requestList.size()), Toast.LENGTH_SHORT).show();
    }
    private void setRecyclerViews(){
        requestAdapter = new RequestHistoryAdapter(context, requestList);
        requestRV.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        requestRV.setAdapter(requestAdapter);
    }

}








