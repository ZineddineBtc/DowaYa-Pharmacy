package com.example.dowaya_pharmacy.activities.core.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.dowaya_pharmacy.R;
import com.example.dowaya_pharmacy.StaticClass;
import com.example.dowaya_pharmacy.activities.core.RequestDescriptionActivity;
import com.example.dowaya_pharmacy.daos.RequestHistoryDAO;
import com.example.dowaya_pharmacy.models.Medicine;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class RequestListFragment extends Fragment {

    private View fragmentView;
    private Context context;
    private SearchView requestSearchView;
    private ListView requestLV;
    private ArrayAdapter adapter;
    private FirebaseFirestore database;
    private ArrayList<String> nameList = new ArrayList<>(),
            copyList = new ArrayList<>();
    private ArrayList<Medicine> requestList = new ArrayList<>();
    private RequestHistoryDAO requestHistoryDAO;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_request_list, container, false);
        context = fragmentView.getContext();
        requestList.clear();
        copyList.clear();
        nameList.clear();
        requestHistoryDAO = new RequestHistoryDAO(context);
        database = FirebaseFirestore.getInstance();
        findViewsByIds();
        getRequestList();
        requestSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return false;}
            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        return fragmentView;
    }
    private void findViewsByIds(){
        requestSearchView = fragmentView.findViewById(R.id.requestSearchView);
        requestLV = fragmentView.findViewById(R.id.requestLV);
    }
    private void setListView(){
        adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, nameList);
        requestLV.setAdapter(adapter);
        requestLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = nameList.get(position);
                String clickedId=null;
                for(Medicine medicine: requestList){
                    if(medicine.getName().equals(name)){
                        clickedId = medicine.getId();
                        break;
                    }
                }
                insertMedicineHistory(clickedId, name);
                startActivity(
                        new Intent(context, RequestDescriptionActivity.class)
                                .putExtra(StaticClass.MEDICINE_ID, clickedId)
                );
            }
        });
    }
    private void getRequestList(){
        database.collection("medicines-requests")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document :
                                    Objects.requireNonNull(task.getResult())) {
                                Medicine medicine = new Medicine();
                                medicine.setId(document.getId());
                                medicine.setName(document.getData().get("name").toString());
                                requestList.add(medicine);
                                nameList.add(medicine.getName());
                                copyList.add(medicine.getName());
                                setListView();
                            }
                        } else {
                            Toast.makeText(context,
                                    "Error getting documents." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void insertMedicineHistory(String id, String name){
        Medicine medicine = new Medicine();
        medicine.setId(id);
        medicine.setName(name);
        medicine.setSearchHistoryTime(
                new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).
                        format(Calendar.getInstance().getTime()));
        requestHistoryDAO.insertRequestHistory(medicine);
    }
    private void filter(String queryText){
        nameList.clear();
        if(queryText.isEmpty()) {
            nameList.addAll(copyList);
        }else{
            for(String s: copyList) {
                if(s.toLowerCase().contains(queryText.toLowerCase())) {
                    nameList.add(s);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}