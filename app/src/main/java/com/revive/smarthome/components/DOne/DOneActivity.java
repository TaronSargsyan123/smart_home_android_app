package com.revive.smarthome.components.DOne;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;
import com.revive.smarthome.R;
import com.revive.smarthome.components.ROneVOne.ROneVOneTimer;
import com.revive.smarthome.firebase.DevicesDefaultLogic;

public class DOneActivity extends AppCompatActivity {
    private String name;
    private Boolean stage;
    private Slider slider;
    private TextView button;
    private TextView backButton;
    private TextView analyticsButton;
    private TextView timerButton;
    private DevicesDefaultLogic defaultLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        slider = findViewById(R.id.d_one_slider_activity);
        button = findViewById(R.id.d_one_button_activity);
        backButton = findViewById(R.id.back_d_one);
        timerButton = findViewById(R.id.timer_d_one);
        analyticsButton = findViewById(R.id.analytics_d_one);
        defaultLogic = new DevicesDefaultLogic();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                name= null;
                stage = null;
            } else {
                name= extras.getString("NAME");
                stage= extras.getBoolean("STAGE");
            }
        } else {
            name= (String) savedInstanceState.getSerializable("NAME");
            stage= (Boolean) savedInstanceState.getSerializable("STAGE");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickMainButton();
            }
        });

        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            SharedPreferences sharedPreferences = getSharedPreferences("Authorisation", MODE_PRIVATE);
            String email = sharedPreferences.getString("email", "");

            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                String currentTime = defaultLogic.getDate();
                defaultLogic.updateAnalyticsData(email, name, currentTime  + "/"+String.valueOf(getBright()));
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                String currentTime = defaultLogic.getDate();
                defaultLogic.updateAnalyticsData(email, name, currentTime  + "/"+String.valueOf(getBright()));
                defaultLogic.insertDataDOne(1, email, name, "DOne", getBright());
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        analyticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("Authorisation", MODE_PRIVATE);
                String email = sharedPreferences.getString("email", "");
                Intent intent = new Intent(getApplicationContext(), DOneAnalyticsActivity.class);
                intent.putExtra("DEVICE", name);
                intent.putExtra("EMAIL", email);
                startActivity(intent);
            }
        });
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer();
            }
        });

    }

    private void timer(){
        SharedPreferences sharedPreferences = getSharedPreferences("Authorisation", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        Intent intent = new Intent(getApplicationContext(), DOneTimer.class);
        intent.putExtra("ARGS", email.split("@")[0] + "/"+name);
        startActivity(intent);
    }

    private void clickMainButton(){
        if(stage){
            stage = false;
        }else {
            stage = true;

        }
        sendFromMain(stage);
        slider.setEnabled(stage);
    }

    private int getBright(){
        float temp = slider.getValue();
        return (int) (temp * 100);
    }



    private void sendFromMain(Boolean flag){
        Thread threadOn = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    SharedPreferences sharedPreferences = getSharedPreferences("Authorisation", MODE_PRIVATE);
                    String email = sharedPreferences.getString("email", "");
                    String currentTime = defaultLogic.getDate();
                    if (flag) {

                        defaultLogic.updateAnalyticsData(email, name, currentTime  + "/on");
                        defaultLogic.insertDataDOne(1, email, name, "DOne", getBright());
                    }
                    else {
                        defaultLogic.updateAnalyticsData(email, name, currentTime  + "/off");
                        defaultLogic.insertDataDOne(0, email, name, "DOne", getBright());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        threadOn.start();
    }


}