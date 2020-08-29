package com.example.dowaya_pharmacy.activities.entry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.dowaya_pharmacy.R;
import com.example.dowaya_pharmacy.adapters.SlideshowAdapter;

import java.util.Objects;

public class SlideshowActivity extends AppCompatActivity {

    ViewPager viewPager;
    SlideshowAdapter imageAdapter;
    LinearLayout sliderDotsPanel;
    int dotsCount;
    ImageView[] dots;
    Button nextSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);
        Objects.requireNonNull(getSupportActionBar()).hide();
        findViewsByIds();
        nextSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextLogin(v);
            }
        });
        setSliderDotsPanel();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i< dotsCount; i++){
                    dots[i].setImageDrawable(getDrawable(R.drawable.nonactive_dot));
                }
                dots[position].setImageDrawable(getDrawable(R.drawable.active_dot));
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch(viewPager.getCurrentItem()){
                    case 0:
                        nextSignUpButton.setText(R.string.next);
                        break;
                    case 1:
                        nextSignUpButton.setText(R.string.login);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }
    public void findViewsByIds(){
        nextSignUpButton = findViewById(R.id.nextSignUpButton);
        viewPager = findViewById(R.id.view_pager);
        sliderDotsPanel = findViewById(R.id.slider_dots);
        imageAdapter = new SlideshowAdapter(this);
        viewPager.setAdapter(imageAdapter);
    }
    public void setSliderDotsPanel(){
        dotsCount = imageAdapter.getCount();
        dots = new ImageView[dotsCount];

        for(int i = 0; i < dotsCount; i++){
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getDrawable(R.drawable.nonactive_dot));
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotsPanel.addView(dots[i], params);

        }
        dots[0].setImageDrawable(getDrawable(R.drawable.active_dot));
    }
    public void nextLogin(View view){
        switch(viewPager.getCurrentItem()){
            case 0:
                viewPager.setCurrentItem(1);
                nextSignUpButton.setText(R.string.next);
                break;
            case 1:
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
        }
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
