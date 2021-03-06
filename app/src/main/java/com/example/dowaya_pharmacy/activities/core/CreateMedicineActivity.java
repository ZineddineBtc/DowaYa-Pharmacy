package com.example.dowaya_pharmacy.activities.core;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dowaya_pharmacy.R;
import com.example.dowaya_pharmacy.StaticClass;
import com.example.dowaya_pharmacy.daos.CreatedMedicineHistoryDAO;
import com.example.dowaya_pharmacy.models.Medicine;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateMedicineActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private ImageView photoIV;
    private TextView usernameTV, emailTV, phoneTV, errorTV;
    private EditText medicineNameET, medicineDescriptionET,
            medicineDoseET, minPriceET, maxPriceET;
    private String medicineName, email, priceRange;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_medicine);
        database = FirebaseFirestore.getInstance();
        findViewsByIds();
        setUserData();
    }
    private void findViewsByIds(){
        photoIV = findViewById(R.id.photoIV);
        usernameTV = findViewById(R.id.usernameTV);
        emailTV = findViewById(R.id.emailTV);
        phoneTV = findViewById(R.id.phoneTV);
        medicineNameET = findViewById(R.id.medicineNameET);
        medicineDescriptionET = findViewById(R.id.medicineDescriptionET);
        minPriceET = findViewById(R.id.medicineMinPriceET);
        maxPriceET = findViewById(R.id.medicineMaxPriceET);
        medicineDoseET = findViewById(R.id.medicineDoseET);
        errorTV = findViewById(R.id.errorTV);
    }
    private void setUserData(){
        sharedPreferences = getSharedPreferences(StaticClass.SHARED_PREFERENCES, MODE_PRIVATE);
        String photoUri = sharedPreferences.getString(StaticClass.PHOTO, "");
        if(!photoUri.isEmpty()){
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), Uri.parse(photoUri));
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "IO Exception",
                        Toast.LENGTH_LONG).show();
            }
            photoIV.setImageBitmap(imageBitmap);
        }
        usernameTV.setText(sharedPreferences.getString(StaticClass.NAME, "no name"));
        emailTV.setText(sharedPreferences.getString(StaticClass.EMAIL, "no email"));
        phoneTV.setText(sharedPreferences.getString(StaticClass.PHONE, "no phone number"));
    }
    private void writeCreatedMedicineHistory(){
        Medicine medicine = new Medicine();
        medicine.setId(medicineName);
        medicine.setName(medicineName);
        medicine.setCreatedHistoryTime(StaticClass.getCurrentTime());
        CreatedMedicineHistoryDAO createdMedicineHistoryDAO = new CreatedMedicineHistoryDAO(this);
        createdMedicineHistoryDAO.insertCreateMedicineHistory(medicine);
    }
    private void writeDescription(){
        Map<String, Object> medicineDescription = new HashMap<>();
        medicineDescription.put("name", medicineName);
        medicineDescription.put("description", medicineDescriptionET.getText().toString());
        medicineDescription.put("price", priceRange);
        medicineDescription.put("dose", medicineDoseET.getText().toString());

        database.collection("medicines-descriptions")
                .document(medicineName)
                .set(medicineDescription)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        writeName();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Error writing description",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void writeName(){
        Map<String, Object> medicineNameMap = new HashMap<>();
        medicineNameMap.put("name", medicineName);
        database.collection("medicines-names")
                .document(medicineName)
                .set(medicineNameMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        writeMedicineStore();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Error writing name",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void writeMedicineStore(){
        DocumentReference storeReference = database.collection("stores")
                .document(email);
        DocumentReference medicinesStores =
                database.collection("medicines-stores")
                        .document(medicineName);
        ArrayList<DocumentReference> referenceList = new ArrayList<>();
        referenceList.add(storeReference);
        Map<String, ArrayList> referenceMap = new HashMap<>();
        referenceMap.put("stores", referenceList);
        medicinesStores.set(referenceMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(), CoreActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Error writing medicine store",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void uploadMedicine(){
        database.collection("medicines-names")
                .document(medicineName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (!document.exists()) {
                                writeDescription();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "get failed with " + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private void create(){
        email = sharedPreferences.getString(StaticClass.EMAIL, "");
        String temp = medicineNameET.getText().toString().trim().toLowerCase();
        if(temp.length()>2){
            medicineName = temp.substring(0, 1).toUpperCase() + temp.substring(1);
        }else{
            medicineName = "";
        }
        priceRange = minPriceET.getText().toString().trim()+"-"+maxPriceET.getText().toString().trim()+" DA";
        if(!medicineNameET.getText().toString().isEmpty()
                && !medicineDescriptionET.getText().toString().isEmpty()){
            writeCreatedMedicineHistory();
            uploadMedicine();
        }else{
            errorTV.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    errorTV.setVisibility(View.GONE);
                }
            }, 1500);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_check, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.check){
            create();
        }
        return false;
    }
}




