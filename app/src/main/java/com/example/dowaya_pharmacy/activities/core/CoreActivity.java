package com.example.dowaya_pharmacy.activities.core;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.dowaya_pharmacy.R;
import com.example.dowaya_pharmacy.StaticClass;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ImageView;
import android.widget.TextView;

public class CoreActivity extends AppCompatActivity {

    AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    ImageView userPhotoIV;
    TextView userNameTV, userEmailTV, userPhoneTV;
    SharedPreferences sharedPreferences;
    String photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);
        findViewsByIds();
        setSupportActionBar(toolbar);
        setUpNavigation();
        setUserData();
        checkBuildVersion();
    }
    public void checkBuildVersion(){
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyHavePermission()) {
                requestForSpecificPermission();
            }
        }
    }
    private boolean checkIfAlreadyHavePermission() {
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.INTERNET},
                101);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // not granted
                moveTaskToBack(true);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public void findViewsByIds(){
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
    }
    public void setUserData(){
        userPhotoIV = navigationView.getHeaderView(0).findViewById(R.id.userPhotoIV);
        userNameTV = navigationView.getHeaderView(0).findViewById(R.id.userNameTV);
        userEmailTV = navigationView.getHeaderView(0).findViewById(R.id.userEmailTV);
        userPhoneTV = navigationView.getHeaderView(0).findViewById(R.id.userPhoneTV);
        sharedPreferences = getSharedPreferences(StaticClass.SHARED_PREFERENCES, MODE_PRIVATE);
        /*
        photoUri = sharedPreferences.getString(StaticClass.PHOTO, "");
        if(!photoUri.isEmpty()){
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(
                        this.getContentResolver(), Uri.parse(photoUri));
            } catch (IOException e) {
                Toast.makeText(this, "IO Exception",
                        Toast.LENGTH_LONG).show();
            }
            userPhotoIV.setImageBitmap(imageBitmap);
        }*/
        userNameTV.setText(sharedPreferences.getString(StaticClass.NAME, "no name"));
        userEmailTV.setText(sharedPreferences.getString(StaticClass.EMAIL, "no email"));
        userPhoneTV.setText(sharedPreferences.getString(StaticClass.PHONE, "no phone number"));
    }
    public void setUpNavigation(){
        navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_requests, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_history, R.id.nav_share, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
