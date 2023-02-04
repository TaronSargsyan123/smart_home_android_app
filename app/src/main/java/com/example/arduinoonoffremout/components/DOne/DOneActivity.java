package com.example.arduinoonoffremout.components.DOne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.arduinoonoffremout.R;
import com.example.arduinoonoffremout.firebase.DevicesDefaultLogic;
import com.google.android.material.slider.Slider;

public class DOneActivity extends AppCompatActivity {
    private String name;
    private Boolean stage;
    private Slider slider;
    private TextView button;
    private DevicesDefaultLogic defaultLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        slider = findViewById(R.id.d_one_slider_activity);
        button = findViewById(R.id.d_one_button_activity);
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
                defaultLogic.insertDataDOne(1, email, name, "DOne", getBright());
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                defaultLogic.insertDataDOne(1, email, name, "DOne", getBright());
            }
        });



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

                    if (flag) {

                        defaultLogic.insertDataDOne(1, email, name, "DOne", getBright());
                    }
                    else {
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