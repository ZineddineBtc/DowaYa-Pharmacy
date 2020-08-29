package com.example.dowaya_pharmacy.activities.core.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dowaya_pharmacy.R;
import com.example.dowaya_pharmacy.StaticClass;
import com.example.dowaya_pharmacy.activities.core.CreateMedicineActivity;
import com.example.dowaya_pharmacy.adapters.MedicineAdapter;
import com.example.dowaya_pharmacy.models.Medicine;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class MedicineListFragment extends Fragment {

    private View fragmentView;
    private Context context;
    private RecyclerView medicineRV;
    private MedicineAdapter adapter;
    private FirebaseFirestore database;
    private ArrayList<Medicine> medicineList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private DocumentReference storeReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_medicine_list, container, false);
        context = fragmentView.getContext();
        setHasOptionsMenu(true);
        database = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences(StaticClass.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        storeReference = database.collection("stores")
                .document(sharedPreferences.getString(StaticClass.EMAIL, ""));
        progressDialog = new ProgressDialog(getActivity());
        findViewsByIds();
        setRecyclerView();
        getMedicineList();

        return fragmentView;
    }
    private void findViewsByIds(){
        SearchView medicineSearchView = fragmentView.findViewById(R.id.medicineSearchView);
        medicineSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return false;}
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
        medicineRV = fragmentView.findViewById(R.id.medicineRV);
    }
    private void setRecyclerView(){
        adapter = new MedicineAdapter(context, medicineList, storeReference);
        medicineRV.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        medicineRV.setAdapter(adapter);
    }
    private void getMedicineList(){
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        database.collection("medicines-stores")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document :
                                    Objects.requireNonNull(task.getResult())) {
                                Medicine medicine = new Medicine();
                                medicine.setId(document.getId());
                                medicine.setName(document.getId());
                                ArrayList<DocumentReference> references = (ArrayList<DocumentReference>)
                                         document.get("stores");
                                medicine.setAvailable(references.contains(storeReference));
                                medicineList.add(medicine);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(context,
                                    "Error getting documents." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_medicine, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.create_medicine){
            startActivity(new Intent(fragmentView.getContext(), CreateMedicineActivity.class));
        }
        return false;
    }
}






