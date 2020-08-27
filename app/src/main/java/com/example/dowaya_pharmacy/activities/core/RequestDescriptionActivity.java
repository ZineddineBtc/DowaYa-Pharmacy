package com.example.dowaya_pharmacy.activities.core;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dowaya_pharmacy.R;
import com.example.dowaya_pharmacy.StaticClass;
import com.example.dowaya_pharmacy.daos.UserHistoryDAO;
import com.example.dowaya_pharmacy.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class RequestDescriptionActivity extends AppCompatActivity {

    LinearLayout toggleableLL, shadeLL, userDescriptionLL;
    TextView nameTV, descriptionTV, doseTV, usernameTV, emailTV, phoneTV, addressTV, cityTV;
    ImageView toggleInfoIV;
    ListView requestLV;
    ArrayAdapter adapter;
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<User> userList = new ArrayList<>();
    ArrayList<DocumentReference> referenceList;
    String medicineId;
    ProgressDialog progressDialog;
    FirebaseFirestore database;
    boolean medicineDescriptionShown=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_description);
        setActionBarTitle("Request Description");
        medicineId = getIntent().getStringExtra(StaticClass.MEDICINE_ID);
        progressDialog = new ProgressDialog(this);
        database = FirebaseFirestore.getInstance();
        findViewsByIds();
        getRequestData();
        setRequestLV();

    }
    public void findViewsByIds(){
        toggleableLL = findViewById(R.id.toggleableLL);
        nameTV = findViewById(R.id.nameTV);
        descriptionTV = findViewById(R.id.descriptionTV);
        doseTV = findViewById(R.id.doseTV);
        toggleInfoIV = findViewById(R.id.toggleInfoIV);
        requestLV = findViewById(R.id.requestLV);
        shadeLL = findViewById(R.id.shadeLL);
        userDescriptionLL = findViewById(R.id.userDescriptionLL);
        usernameTV = findViewById(R.id.usernameTV);
        emailTV = findViewById(R.id.emailTV);
        phoneTV = findViewById(R.id.phoneTV);
        addressTV = findViewById(R.id.addressTV);
    }
    public void toggleInfo(View view){
        toggleableLL.setVisibility(medicineDescriptionShown ? View.GONE : View.VISIBLE);
        toggleInfoIV.setImageResource(medicineDescriptionShown ?
                R.drawable.ic_drop_up_black : R.drawable.ic_drop_down_black);
        medicineDescriptionShown = !medicineDescriptionShown;
    }
    public void getRequestData(){
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        DocumentReference documentReference =
                database.collection("medicines-requests")
                        .document(medicineId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        nameTV.setText(document.get("name").toString());
                        descriptionTV.setText(document.get("description").toString());
                        doseTV.setText(document.get("dose").toString());
                        referenceList = (ArrayList<DocumentReference>)
                                document.get("requesters");
                        getRequesters();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "No such document",
                                Toast.LENGTH_SHORT).show();
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "get failed with " + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        progressDialog.dismiss();
    }
    public void getRequesters(){
        for (DocumentReference reference: referenceList){
            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User user = new User();
                            user.setUsername(document.get("username").toString());
                            user.setEmail(document.get("email").toString());
                            user.setPhone(document.get("phone").toString());
                            user.setAddress(document.get("address").toString());
                            user.setCity(document.get("city").toString());
                            nameList.add(user.getUsername()+" "+user.getCity());
                            userList.add(user);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "No such document",
                                    Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "No such document");
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "get failed with " + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void setRequestLV(){
        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,
                nameList);
        requestLV.setAdapter(adapter);
        requestLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setRequesterData(position);
                showDescription();
                insertUserHistory(position);
            }
        });
    }
    public void insertUserHistory(int position){
        User user = new User();
        user.setUsername(userList.get(position).getUsername());
        user.setId(userList.get(position).getEmail());
        user.setSearchHistoryTime(
                new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).
                        format(Calendar.getInstance().getTime()));
        UserHistoryDAO userHistoryDAO = new UserHistoryDAO(this);
        userHistoryDAO.insertUserHistory(user);
    }
    public void setRequesterData(int position){
        usernameTV.setText(userList.get(position).getUsername());
        emailTV.setText(userList.get(position).getEmail());
        phoneTV.setText(userList.get(position).getPhone());
        addressTV.setText(userList.get(position).getAddress());
    }
    public void showDescription(){
        shadeLL.setVisibility(View.VISIBLE);
        userDescriptionLL.setVisibility(View.VISIBLE);
    }
    public void hideDescription(View view){
        shadeLL.setVisibility(View.GONE);
        userDescriptionLL.setVisibility(View.GONE);
    }
    public void dialPhone(View view){
        String phoneNumber = phoneTV.getText().toString()
                .replaceAll("-", "");
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (checkSelfPermission(Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }
    public void locatePharmacy(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(
                Uri.parse("https://www.google.com/maps/search/" +
                        addressTV.getText().toString()));
        startActivity(browserIntent);
    }
    public void setActionBarTitle(String title){
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
        Objects.requireNonNull(getSupportActionBar()).setTitle(
                Html.fromHtml("<font color=\"#ffffff\"> "+title+" </font>")
        );
    }
    @Override
    public void onBackPressed(){
        if(shadeLL.getVisibility()==View.VISIBLE){
            hideDescription(shadeLL);
        }else{
            startActivity(new Intent(getApplicationContext(),
                    CoreActivity.class));
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
