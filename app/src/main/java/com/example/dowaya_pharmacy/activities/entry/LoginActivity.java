package com.example.dowaya_pharmacy.activities.entry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dowaya_pharmacy.R;
import com.example.dowaya_pharmacy.StaticClass;
import com.example.dowaya_pharmacy.activities.TermsActivity;
import com.example.dowaya_pharmacy.activities.core.CoreActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    EditText emailET, passwordET;
    TextView errorTV;
    ProgressDialog progressDialog;
    FirebaseFirestore database;
    SharedPreferences sharedPreferences;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).hide();
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(StaticClass.SHARED_PREFERENCES, MODE_PRIVATE);

        emailET = findViewById(R.id.emailET);
        emailET.requestFocus();
        passwordET = findViewById(R.id.passwordET);
        errorTV = findViewById(R.id.errorTV);
        progressDialog = new ProgressDialog(this);
    }

    public void login(View view){
        email = emailET.getText().toString().isEmpty() ?
                " " : emailET.getText().toString();
        String password = passwordET.getText().toString().isEmpty() ?
                " " : passwordET.getText().toString();
        progressDialog.setMessage("Login...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Sign in with success",
                                    Toast.LENGTH_SHORT).show();
                            getDataByDocument();
                        } else {
                            displayErrorTV(R.string.authentication_failed);
                        }
                    }
                });
    }

    public void setSharedPreferences(String email, String name,
                                     String phone, String address){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StaticClass.NAME, name);
        editor.putString(StaticClass.PHONE, phone);
        editor.putString(StaticClass.ADDRESS, address);
        editor.putString(StaticClass.EMAIL, email);
        editor.apply();
        progressDialog.dismiss();
        startActivity(new Intent(
                getApplicationContext(), CoreActivity.class));
    }
    public void getDataByDocument(){
        DocumentReference documentReference =
                database.collection("stores")
                        .document(email);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        setSharedPreferences(
                                document.get("email").toString(),
                                document.get("name").toString(),
                                document.get("phone").toString(),
                                document.get("address").toString());
                        Toast.makeText(getApplicationContext(),
                                "got data by doc",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "No such store",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "get failed with " + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void toSignUp(View view){
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
    }
    public void toTermsAndConditions(View view){
        startActivity(new Intent(getApplicationContext(), TermsActivity.class));
    }
    public void displayErrorTV(int resourceID){
        errorTV.setText(resourceID);
        errorTV.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                errorTV.setVisibility(View.GONE);
            }
        }, 1500);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), SlideshowActivity.class));
    }
}
